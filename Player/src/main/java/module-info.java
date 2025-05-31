import dk.sdu.cbse.goodguy.GoodGuyControl;
import dk.sdu.cbse.goodguy.GoodGuyPlugin;
import dk.sdu.cbse.common.services.IEntityProcService;
import dk.sdu.cbse.common.services.IGamePluginService;

module Player {
    exports dk.sdu.cbse.goodguy;
    requires Common;
    requires CommonBullet;
    uses dk.sdu.cbse.common.bullet.PewPewSPI;

    provides IGamePluginService with GoodGuyPlugin;
    provides IEntityProcService with GoodGuyControl;
}