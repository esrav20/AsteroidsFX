package dk.sdu.cbse.pewpew;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.Parts.LifePart;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;
import org.springframework.stereotype.Service;

@Service
public class PewPewControl implements IEntityProcService {
    @Override
    public void process(VisualGameData vgData, World world) {

        for (Entity pewpew : world.getEntities(PewPew.class)) {
            // Move bullets faster
            double shiftX = Math.cos(Math.toRadians(pewpew.getRotation()));
            double shiftY = Math.sin(Math.toRadians(pewpew.getRotation()));
            pewpew.setX(pewpew.getX() + shiftX * 8);
            pewpew.setY(pewpew.getY() + shiftY * 8);

            // Remove bullets when they go off screen
            if (pewpew.getX() < -20 || pewpew.getX() > vgData.getDisplayW() + 20 ||
                    pewpew.getY() < -20 || pewpew.getY() > vgData.getDisplayH() + 20) {
                world.removeEntity(pewpew);
                continue;
            }

            // Check life BEFORE processing
            LifePart lifePart = pewpew.getPart(LifePart.class);
            if (lifePart != null) {
                if (lifePart.getLife() <= 0) {
                    world.removeEntity(pewpew);
                }
                lifePart.process(vgData, pewpew);  // Process AFTER checking
            }
        }
    }

    public Entity createPewPew(Entity shooter, VisualGameData vgData) {
        Entity pewpew = new PewPew();
        pewpew.setPolygonCoordinates(3, -1, 3, 1, -3, 1, -3, -1);
        pewpew.setX(shooter.getX());
        pewpew.setY(shooter.getY());
        pewpew.add(new LifePart(1, 1)); // OLIVER'S PATTERN: 1 life, not 90
        pewpew.setRotation(shooter.getRotation());

        // Spawn bullet slightly ahead of shooter
        double shiftX = Math.cos(Math.toRadians(shooter.getRotation()));
        double shiftY = Math.sin(Math.toRadians(shooter.getRotation()));
        pewpew.setX(pewpew.getX() + shiftX * 15);
        pewpew.setY(pewpew.getY() + shiftY * 15);

        return pewpew;
    }
}