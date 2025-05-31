package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.Parts.LifePart;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AstControlSystem implements IEntityProcService {

    @Override
    public void process(VisualGameData vgData, World world) {

        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            // Move asteroids slower
            double changeX = Math.cos(Math.toRadians(asteroid.getRotation()));
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation()));
            asteroid.setX(asteroid.getX() + changeX * 1.0);
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
                // Process damage first
                lifePart.process(vgData, asteroid);

                // Check what type of asteroid this is based on its polygon coordinates
                int currentLife = lifePart.getLife();

                // Determine asteroid type by polygon size
                double[] coords = asteroid.getPolygonCoordinates();
                boolean isLargeAsteroid = coords[0] == 30;
                boolean isMediumAsteroid = coords[0] == 20;
                boolean isSmallAsteroid = coords[0] == 10;

                // Large asteroid splits when destroyed
                if (isLargeAsteroid && currentLife <= 0) {
                    createMediumAsteroid(asteroid, world, -45);
                    createMediumAsteroid(asteroid, world, 45);
                    world.removeEntity(asteroid);
                }
                // Medium asteroid splits when destroyed
                else if (isMediumAsteroid && currentLife <= 0) {
                    createSmallAsteroid(asteroid, world, -60);
                    createSmallAsteroid(asteroid, world, 60);
                    world.removeEntity(asteroid);
                }
                // Small asteroid just disappears
                else if (isSmallAsteroid && currentLife <= 0) {
                    world.removeEntity(asteroid);
                }
            }
        }

        // Spawn new asteroids occasionally
        Random random = new Random();
        int randomInt = random.nextInt(600);

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

        asteroid1.setRotation(random.nextInt(360));
        asteroid1.add(new LifePart(3, 1)); // Large asteroids have 3 HP

        world.addEntity(asteroid1);
    }

    public void createMediumAsteroid(Entity asteroid, World world, int rotationOffset) {
        Entity asteroid1 = new Asteroid();
        asteroid1.setPolygonCoordinates(20, 0, 14, 14, 0, 20, -14, 14, -20, 0, -14, -14, 0, -20, 14, -14);
        asteroid1.setX(asteroid.getX());
        asteroid1.setY(asteroid.getY());
        asteroid1.setRotation(asteroid.getRotation() + rotationOffset);
        asteroid1.add(new LifePart(2, 1)); // Medium asteroids have 2 HP

        world.addEntity(asteroid1);
    }

    public void createSmallAsteroid(Entity asteroid, World world, int rotationOffset) {
        Entity asteroid1 = new Asteroid();
        asteroid1.setPolygonCoordinates(10, 0, 7, 7, 0, 10, -7, 7, -10, 0, -7, -7, 0, -10, 7, -7);
        asteroid1.setX(asteroid.getX());
        asteroid1.setY(asteroid.getY());
        asteroid1.setRotation(asteroid.getRotation() + rotationOffset);
        asteroid1.add(new LifePart(1, 1)); // Small asteroids have 1 HP

        world.addEntity(asteroid1);
    }
}