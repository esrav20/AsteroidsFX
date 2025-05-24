package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;

import java.util.Random;

public class AstControlSystem implements IEntityProcService {


    @Override
    public void process(VisualGameData vgData, World world) {
        Random random = new Random();

        // Chance to spawn 1
        int randomInt = random.nextInt(50);

        for (Entity spaceRock : world.getEntities(App.class)) {
            double changeX = Math.cos(Math.toRadians(spaceRock.getRotation()));
            double changeY = Math.sin(Math.toRadians(spaceRock.getRotation()));
            spaceRock.setX(spaceRock.getX() + changeX * 0.5);
            spaceRock.setY(spaceRock.getY() + changeY * 0.5);


            if(spaceRock.getHitPoints()==7){

                createMediumSpaceRock(spaceRock, world, -90);
                createMediumSpaceRock(spaceRock, world, 90);
                world.removeEntity(spaceRock);

            }

            if(spaceRock.getHitPoints()==3){

                createSmallSpaceRock(spaceRock, world, -90);
                createSmallSpaceRock(spaceRock, world, 90);
                world.removeEntity(spaceRock);
            }

        }


        if (randomInt==1){
            // Add entities to the world
//            Entity spaceRock;
            createSpaceRock(vgData, world);
//            world.addEntity(spaceRock);
        }

    }

    public void createSpaceRock(VisualGameData vgData, World world) {
        Random random = new Random();

        int randomInt = random.nextInt(vgData.getDisplayW())+1;

        Entity spaceRock = new App();
        spaceRock.setPolygonCoordinates(30, 0, 20, 20, 0, 30, -20, 20, -30, 0, -20, -20, 0, -30, 20, -20);
        spaceRock.setX(randomInt);
        spaceRock.setY(0);
        spaceRock.setRotation(90);
        spaceRock.setHitPoints(10);
        spaceRock.setDmg(10);

        world.addEntity(spaceRock);
    }

    public void createMediumSpaceRock(Entity spaceRock, World world, int rotation){
        // Second spaceRock
        Entity spaceRock1 = new App();
        spaceRock1.setPolygonCoordinates(20, 0, 14, 14, 0, 20, -14, 14, -20, 0, -14, -14, 0, -20, 14, -14);
        spaceRock1.setX(spaceRock.getX());
        spaceRock1.setY(spaceRock.getY());
        spaceRock1.setRotation(spaceRock.getRotation()+rotation);
        spaceRock1.setHitPoints(6);
        spaceRock1.setDmg(10);
        world.addEntity(spaceRock1);
    }

    public void createSmallSpaceRock(Entity spaceRock, World world, int rotation){
        Entity spaceRock2 = new App();
        spaceRock2.setPolygonCoordinates(10, 0, 7, 7, 0, 10, -7, 7, -10, 0, -7, -7, 0, -10, 7, -7);
        spaceRock2.setX(spaceRock.getX());
        spaceRock2.setY(spaceRock.getY());
        spaceRock2.setRotation(spaceRock.getRotation()+rotation);
        spaceRock2.setHitPoints(2);
        spaceRock2.setDmg(10);
        world.addEntity(spaceRock2);
    }
}
