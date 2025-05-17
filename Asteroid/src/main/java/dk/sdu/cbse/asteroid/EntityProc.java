package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;

public class EntityProc implements IEntityProcService {

    @Override
    public void process(VisualGameData vgData, World world) {
        System.out.println("EntityProc");

    }
}
