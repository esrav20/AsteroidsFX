import dk.sdu.cbse.badguy.BadGuyControl;
import dk.sdu.cbse.badguy.BadGuyPlugin;
import dk.sdu.cbse.common.services.IEntityProcService;
import dk.sdu.cbse.common.services.IGamePluginService;

module Enemy {
    exports dk.sdu.cbse.badguy;
    requires Common;
    requires CommonBullet;
    requires Player;
    uses dk.sdu.cbse.common.bullet.PewPewSPI;
    provides IGamePluginService with BadGuyPlugin;
    provides IEntityProcService with BadGuyControl;
}