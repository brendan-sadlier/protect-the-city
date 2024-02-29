package Levels;

import Enemies.Enemy;
import Enemies.HeavyEnemy;
import Enemies.NormalEnemy;
import util.Point3f;

import java.util.concurrent.CopyOnWriteArrayList;

public class LevelTwo extends Level implements Levels {

    private final int NUMBER_OF_NORMAL_ENEMIES = 6;
    private final int NUMBER_OF_HEAVY_ENEMIES = 3;
    private final int NUMBER_OF_AMMO_CRATES = 3;
    private final int NUMBER_OF_HEALTH_CRATES = 2;


    public LevelTwo() {

        super("res/gui/backgrounds/BG_LevelTwo.png");

        setNormalEnemies();
        setHeavyEnemies();
    }

    private void setNormalEnemies() {

        normalEnemies = new CopyOnWriteArrayList<>();

        for (int i = 0; i < NUMBER_OF_NORMAL_ENEMIES; i++) {
            normalEnemies.add(new NormalEnemy(new Point3f(((float)Math.random()*1000), 0,0)));
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

    public int getStartingNumberOfHealthCrates() {
        return NUMBER_OF_HEALTH_CRATES;
    }

    public int getStartingNumberOfAmmoCrates() {
        return NUMBER_OF_AMMO_CRATES;
    }

    public int getStartingHealth() {
        return 10;
    }

}
