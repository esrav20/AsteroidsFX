import dk.sdu.cbse.common.services.IEntityProcService;

module Collision {
    requires Common;
    provides IEntityProcService with dk.sdu.cbse.collision.CollisionControl;
}