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
            // Move asteroids slower
            double changeX = Math.cos(Math.toRadians(asteroid.getRotation()));
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation()));
            asteroid.setX(asteroid.getX() + changeX * 1.0); // Slower movement
            asteroid.setY(asteroid.getY() + changeY * 1.0);

            // Wrap asteroids around screen edges
            if (asteroid.getX() < -30) {
                asteroid.setX(vgData.getDisplayW() + 30);
            }
            if (asteroid.getX() > vgData.getDisplayW() + 30) {
                asteroid.setX(-30);
            }
            if (asteroid.getY() < -30) {
                asteroid.setY(vgData.getDisplayH() + 30);
            }
            if (asteroid.getY() > vgData.getDisplayH() + 30) {
                asteroid.setY(-30);
            }

            LifePart lifePart = (LifePart) asteroid.getPart(LifePart.class);
            if (lifePart != null) {
                lifePart.process(vgData, asteroid);

                // Large asteroid breaks into medium asteroids
                if (lifePart.getLife() == 7) {
                    createMediumAsteroid(asteroid, world, -45);
                    createMediumAsteroid(asteroid, world, 45);
                    world.removeEntity(asteroid);
                }
                // Medium asteroid breaks into small asteroids
                else if (lifePart.getLife() == 3) {
                    createSmallAsteroid(asteroid, world, -60);
                    createSmallAsteroid(asteroid, world, 60);
                    world.removeEntity(asteroid);
                }
                // Small asteroid just disappears
                else if (lifePart.getLife() <= 0) {
                    world.removeEntity(asteroid);
                }
            }
        }

        // Spawn new asteroids occasionally (much less frequent)
        Random random = new Random();
        int randomInt = random.nextInt(600); // About once every 10 seconds

        if (randomInt == 1) {
            createAsteroid(vgData, world);
        }
    }

    public void createAsteroid(VisualGameData vgData, World world) {
        Random random = new Random();

        Entity asteroid1 = new Asteroid();
        asteroid1.setPolygonCoordinates(30, 0, 20, 20, 0, 30, -20, 20, -30, 0, -20, -20, 0, -30, 20, -20);

        // Spawn at random edge of screen
        int edge = random.nextInt(4);
        switch (edge) {
            case 0: // Top
                asteroid1.setX(random.nextInt(vgData.getDisplayW()));
                asteroid1.setY(-30);
                break;
            case 1: // Right
                asteroid1.setX(vgData.getDisplayW() + 30);
                asteroid1.setY(random.nextInt(vgData.getDisplayH()));
                break;
            case 2: // Bottom
                asteroid1.setX(random.nextInt(vgData.getDisplayW()));
                asteroid1.setY(vgData.getDisplayH() + 30);
                break;
            case 3: // Left
                asteroid1.setX(-30);
                asteroid1.setY(random.nextInt(vgData.getDisplayH()));
                break;
        }

        asteroid1.setRotation(random.nextInt(360)); // Random direction
        asteroid1.add(new LifePart(10, 1));

        world.addEntity(asteroid1);
    }

    public void createMediumAsteroid(Entity asteroid, World world, int rotationOffset) {
        Entity asteroid1 = new Asteroid();
        asteroid1.setPolygonCoordinates(20, 0, 14, 14, 0, 20, -14, 14, -20, 0, -14, -14, 0, -20, 14, -14);
        asteroid1.setX(asteroid.getX());
        asteroid1.setY(asteroid.getY());
        asteroid1.setRotation(asteroid.getRotation() + rotationOffset);
        asteroid1.add(new LifePart(6, 1));

        world.addEntity(asteroid1);
    }

    public void createSmallAsteroid(Entity asteroid, World world, int rotationOffset) {
        Entity asteroid1 = new Asteroid();
        asteroid1.setPolygonCoordinates(10, 0, 7, 7, 0, 10, -7, 7, -10, 0, -7, -7, 0, -10, 7, -7);
        asteroid1.setX(asteroid.getX());
        asteroid1.setY(asteroid.getY());
        asteroid1.setRotation(asteroid.getRotation() + rotationOffset);
        asteroid1.add(new LifePart(2, 1));
        world.addEntity(asteroid1);
    }
}