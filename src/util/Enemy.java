package util;

import java.util.Random;

public class Enemy extends GameObject {

    public int damage;
    private boolean isHit; // Indicates if the enemy has been hit


    public Enemy(Point3f centre) {
        super("res/sprites/MissileOne.png",50,50, centre);
        this.damage = 1;
        this.isHit = false;
    }


    // Getters
    public int getDamage() {
        return this.damage;
    }

    public boolean isHit() {
        return isHit;
    }

}
