package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;

/**
 *
 */
public interface IEntityProcService {
    void process(VisualGameData vgData, World world);
}
