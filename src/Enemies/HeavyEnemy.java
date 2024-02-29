package Enemies;

import util.GameObject;
import util.Point3f;

public class HeavyEnemy extends Enemy {

    public int damage;
    private boolean isHit;


    public HeavyEnemy (Point3f startingPos) {
        super("res/sprites/MissileTwo.png",30, 30, 30, startingPos);
    }

    public int getDamage() {
        return this.damage;
    }
    public boolean isHit() {
        return isHit;
    }
}
