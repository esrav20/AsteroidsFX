package dk.sdu.cbse.pewpew;

import dk.sdu.cbse.common.bullet.PewPewSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;

public class PewPewPlugin implements IGamePluginService, PewPewSPI {

    private PewPewControl pewPewControl = new PewPewControl();

    @Override
    public Entity createPewPew(Entity entity, VisualGameData VGdata) {
        return pewPewControl.createPewPew(entity, VGdata);
    }

    @Override
    public void start(VisualGameData VGdata, World world) {
        // No initial bullets needed
    }

    @Override
    public void stop(VisualGameData VGdata, World world) {
        for (Entity e : world.getEntities(PewPew.class)) {
            world.removeEntity(e);
        }
    }
}