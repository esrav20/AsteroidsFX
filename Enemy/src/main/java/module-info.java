import dk.sdu.cbse.badguy.BadGuyControl;
import dk.sdu.cbse.badguy.BadGuyPlugin;
import dk.sdu.cbse.common.services.IEntityProcService;
import dk.sdu.cbse.common.services.IGamePluginService;

module Enemy {
    requires Common;
    requires CommonBullet;
    uses dk.sdu.cbse.common.bullet.PewPewSPI;
    provides IGamePluginService with BadGuyPlugin;
    provides IEntityProcService with BadGuyControl;
}