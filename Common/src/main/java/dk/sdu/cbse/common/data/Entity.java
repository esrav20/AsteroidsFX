package dk.sdu.cbse.common.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

public class Entity implements Serializable {
    private final UUID ID = UUID.randomUUID();

    private double[] polygonCoordinates;
    private double x;
    private double y;
    private double rotation;
    private int hitPoints;

    private int dmg;



    public String getID() {
        return ID.toString();
    }

    public double getWidth(){
        double[] coordinates = getPolygonCoordinates();
        Double max = Arrays.stream(coordinates).max().orElse(-1);
        return max*2;
    }

    public void setPolygonCoordinates(double... coordinates ) {
        this.polygonCoordinates = coordinates;
    }

    public double[] getPolygonCoordinates() {
        return polygonCoordinates;
    }


    public void setX(double x) {
        this.x =x;
    }

    public double getX() {
        return x;
    }


    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getRotation() {
        return rotation;
    }


    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }


    public int getHitPoints() {
        return hitPoints;
    }


    public void setDmg(int dmg) {
        this.dmg = dmg;
    }


    public int getDmg() {
        return dmg;
    }


}
