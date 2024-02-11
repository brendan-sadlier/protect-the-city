package util;

public class PlayerTwo extends GameObject {
    private int health;
    private int bulletCount;

    public PlayerTwo () {
        super("res/tank_multi.png",50,50,new Point3f(600,590,0));
        this.health = 10;
        this.bulletCount = 10;
    }

    public void moveLeft() {
        this.getCentre().ApplyVector( new Vector3f(-2,0,0));
    }

    public void moveRight() {
        this.getCentre().ApplyVector( new Vector3f(2,0,0));
    }

    public GameObject CreateBullet() {
        return new GameObject("res/bullet_1.png",7,15,new Point3f(this.getCentre().getX() + 22, this.getCentre().getY() - 12,0.0f));
    }
}
