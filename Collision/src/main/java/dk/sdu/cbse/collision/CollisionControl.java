package dk.sdu.cbse.collision;


import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.Parts.LifePart;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IPostEntityProcService;
import dk.sdu.cbse.asteroid.Asteroid;
import dk.sdu.cbse.badguy.BadGuy;
import dk.sdu.cbse.goodguy.GoodGuy;
import dk.sdu.cbse.pewpew.PewPew;

import java.util.ArrayList;
import java.util.List;

public class CollisionControl implements IPostEntityProcService {

    @Override
    public void process(VisualGameData vgData, World world) {

        List<Entity> bullets = new ArrayList<>(world.getEntities(PewPew.class));
        List<Entity> asteroids = new ArrayList<>(world.getEntities(Asteroid.class));
        List<Entity> enemies = new ArrayList<>(world.getEntities(BadGuy.class));
        List<Entity> players = new ArrayList<>(world.getEntities(GoodGuy.class));

        // Simple collision processing - bullets hit everything
        for (Entity bullet : bullets) {
            if (!world.getEntities().contains(bullet)) continue;

            // Bullet vs Asteroids
            for (Entity asteroid : asteroids) {
                if (!world.getEntities().contains(asteroid)) continue;

                if (isCollision(bullet, asteroid)) {
                    world.removeEntity(bullet);

                    LifePart asteroidLife = asteroid.getPart(LifePart.class);
                    if (asteroidLife != null) {
                        int currentLife = asteroidLife.getLife();
                        asteroidLife.setLife(currentLife - 1);

                        if (asteroidLife.getLife() <= 0) {
                            world.removeEntity(asteroid);
                        }
                    }
                    break;
                }
            }

            // Bullet vs Enemies (if bullet still exists)
            if (world.getEntities().contains(bullet)) {
                for (Entity enemy : enemies) {
                    if (!world.getEntities().contains(enemy)) continue;

                    if (isCollision(bullet, enemy)) {
                        world.removeEntity(bullet);

                        LifePart enemyLife = enemy.getPart(LifePart.class);
                        if (enemyLife != null) {
                            int currentLife = enemyLife.getLife();
                            enemyLife.setLife(currentLife - 1);

                            if (enemyLife.getLife() <= 0) {
                                world.removeEntity(enemy);
                            }
                        }
                        break;
                    }
                }
            }

            // Bullet vs Player (if bullet still exists)
            if (world.getEntities().contains(bullet)) {
                for (Entity player : players) {
                    if (!world.getEntities().contains(player)) continue;

                    if (isCollision(bullet, player)) {
                        world.removeEntity(bullet);

                        LifePart playerLife = player.getPart(LifePart.class);
                        if (playerLife != null) {
                            int currentLife = playerLife.getLife();
                            playerLife.setLife(currentLife - 1);
                            // No game over message - just take damage silently
                        }
                        break;
                    }
                }
            }
        }

        // Player vs Asteroid collisions
        for (Entity player : players) {
            if (!world.getEntities().contains(player)) continue;

            for (Entity asteroid : asteroids) {
                if (!world.getEntities().contains(asteroid)) continue;

                if (isCollision(player, asteroid)) {
                    LifePart playerLife = player.getPart(LifePart.class);
                    if (playerLife != null) {
                        int currentLife = playerLife.getLife();
                        playerLife.setLife(currentLife - 1);
                    }

                    LifePart asteroidLife = asteroid.getPart(LifePart.class);
                    if (asteroidLife != null) {
                        int currentLife = asteroidLife.getLife();
                        asteroidLife.setLife(currentLife - 1);
                        if (asteroidLife.getLife() <= 0) {
                            world.removeEntity(asteroid);
                        }
                    }
                }
            }
        }
    }

    /**
     * Public collision detection method for testing
     */
    public boolean isCollision(Entity entity1, Entity entity2) {
        double dx = entity1.getX() - entity2.getX();
        double dy = entity1.getY() - entity2.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < 25;
    }
}