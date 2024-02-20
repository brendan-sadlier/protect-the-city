package Player;

import util.GameObject;
import util.Point3f;
import util.Vector3f;

public class Player extends GameObject {

    public Player(String texturePath, Point3f startingPos) {
        super(texturePath, 50, 50, startingPos);
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
