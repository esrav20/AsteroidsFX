package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;


/**
 *
 */
public interface IGamePluginService {
    void start(VisualGameData VGdata, World world);
    void stop(VisualGameData VGdata, World world);
}
