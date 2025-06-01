package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.data.Entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void isCollision() {
        Entity enemy = new Entity();
        Entity bullet = new Entity();
        enemy.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        bullet.setPolygonCoordinates(3, -1, 3, 1, -3, 1, -3, -1);

        enemy.setX(50);
        enemy.setY(50);
        bullet.setX(55);
        bullet.setY(55);

        CollisionControl collisionControl = new CollisionControl();

        assertTrue(collisionControl.isCollision(enemy, bullet));
    }

    @Test
    void isNotCollision() {
        Entity enemy = new Entity();
        Entity bullet = new Entity();
        enemy.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        bullet.setPolygonCoordinates(3, -1, 3, 1, -3, 1, -3, -1);

        enemy.setX(50);
        enemy.setY(50);
        bullet.setX(200);
        bullet.setY(200);

        CollisionControl collisionControl = new CollisionControl();

        assertFalse(collisionControl.isCollision(enemy, bullet));
    }
}