package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;

import java.util.Random;

public class Asteroid extends Entity {

    public Entity splitAs(Entity entity, int size) {

        Random rand = new Random();

        Entity spaceRock = new Asteroid();
        spaceRock.setX(entity.getX());
        spaceRock.setY(entity.getY());
        spaceRock.setRotation(entity.getRotation() + rand.nextInt(360));
        spaceRock.setHitPoints(7);

        switch (size) {
            case 1:
                spaceRock.setPolygonCoordinates(3, 0, 2, 2, 0, 3, -2, 2, -3, 0, -2, -2, 0, -3, 2, -2);
                break;

            case 2:
                spaceRock.setPolygonCoordinates(5, 0, 3, 3, 0, 5, -3, 3, -5, 0, -3, -3, 0, -5, 3, -3);
                break;
        }
        return spaceRock;
    }
}
