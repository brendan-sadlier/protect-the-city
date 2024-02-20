package Levels;

import util.Enemy;
import util.HeavyEnemy;
import util.Point3f;

import java.util.concurrent.CopyOnWriteArrayList;

public class LevelThree extends Level implements Levels {

    public LevelThree () {
        super("res/gui/backgrounds/BG_LevelThree.png");
        setNormalEnemies();
        setHeavyEnemies();
    }

    private void setNormalEnemies() {

        normalEnemies = new CopyOnWriteArrayList<>();

        int NUMBER_OF_NORMAL_ENEMIES = 20;
        for (int i = 0; i < NUMBER_OF_NORMAL_ENEMIES; i++) {
            normalEnemies.add(new Enemy(new Point3f(((float)Math.random()*1000), 0,0)));
        }
    }

    private void setHeavyEnemies() {
        heavyEnemies = new CopyOnWriteArrayList<>();
        int NUMBER_OF_HEAVY_ENEMIES = 15;
        for (int i = 0; i < NUMBER_OF_HEAVY_ENEMIES; i++) {
            heavyEnemies.add(new HeavyEnemy(new Point3f(((float)Math.random()*1000), 0,0)));
        }
    }

    public int getNumOfNormalEnemies() {
        return 20;
    }

    public int getNumOfHeavyEnemies() {
        return 15;
    }

    public int getStartingHealth() {
        return 10;
    }

    public int getStatingNumOfBullets() {
        return 10;
    }
}
