package dk.sdu.cbse.goodguy;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import org.springframework.stereotype.Component;

@Component
public class GoodGuyPlugin implements IGamePluginService {

    private Entity goodguy;

    public GoodGuyPlugin() {}

    @Override
    public void start(VisualGameData vgData, World world) {
        goodguy = createGoodGuy(vgData);
        world.addEntity(goodguy);
    }

    private Entity createGoodGuy(VisualGameData vgData) {
        Entity goodguyV = new GoodGuy(); // Fixed: was creating generic Entity
        goodguyV.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        goodguyV.setX(vgData.getDisplayW()/2);
        goodguyV.setY(vgData.getDisplayH()/2);
        goodguyV.setRotation(0); // Start facing right
        goodguyV.add(new dk.sdu.cbse.common.data.Parts.LifePart(5, 1)); // Add life component
        return goodguyV;
    }

    @Override
    public void stop(VisualGameData VGdata, World world) {
        world.removeEntity(goodguy);
    }
}