package Levels;

import util.Enemy;
import util.HeavyEnemy;
import util.Point3f;

import java.util.concurrent.CopyOnWriteArrayList;

public class LevelTwo extends Level implements Levels {

    private final int NUMBER_OF_NORMAL_ENEMIES = 1;
    private final int NUMBER_OF_HEAVY_ENEMIES = 1;


    public LevelTwo() {

        super("res/gui/backgrounds/BG_LevelTwo.png");

        setNormalEnemies();
        setHeavyEnemies();
    }

    private void setNormalEnemies() {

        normalEnemies = new CopyOnWriteArrayList<>();

        for (int i = 0; i < NUMBER_OF_NORMAL_ENEMIES; i++) {
            normalEnemies.add(new Enemy(new Point3f(((float)Math.random()*1000), 0,0)));
        }

    }

    private void setHeavyEnemies() {
        heavyEnemies = new CopyOnWriteArrayList<>();
        for (int i = 0; i < NUMBER_OF_HEAVY_ENEMIES; i++) {
            heavyEnemies.add(new HeavyEnemy(new Point3f(((float)Math.random()*1000), 0,0)));
        }
    }



    public int getNumOfNormalEnemies() {
        return NUMBER_OF_NORMAL_ENEMIES;
    }



    public int getNumOfHeavyEnemies() {
        return NUMBER_OF_HEAVY_ENEMIES;
    }

    public int getStatingNumOfBullets() {
        return 10;
    }

    public int getStartingHealth() {
        return 10;
    }

}
