package dk.sdu.cbse.common.data.Parts;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;

public class LifePart implements EntityPart {

    private int life;
    private int dmg;
    private int dmgTaken;

    public LifePart(int life, int dmg) {
        this.life = life;
        this.dmg = dmg;
    }


    public int getDmgTaken() {
        return dmgTaken;
    }

    public void setDmgTaken(int dmgTaken) {
        this.dmgTaken = dmgTaken;
    }


    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }



    @Override
    public void process(VisualGameData VGData, Entity entity) {
        LifePart lifePart = (LifePart) entity.getPart(LifePart.class);
        lifePart.setLife(lifePart.getLife() - lifePart.getDmgTaken());
        lifePart.setDmgTaken(0);
    }

}
