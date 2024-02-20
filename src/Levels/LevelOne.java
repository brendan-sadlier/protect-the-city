package Levels;

import util.Enemy;
import util.HeavyEnemy;
import util.Point3f;

import java.util.concurrent.CopyOnWriteArrayList;

public class LevelOne extends Level implements Levels {

    private final int NUMBER_OF_NORMAL_ENEMIES = 4;
    private final int NUMBER_OF_HEAVY_ENEMIES = 1;
    private final int START_NUMBER_OF_BULLETS = 3;
    private final int START_HEALTH = 4;

    public LevelOne() {

        super("res/gui/backgrounds/BG_LevelOne.png");

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
        heavyEnemies.add(new HeavyEnemy(new Point3f(((float)Math.random()*1000), 0,0)));
    }

    public int getNumOfNormalEnemies() {
        return NUMBER_OF_NORMAL_ENEMIES;
    }

    public int getNumOfHeavyEnemies() {
        return NUMBER_OF_HEAVY_ENEMIES;
    }

    public int getStatingNumOfBullets() {
        return START_NUMBER_OF_BULLETS;
    }

    public int getStartingHealth() {
        return START_HEALTH;
    }
}
