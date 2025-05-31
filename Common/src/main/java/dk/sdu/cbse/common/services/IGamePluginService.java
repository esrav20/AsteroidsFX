package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;


/**
 *
 *
 * Pre-condition: All gamedata must be loaded and a world must be set. And the game is either started or stopped
 * Post-condition: The game is either started or stopped
 *
 * @param vgData
 * @param world
 * @throws IllegalStateException if no gameData or world
 *
 */
public interface IGamePluginService {
    void start(VisualGameData vgData, World world);
    void stop(VisualGameData vgData, World world);
}
