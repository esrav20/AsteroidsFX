package dk.sdu.cbse.badguy;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.Parts.LifePart;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import org.springframework.stereotype.Component;

@Component
public class BadGuyPlugin implements IGamePluginService {
    private Entity badguy;

    public BadGuyPlugin() {

    }

    @Override
    public void start(VisualGameData VGdata, World world) {
        badguy = createBadGuy(VGdata);
        world.addEntity(badguy);
    }

    private Entity createBadGuy(VisualGameData vGdata) {
        Entity badGuyV = new BadGuy();
        badGuyV.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        badGuyV.setX(vGdata.getDisplayW() / 2 + 100);  // Spawn to the right of center
        badGuyV.setY(vGdata.getDisplayH() / 2 + 100);  // Spawn below center
        badGuyV.setRotation(0);  // Start facing right
        badGuyV.add(new LifePart(3, 1));  // Add life component
        return badGuyV;
    }

    @Override
    public void stop(VisualGameData VGdata, World world) {
        // Remove all bad guys when stopping
        for (Entity entity : world.getEntities(BadGuy.class)) {
            world.removeEntity(entity);
        }
    }
}