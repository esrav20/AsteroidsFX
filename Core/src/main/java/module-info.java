module Core {
    requires Common;
    requires CommonBullet;
    requires javafx.graphics;
    requires jdk.compiler;

    opens dk.sdu.cbse to javafx.graphics;
    uses dk.sdu.cbse.common.services.IGamePluginService;
    uses dk.sdu.cbse.common.services.IEntityProcService;
}