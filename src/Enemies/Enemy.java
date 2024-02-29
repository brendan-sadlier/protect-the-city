package Enemies;

import util.GameObject;
import util.Point3f;

import java.util.Random;

public class Enemy extends GameObject {

    public int damage;
    private boolean isHit; // Indicates if the enemy has been hit

    public Enemy(String texturePath, int damage, int height, int width, Point3f startingPos) {
        super(texturePath, width, height, startingPos);
        this.damage = damage;
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
