package dk.sdu.cbse.badguy;

import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.Parts.LifePart;
import dk.sdu.cbse.common.bullet.PewPewSPI;
import dk.sdu.cbse.goodguy.GoodGuy;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class BadGuyControl implements IEntityProcService {
    private static int shootCooldown = 0;
    private static int respawnCooldown = 0;
    private static Random random = new Random();

    @Override
    public void process(VisualGameData vgData, World world) {
        // Decrease cooldowns
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        if (respawnCooldown > 0) {
            respawnCooldown--;
        }

        // Find player for AI targeting
        Entity player = world.getEntities(GoodGuy.class).stream().findFirst().orElse(null);

        // Process existing enemies
        for (Entity badGuy : world.getEntities(BadGuy.class)) {
            // Process enemy life first (like Oliver's pattern)
            LifePart lifePart = badGuy.getPart(LifePart.class);
            if (lifePart != null) {
                // Check if enemy should be destroyed
                if (lifePart.getLife() <= 0) {
                    world.removeEntity(badGuy);
                    respawnCooldown = 300; // 5 seconds at 60fps
                    continue; // Skip processing this enemy
                }
                // Process damage
                lifePart.process(vgData, badGuy);
            }

            // AI: Move toward player if player exists
            if (player != null) {
                double dx = player.getX() - badGuy.getX();
                double dy = player.getY() - badGuy.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                // Only chase if not too close (avoid collision)
                if (distance > 100) {
                    // Calculate angle to player
                    double angleToPlayer = Math.toDegrees(Math.atan2(dy, dx));

                    // Gradually turn toward player
                    double angleDiff = angleToPlayer - badGuy.getRotation();
                    // Normalize angle difference to -180 to 180
                    while (angleDiff > 180) angleDiff -= 360;
                    while (angleDiff < -180) angleDiff += 360;

                    // Turn toward player (slowly)
                    if (Math.abs(angleDiff) > 5) {
                        if (angleDiff > 0) {
                            badGuy.setRotation(badGuy.getRotation() + 2);
                        } else {
                            badGuy.setRotation(badGuy.getRotation() - 2);
                        }
                    }
                }
            }

            // Move forward (slower than player)
            double shiftX = Math.cos(Math.toRadians(badGuy.getRotation()));
            double shiftY = Math.sin(Math.toRadians(badGuy.getRotation()));
            badGuy.setX(badGuy.getX() + shiftX * 1.5);
            badGuy.setY(badGuy.getY() + shiftY * 1.5);

            // Shoot at player occasionally
            if (player != null && shootCooldown <= 0 && random.nextInt(120) == 0) {
                getPewPewSPI().stream().findFirst().ifPresent(pewSPI -> {
                    Entity bullet = pewSPI.createPewPew(badGuy, vgData);
                    world.addEntity(bullet);
                });
                shootCooldown = 60; // 1 second cooldown
            }

            // Bounce off walls
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

        // Check if we need to spawn a new enemy
        if (world.getEntities(BadGuy.class).isEmpty() && respawnCooldown <= 0) {
            spawnNewEnemy(vgData, world);
        }
    }

    private void spawnNewEnemy(VisualGameData vgData, World world) {
        Entity newEnemy = new BadGuy();
        newEnemy.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);

        // Spawn at random edge of screen
        int edge = random.nextInt(4);
        switch (edge) {
            case 0: // Top
                newEnemy.setX(random.nextInt(vgData.getDisplayW()));
                newEnemy.setY(-30);
                newEnemy.setRotation(90); // Face down
                break;
            case 1: // Right
                newEnemy.setX(vgData.getDisplayW() + 30);
                newEnemy.setY(random.nextInt(vgData.getDisplayH()));
                newEnemy.setRotation(180); // Face left
                break;
            case 2: // Bottom
                newEnemy.setX(random.nextInt(vgData.getDisplayW()));
                newEnemy.setY(vgData.getDisplayH() + 30);
                newEnemy.setRotation(270); // Face up
                break;
            case 3: // Left
                newEnemy.setX(-30);
                newEnemy.setY(random.nextInt(vgData.getDisplayH()));
                newEnemy.setRotation(0); // Face right
                break;
        }

        newEnemy.add(new LifePart(3, 1)); // 3 hit points
        world.addEntity(newEnemy);
    }

    private Collection<? extends PewPewSPI> getPewPewSPI() {
        return ServiceLoader.load(PewPewSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}