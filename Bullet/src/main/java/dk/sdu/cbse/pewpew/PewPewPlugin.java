package dk.sdu.cbse.pewpew;

import dk.sdu.cbse.common.bullet.PewPewSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;

public class PewPewPlugin implements IGamePluginService, PewPewSPI {
    @Override
    public Entity createPewPew(Entity entity, VisualGameData VGdata) {
        return null;
    }

    @Override
    public void start(VisualGameData VGdata, World world) {

    }

    @Override
    public void stop(VisualGameData VGdata, World world) {

    }
}
