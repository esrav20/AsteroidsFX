import dk.sdu.cbse.common.services.IEntityProcService;
import dk.sdu.cbse.common.services.IPostEntityProcService;

module Collision {
    requires Common;
    requires CommonBullet;
    requires Asteroid;
    provides IPostEntityProcService with dk.sdu.cbse.collision.CollisionControl;
}