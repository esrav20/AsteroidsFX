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
        int RandInt;
        int RandInt2;

        for (Entity e : world.getEntities()) {

            RandInt = rand.nextInt(2);
            RandInt2 = rand.nextInt(10);

            if (RandInt == 0) {
                e.setRotation(e.getRotation() - 5);
            }
            if (RandInt2 == 1) {
                e.setRotation(e.getRotation() + 5);
            }
            if (true) {
                double shiftX = Math.cos(Math.toRadians(e.getRotation()));
                double shiftY = Math.sin(Math.toRadians(e.getRotation()));
                e.setX(e.getX() + shiftX);
                e.setY(e.getY() + shiftY);
            }
            if (RandInt == 1) {
                getPewPewSPI().stream().findFirst().ifPresent(pewSPI -> {
                    world.addEntity(pewSPI.createPewPew(e, vgData));
                });
            }
            if (e.getX()<0){
                e.setX(1);
                e.setRotation(e.getRotation() + 180);
            }
            if (e.getY()<0){
                e.setY(1);
                e.setRotation(e.getRotation() + 180);
            }
            if (e.getX() > vgData.getDisplayW()) {
                e.setX(vgData.getDisplayW()-1);
                e.setRotation(e.getRotation() + 180);
            }
            if (e.getY() > vgData.getDisplayH()) {
                e.setY(vgData.getDisplayH()-1);
                e.setRotation(e.getRotation() + 180);
            }

        }
    }
    private Collection<? extends PewPewSPI> getPewPewSPI() {
        return ServiceLoader.load(PewPewSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }


}
