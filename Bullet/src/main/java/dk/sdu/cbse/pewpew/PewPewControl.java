package dk.sdu.cbse.pewpew;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;

public class PewPewControl implements IEntityProcService {
    @Override
    public void process(VisualGameData vgData, World world) {

        for (Entity pewpew : world.getEntities(PewPew.class)) {
            double shiftX = Math.cos(Math.toRadians(pewpew.getRotation()));
            double shiftY = Math.sin(Math.toRadians(pewpew.getRotation()));
            pewpew.setX(pewpew.getX() + shiftX * 3);
            pewpew.setY(pewpew.getY() + shiftY * 3);

            if (pewpew.getX() < 0) {
                world.removeEntity(pewpew);
            }
            if (pewpew.getY() < 0) {
                world.removeEntity(pewpew);
            }
            if (pewpew.getX() > vgData.getDisplayW()) {
                world.removeEntity(pewpew);
            }
            if (pewpew.getY() > vgData.getDisplayH()) {
                world.removeEntity(pewpew);
            }
        }


    }

    public Entity createPewPew(Entity shooter, VisualGameData vgData) {
        Entity pewpew = new PewPew();
        pewpew.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);
        pewpew.setX(shooter.getX());
        pewpew.setY(shooter.getY());
        pewpew.setHitPoints(1);
        pewpew.setDmg(1);
        pewpew.setRotation(shooter.getRotation());
        double shiftX = Math.cos(Math.toRadians(shooter.getRotation()));
        double shiftY = Math.sin(Math.toRadians(shooter.getRotation()));
        pewpew.setX(pewpew.getX() + shiftX * 10);
        pewpew.setY(pewpew.getY() + shiftY * 10);
        return pewpew;
    }

}
