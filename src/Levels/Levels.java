package Levels;

import util.Enemy;
import util.GameObject;
import util.HeavyEnemy;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Levels {

    CopyOnWriteArrayList<Enemy> getEnemyList();
    CopyOnWriteArrayList<HeavyEnemy> getHeavyEnemyList();

    String getBackground();
    int getNumOfNormalEnemies();

    int getNumOfHeavyEnemies();

    int getStartingHealth();

    int getStatingNumOfBullets();

    int getStartingNumberOfHealthCrates();
    int getStartingNumberOfAmmoCrates();

    boolean isLevelComplete();
}
