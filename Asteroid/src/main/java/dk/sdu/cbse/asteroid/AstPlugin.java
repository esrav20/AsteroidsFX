package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;

public class AstPlugin implements IGamePluginService {

    private Entity spaceRock;

    @Override
    public void start(VisualGameData VGdata, World world) {
        spaceRock = createSpaceRock(VGdata);
        world.addEntity(spaceRock);

    }
    private Entity createSpaceRock(VisualGameData VGdata) {
        Entity spaceRock1 = new Entity();
        spaceRock1.setPolygonCoordinates(20, 0, 14, 14, 0, 20, -14, 14, -20, 0, -14, -14, 0, -20, 14, -14);
        spaceRock1.setX(VGdata.getDisplayW()/2);
        spaceRock1.setY(0);
        spaceRock1.setRotation(90);
        spaceRock1.setHitPoints(10);
        spaceRock1.setDmg(10);
        return spaceRock1;
    }

    @Override
    public void stop(VisualGameData VGdata, World world) {
        for (Entity e : world.getEntities()) {
            if (e.getClass() == App.class) {
                world.removeEntity(e);
            }
        }

    }
}
