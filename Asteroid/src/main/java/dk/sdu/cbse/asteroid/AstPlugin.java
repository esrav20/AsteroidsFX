package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.Parts.LifePart;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import org.springframework.stereotype.Component;

@Component
public class AstPlugin implements IGamePluginService {

    private Entity asteroid;

    @Override
    public void start(VisualGameData vgData, World world) {
        asteroid = createAsteroid(vgData);
        world.addEntity(asteroid);

    }
    private Entity createAsteroid(VisualGameData vgData) {

        Entity asteroid1 = new Asteroid();
        asteroid1.setPolygonCoordinates(20, 0, 14, 14, 0, 20,
                -14, 14, -20, 0, -14, -14, 0, -20, 14, -14);
        asteroid1.setX(vgData.getDisplayW()/2);
        asteroid1.setY(0);
        asteroid1.setRotation(90);
        asteroid1.add(new LifePart(2, 1)); // Medium asteroid with 2 HP

        return asteroid1;
    }

    @Override
    public void stop(VisualGameData VGdata, World world) {
        for (Entity e : world.getEntities()) {
            if (e.getClass() == Asteroid.class) {
                world.removeEntity(e);
            }
        }

    }
}