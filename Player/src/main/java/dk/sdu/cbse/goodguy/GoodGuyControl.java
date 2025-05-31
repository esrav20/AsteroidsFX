package dk.sdu.cbse.goodguy;

import dk.sdu.cbse.common.bullet.PewPewSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameControls;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class GoodGuyControl implements IEntityProcService {
    private static int shootCooldown = 0;

    @Override
    public void process(VisualGameData vgData, World world) {
        // Decrease shoot cooldown every frame
        if (shootCooldown > 0) {
            shootCooldown--;
        }

        for (Entity goodguy : world.getEntities(GoodGuy.class)) {
            // Rotation controls
            if (vgData.getControls().isDown(GameControls.LEFT)) {
                goodguy.setRotation(goodguy.getRotation() - 5);
            }
            if (vgData.getControls().isDown(GameControls.RIGHT)) {
                goodguy.setRotation(goodguy.getRotation() + 5);
            }

            // Movement
            if (vgData.getControls().isDown(GameControls.UP)) {
                double shiftX = Math.cos(Math.toRadians(goodguy.getRotation()));
                double shiftY = Math.sin(Math.toRadians(goodguy.getRotation()));
                goodguy.setX(goodguy.getX() + shiftX * 3);
                goodguy.setY(goodguy.getY() + shiftY * 3);
            }

            // Shooting
            if (vgData.getControls().isDown(GameControls.SPACE) && shootCooldown <= 0) {
                getPewPewSPI().stream().findFirst().ifPresent(pewSPI -> {
                    Entity bullet = pewSPI.createPewPew(goodguy, vgData);
                    world.addEntity(bullet);
                });
                shootCooldown = 15;
            }

            // Keep player on screen
            if (goodguy.getX() < 0) goodguy.setX(1);
            if (goodguy.getY() < 0) goodguy.setY(1);
            if (goodguy.getX() > vgData.getDisplayW()) goodguy.setX(vgData.getDisplayW() - 1);
            if (goodguy.getY() > vgData.getDisplayH()) goodguy.setY(vgData.getDisplayH() - 1);
        }
    }

    private Collection<? extends PewPewSPI> getPewPewSPI() {
        return ServiceLoader.load(PewPewSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}