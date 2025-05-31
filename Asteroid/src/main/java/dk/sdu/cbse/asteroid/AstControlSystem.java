package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.Parts.LifePart;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;

import java.util.Random;

public class AstControlSystem implements IEntityProcService {


    @Override
    public void process(VisualGameData vgData, World world) {


        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            double changeX = Math.cos(Math.toRadians(asteroid.getRotation()));
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation()));
            asteroid.setX(asteroid.getX() + changeX * 0.5);
            asteroid.setY(asteroid.getY() + changeY * 0.5);


            LifePart lifePart = (LifePart) asteroid.getPart(LifePart.class);
            lifePart.process(vgData, asteroid);

            if (lifePart.getLife() == 7) {
                createMediumAsteroid(asteroid, world, -90);
                createMediumAsteroid(asteroid, world, 90);
                world.removeEntity(asteroid);

            }

            if (lifePart.getLife() == 3) {
                createSmallAsteroid(asteroid, world, -90);
                createSmallAsteroid(asteroid, world, 90);
                world.removeEntity(asteroid);
            }
            if (lifePart.getLife() <= 0) {
                world.removeEntity(asteroid);
            }
        }


        Random random = new Random();

        // Chance to spawn one asteroid
        int randomInt = random.nextInt(50);

        if (randomInt == 1) {
            // Add entities to the world
            createAsteroid(vgData, world);
        }

    }

    public void createAsteroid(VisualGameData vgData, World world) {
        Random random = new Random();
        int randomInt = random.nextInt(vgData.getDisplayW()) + 1;
        Entity asteroid1 = new Asteroid();
        asteroid1.setPolygonCoordinates(30, 0, 20, 20, 0, 30, -20, 20, -30, 0, -20, -20, 0, -30, 20, -20);
        asteroid1.setX(randomInt);
        asteroid1.setY(0);
        asteroid1.setRotation(90);
        asteroid1.add(new LifePart(10, 10));

        world.addEntity(asteroid1);
    }

    public void createMediumAsteroid(Entity asteroid, World world, int rotation) {
        // Second asteroid
        Entity asteroid1 = new Asteroid();
        asteroid1.setPolygonCoordinates(20, 0, 14, 14, 0, 20, -14, 14, -20, 0, -14, -14, 0, -20, 14, -14);
        asteroid1.setX(asteroid.getX());
        asteroid1.setY(asteroid.getY());
        asteroid1.setRotation(asteroid.getRotation() + rotation);
        asteroid1.add(new LifePart(6, 10));

        world.addEntity(asteroid1);
    }

    public void createSmallAsteroid(Entity asteroid, World world, int rotation) {
        Entity asteroid1 = new Asteroid();
        asteroid1.setPolygonCoordinates(10, 0, 7, 7, 0, 10, -7, 7, -10, 0, -7, -7, 0, -10, 7, -7);
        asteroid1.setX(asteroid.getX());
        asteroid1.setY(asteroid.getY());
        asteroid1.setRotation(asteroid.getRotation() + rotation);
        asteroid1.add(new LifePart(2, 10));
        world.addEntity(asteroid1);
    }


}
