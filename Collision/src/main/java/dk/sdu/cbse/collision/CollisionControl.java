package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;

import static java.lang.Math.sqrt;

public class CollisionControl implements IEntityProcService {

    @Override
    public void process(VisualGameData vgData, World world) {

        for (Entity e : world.getEntities()) {
            for (Entity e1 : world.getEntities()) {
                if (isCollision(e, e1) && e.getClass() != e1.getClass()) {
                    System.out.println(e.getClass());
                    System.out.println(e1.getClass());
                    System.out.println("SpaceRocks : " + e.getWidth() + "Bullet : " + e1.getWidth());

                    System.out.println(e.getDmg());
                    System.out.println(e1.getDmg());
                    e.setHitPoints(e.getHitPoints() - e1.getDmg());
                    e1.setHitPoints(e1.getHitPoints() -e.getDmg());

                    if(e.getHitPoints()<1){
                        world.removeEntity(e);
                    }
                    if(e1.getHitPoints()<1){
                        world.removeEntity(e1);
                    }


                }
            }
        }

    }
    public boolean isCollision(Entity e1, Entity e2) {

        double x1 = e1.getX();
        double y1 = e1.getY();

        double x2 = e2.getX();
        double y2 = e2.getY();

        double result = sqrt(((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2)));
        double e1Width = e1.getWidth()/2;
        double e2Width = e2.getWidth()/2;

        if (result<e1Width+e2Width) {
            return true;
        }
        return false;
    }
}
