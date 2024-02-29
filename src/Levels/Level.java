package Levels;

import Enemies.Enemy;
import util.GameObject;
import Enemies.HeavyEnemy;

import java.util.concurrent.CopyOnWriteArrayList;

public class Level {

    private String background;

    public CopyOnWriteArrayList<Enemy> normalEnemies;
    public CopyOnWriteArrayList<HeavyEnemy> heavyEnemies;
    public CopyOnWriteArrayList<GameObject> ammoList;


    public Level () {}

    public Level (String background) {
        this.background = background;
    }

    public String getBackground() {
        return background;
    }

    public CopyOnWriteArrayList<Enemy> getEnemyList() {
        return normalEnemies;
    }
    public CopyOnWriteArrayList<HeavyEnemy> getHeavyEnemyList() {
        return heavyEnemies;
    }

    public boolean isLevelComplete() {
        return normalEnemies.isEmpty() && heavyEnemies.isEmpty();
    }

}
