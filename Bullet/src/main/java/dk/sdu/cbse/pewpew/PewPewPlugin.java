// Example 2: Convert PewPewPlugin with Dependency Injection
package dk.sdu.cbse.pewpew;

import dk.sdu.cbse.common.bullet.PewPewSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PewPewPlugin implements IGamePluginService, PewPewSPI {

    @Autowired
    private PewPewControl pewPewControl;

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
