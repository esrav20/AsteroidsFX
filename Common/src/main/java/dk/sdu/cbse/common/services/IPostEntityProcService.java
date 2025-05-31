package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;

/**
 * Pre-condition: All gamedata must be loaded and a world must be set.
 * Post-condition: All processes must be terminated
 */

public interface IPostEntityProcService {

    void process(VisualGameData vgData, World world);
}
