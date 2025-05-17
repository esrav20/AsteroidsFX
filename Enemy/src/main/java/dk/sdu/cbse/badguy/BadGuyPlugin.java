package dk.sdu.cbse.badguy;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;

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
        badGuyV.setX(vGdata.getDisplayH()/2+100);
        badGuyV.setY(vGdata.getDisplayW()/2+100);
        return badGuyV;
    }

    @Override
    public void stop(VisualGameData VGdata, World world) {
        world.removeEntity(badguy);

    }
}
