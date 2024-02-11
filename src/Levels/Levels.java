package Levels;

import util.Enemy;
import util.GameObject;
import util.HeavyEnemy;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Levels {

    CopyOnWriteArrayList<Enemy> getEnemyList();
    CopyOnWriteArrayList<HeavyEnemy> getHeavyEnemyList();
    CopyOnWriteArrayList<GameObject> getAmmoList();

    String getBackground();
    int getNumOfNormalEnemies();

    int getNumOfHeavyEnemies();

    int getSTART_HEALTH();

    int getSTART_NUMBER_OF_BULLETS();

    boolean isLevelComplete();
}
