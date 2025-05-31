package dk.sdu.cbse.badguy;

import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.bullet.PewPewSPI;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class BadGuyControl implements IEntityProcService {

    @Override
    public void process(VisualGameData vgData, World world) {
        Random rand = new Random();

        for (Entity badGuy : world.getEntities(BadGuy.class)) {

            // Random direction change occasionally
            if (rand.nextInt(120) == 0) {
                badGuy.setRotation(badGuy.getRotation() + (rand.nextBoolean() ? 45 : -45));
            }

            // Move forward
            double shiftX = Math.cos(Math.toRadians(badGuy.getRotation()));
            double shiftY = Math.sin(Math.toRadians(badGuy.getRotation()));
            badGuy.setX(badGuy.getX() + shiftX * 1.5);
            badGuy.setY(badGuy.getY() + shiftY * 1.5);

            // Shoot occasionally
            if (rand.nextInt(300) == 0) {
                getPewPewSPI().stream().findFirst().ifPresent(pewSPI -> {
                    world.addEntity(pewSPI.createPewPew(badGuy, vgData));
                });
            }

            // Bounce off screen edges
            if (badGuy.getX() < 0) {
                badGuy.setX(1);
                badGuy.setRotation(badGuy.getRotation() + 180);
            }
            if (badGuy.getY() < 0) {
                badGuy.setY(1);
                badGuy.setRotation(badGuy.getRotation() + 180);
            }
            if (badGuy.getX() > vgData.getDisplayW()) {
                badGuy.setX(vgData.getDisplayW() - 1);
                badGuy.setRotation(badGuy.getRotation() + 180);
            }
            if (badGuy.getY() > vgData.getDisplayH()) {
                badGuy.setY(vgData.getDisplayH() - 1);
                badGuy.setRotation(badGuy.getRotation() + 180);
            }
        }
    }

    private Collection<? extends PewPewSPI> getPewPewSPI() {
        return ServiceLoader.load(PewPewSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}