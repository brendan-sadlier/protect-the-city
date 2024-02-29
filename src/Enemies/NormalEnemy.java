package Enemies;

import util.Point3f;

public class NormalEnemy extends Enemy {

    public NormalEnemy(Point3f startingPos) {
        super("res/sprites/MissileOne.png", 1, 50, 50 ,startingPos);
    }
}
