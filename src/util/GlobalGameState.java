package util;

public class GlobalGameState {

    private static GlobalGameState instance;
    private int currentLevel = 1;

    private GlobalGameState() {}

    public static GlobalGameState getInstance() {
        if (instance == null) {
            instance = new GlobalGameState();
        }
        return instance;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public void nextLevel() {
        this.currentLevel++;
    }
}
