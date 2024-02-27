package util;

public class HeavyEnemy extends GameObject {

    public int damage;
    private boolean isHit;


    public HeavyEnemy (Point3f centre) {
        super("res/sprites/MissileTwo.png",30,30, centre);
        this.damage = 2;
        this.isHit = false;
    }

    public int getDamage() {
        return this.damage;
    }
    public boolean isHit() {
        return isHit;
    }
}
