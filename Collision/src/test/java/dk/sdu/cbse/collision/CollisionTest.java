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
        Entity spaceRock = new Entity();
        Entity bullet = new Entity();
        spaceRock.setPolygonCoordinates(20, 0, 14, 14, 0, 20, -14, 14, -20, 0, -14, -14, 0, -20, 14, -14);
        bullet.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);

        spaceRock.setX(10);
        spaceRock.setY(10);
        bullet.setX(15);
        bullet.setY(15);

        CollisionControl collisionControl = new CollisionControl();

        assertTrue(collisionControl.isCollision(spaceRock, bullet));
    }
    @Test
    void isNotCollision() {

        Entity spaceRock = new Entity();
        Entity bullet = new Entity();
        spaceRock.setPolygonCoordinates(20, 0, 14, 14, 0, 20, -14, 14, -20, 0, -14, -14, 0, -20, 14, -14);
        bullet.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);

        spaceRock.setX(10);
        spaceRock.setY(10);
        bullet.setX(150);
        bullet.setY(150);

        CollisionControl collisionControl = new CollisionControl();

        assertFalse(collisionControl.isCollision(spaceRock, bullet));
    }



}