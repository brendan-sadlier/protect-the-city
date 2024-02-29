package Levels;

import Enemies.Enemy;
import Enemies.HeavyEnemy;
import Enemies.NormalEnemy;
import util.Point3f;

import java.util.concurrent.CopyOnWriteArrayList;

public class LevelOne extends Level implements Levels {

    private final int NUMBER_OF_NORMAL_ENEMIES = 4;
    private final int NUMBER_OF_HEAVY_ENEMIES = 1;
    private final int START_NUMBER_OF_BULLETS = 3;
    private final int START_HEALTH = 4;
    private final int START_NUMBER_OF_HEALTH_CRATES = 1;
    private final int START_NUMBER_OF_AMMO_CRATES = 1;

    public LevelOne() {

        super("res/gui/backgrounds/BG_LevelOne.png");

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

    @Override
    public int getStartingNumberOfHealthCrates() {
        return START_NUMBER_OF_HEALTH_CRATES;
    }

    public int getStartingNumberOfAmmoCrates() {
        return START_NUMBER_OF_AMMO_CRATES;
    }

    public int getStartingHealth() {
        return START_HEALTH;
    }
}
