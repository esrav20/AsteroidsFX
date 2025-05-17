import dk.sdu.cbse.common.bullet.PewPewSPI;
import dk.sdu.cbse.common.services.IEntityProcService;
import dk.sdu.cbse.common.services.IGamePluginService;
module Bullet {
    exports dk.sdu.cbse.pewpew;
    requires Common;
    requires CommonBullet;
    provides IGamePluginService with dk.sdu.cbse.pewpew.PewPewPlugin;
    provides PewPewSPI with dk.sdu.cbse.pewpew.PewPewPlugin;
    provides IEntityProcService with dk.sdu.cbse.pewpew.PewPewControl;

}