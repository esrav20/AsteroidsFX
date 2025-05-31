import dk.sdu.cbse.asteroid.AstPlugin;
import dk.sdu.cbse.asteroid.AstControlSystem;
import dk.sdu.cbse.common.services.IEntityProcService;
import dk.sdu.cbse.common.services.IGamePluginService;

module Asteroid {
    requires Common;
    provides IGamePluginService with AstPlugin;
    provides IEntityProcService with AstControlSystem;
}