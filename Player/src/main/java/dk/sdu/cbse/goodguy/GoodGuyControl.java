package dk.sdu.cbse.goodguy;

import dk.sdu.cbse.common.bullet.PewPewSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameControls;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class GoodGuyControl implements IEntityProcService {
    @Override
    public void process(VisualGameData vgData, World world) {

        for (Entity goodguy : world.getEntities(GoodGuy.class)) {
            if (vgData.getControls().isDown(GameControls.LEFT)) {
                goodguy.setRotation(goodguy.getRotation() - 5);
            }
            if (vgData.getControls().isDown(GameControls.RIGHT)) {
                goodguy.setRotation(goodguy.getRotation() + 5);
            }
            if (vgData.getControls().isDown(GameControls.UP)) {
                double shiftX = Math.cos(Math.toRadians(goodguy.getRotation()));
                double shiftY = Math.sin(Math.toRadians(goodguy.getRotation()));

                goodguy.setX(goodguy.getX() + shiftX);
                goodguy.setY(goodguy.getY() + shiftY);
            }
            if (vgData.getControls().isDown(GameControls.SPACE)) {
                getPewPewSPI().stream().findFirst().ifPresent(
                        pewPewSPI -> {
                            world.addEntity(pewPewSPI.createPewPew(goodguy, vgData));
                        }
                );
            }
            if (goodguy.getX()<0) {
                goodguy.setX(1);
            }
            if (goodguy.getY()<0) {
                goodguy.setY(1);
            }
            if (goodguy.getX() > vgData.getDisplayW()) {
                goodguy.setX(vgData.getDisplayW()-1);
            }
            if (goodguy.getY() > vgData.getDisplayH()) {
                goodguy.setY(vgData.getDisplayH()-1);
            }
        }
    }
    private Collection<? extends PewPewSPI> getPewPewSPI() {
        return ServiceLoader.load(PewPewSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
