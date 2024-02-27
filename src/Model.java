import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

import Player.PlayerOne;
import Player.PlayerTwo;
import util.*;

import Levels.Levels;
import Levels.LevelOne;
import Levels.LevelTwo;
import Levels.LevelThree;

/*
 * Created by Abraham Campbell on 15/01/2020.
 *   Copyright (c) 2020  Abraham Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
   
   (MIT LICENSE ) e.g do what you want with this :-) 
 */ 
public class Model {


	private PlayerOne playerOne;
	private PlayerTwo playerTwo;
	private final SoundEffect sound = new SoundEffect();
	private CopyOnWriteArrayList<Enemy> normalEnemies = new CopyOnWriteArrayList<Enemy>();
	private CopyOnWriteArrayList<HeavyEnemy> heavyEnemies = new CopyOnWriteArrayList<HeavyEnemy>();
	private Controller controller = Controller.getInstance();
	private final CopyOnWriteArrayList<GameObject> BulletList  = new CopyOnWriteArrayList<GameObject>();

	private CopyOnWriteArrayList<GameObject> ammoList = new CopyOnWriteArrayList<GameObject>();
	private final CopyOnWriteArrayList<GameObject> healthCrateList  = new CopyOnWriteArrayList<GameObject>();
	private final CopyOnWriteArrayList<GameObject> explosionList  = new CopyOnWriteArrayList<GameObject>();
	private int bulletCount;
	private int health;
	private final int LAST_LEVEL = 3;
	private Levels Level;

	private int NUMBER_OF_NORMAL_ENEMIES;
	private int NUMBER_OF_HEAVY_ENEMIES;
	private int NUMBER_OF_AMMO_CRATES;
	private int NUMBER_OF_HEALTH_CRATES;



	private boolean roundComplete = false;
	private boolean gameComplete = false;
	private boolean roundFailed = false;

	public Model() {
		setupLevel();
	}

	public void setupLevel () {

		roundComplete = false;
		roundFailed = false;
		gameComplete = false;

		int level = GlobalGameState.getInstance().getCurrentLevel();
		Controller.getInstance().resetController();

		playerOne = new PlayerOne();
		playerTwo = new PlayerTwo();

		if (level == 1) {
			Level = new LevelOne();
		} else if (level == 2) {
			Level = new LevelTwo();
		} else {
			Level = new LevelThree();
		}

		NUMBER_OF_AMMO_CRATES = Level.getStartingNumberOfAmmoCrates();
		NUMBER_OF_HEALTH_CRATES = Level.getStartingNumberOfHealthCrates();
		NUMBER_OF_NORMAL_ENEMIES = Level.getNumOfNormalEnemies();
		NUMBER_OF_HEAVY_ENEMIES = Level.getNumOfHeavyEnemies();
		bulletCount = Level.getStatingNumOfBullets();
		health = Level.getStartingHealth();

		System.out.println("Level: " + level);
	}

	public void nextLevel() {
		GlobalGameState.getInstance().nextLevel();
		setupLevel();
	}

	public void resetLevel() {
		GlobalGameState.getInstance().setCurrentLevel(1);
		setupLevel();
	}
	
	// This is the heart of the game , where the model takes in all the inputs ,decides the outcomes and then changes the model accordingly. 
	public void gamelogic() 
	{
		// Player Logic first 
		playerLogic();
		// Enemy Logic next
		enemyLogic();
		// Bullets move next 
		bulletLogic();
		// interactions between objects 
		gameLogic();
		// AmmoCrate Logic
		AmmoCrateLogic();
		// HealthCrate Logic
		HealthCrateLogic();
		// Round End Logic
		checkGameOver();
	   
	}

	private void checkGameOver() {

		int level = GlobalGameState.getInstance().getCurrentLevel();

		boolean noActiveEnemies = normalEnemies.isEmpty() && heavyEnemies.isEmpty();

		if (noActiveEnemies && level != LAST_LEVEL) {
			roundComplete = true;
		} else if (noActiveEnemies && GlobalGameState.getInstance().getCurrentLevel() == LAST_LEVEL) {
			gameComplete = true;
		} else if (health <= 0 || bulletCount <= 0) {
			roundFailed = true;
		}
	}

	// This is a way to get the state of the game to the view
	public int gameState() {

		// Game Complete
		if (gameComplete) {
			return 1;
		} else if (roundComplete) {
			return 2;
		} else if (roundFailed){
			return 3;
		} else {
			return 0;
		}
	}

	private boolean isPlayerColliding(PlayerOne playerOne, PlayerTwo playerTwo) {
		float player1RightEdge = playerOne.getCentre().getX() + playerOne.getWidth();
		float player2LeftEdge = playerTwo.getCentre().getX();
		float player2RightEdge = playerTwo.getCentre().getX() + playerTwo.getWidth();
		float player1LeftEdge = playerOne.getCentre().getX();

		return !(player1RightEdge > player2LeftEdge) || !(player1LeftEdge < player2RightEdge);
	}

	private void gameLogic() {

		// this is a way to increment across the array list data structure
		//see if they hit anything 
		// using enhanced for-loop style as it makes it alot easier both code wise and reading wise too

		// Region: Normal Enemies
		for (Enemy temp : normalEnemies) {

			if (!temp.isHit()) {

				for (GameObject Bullet : BulletList) {
					if (Math.abs(temp.getCentre().getX() - Bullet.getCentre().getX()) < temp.getWidth()
							&& Math.abs(temp.getCentre().getY() - Bullet.getCentre().getY()) < temp.getHeight()) {

						GameObject tempExplosion = createExplosion(new Point3f(temp.getCentre().getX(), temp.getCentre().getY(), 0));
						explosionList.add(tempExplosion);
						sound.playSound("res/sounds/explosion.wav");

						BulletList.remove(Bullet);

						Timer timer = new Timer();
						timer.schedule(new java.util.TimerTask() {
							@Override
							public void run() {
								explosionList.remove(tempExplosion);
							}
						}, 1000);

						normalEnemies.remove(temp);
					}
				}
			}
		}

		// Region: Heavy Enemies
		for (HeavyEnemy heavyEnemy : heavyEnemies) {

			if (!heavyEnemy.isHit()) {

				for (GameObject Bullet : BulletList) {
					if (Math.abs(heavyEnemy.getCentre().getX() - Bullet.getCentre().getX()) < heavyEnemy.getWidth()
							&& Math.abs(heavyEnemy.getCentre().getY() - Bullet.getCentre().getY()) < heavyEnemy.getHeight()) {

						GameObject tempExplosion = createExplosion(new Point3f(heavyEnemy.getCentre().getX(), heavyEnemy.getCentre().getY(), 0));
						explosionList.add(tempExplosion);
						sound.playSound("res/sounds/explosion.wav");

						heavyEnemies.remove(heavyEnemy);
						BulletList.remove(Bullet);

						Timer timer = new Timer();
						timer.schedule(new java.util.TimerTask() {
							@Override
							public void run() {
								explosionList.remove(tempExplosion);
							}
						}, 1000);
					}
				}
			}
		}

		// Region: AmmoCrate
		for (GameObject temp : ammoList)
		{
			for (GameObject Bullet : BulletList)
			{
				if ( Math.abs(temp.getCentre().getX()- Bullet.getCentre().getX())< temp.getWidth()
						&& Math.abs(temp.getCentre().getY()- Bullet.getCentre().getY()) < temp.getHeight())
				{
					ammoList.remove(temp);
					NUMBER_OF_AMMO_CRATES -= 1;
					sound.playSound("res/sounds/reload.wav");
					BulletList.remove(Bullet);
					replenishBullets();
				}
			}


		}

		// Region: HealthCrate
		for (GameObject temp : healthCrateList)
		{
			for (GameObject Bullet : BulletList)
			{
				if ( Math.abs(temp.getCentre().getX()- Bullet.getCentre().getX())< temp.getWidth()
						&& Math.abs(temp.getCentre().getY()- Bullet.getCentre().getY()) < temp.getHeight())
				{
					healthCrateList.remove(temp);
					sound.playSound("res/sounds/repair.wav");
					BulletList.remove(Bullet);
					replenishHealth();
				}
			}
		}

		
	}

	private void enemyLogic() {

		// Normal Enemies
		for (Enemy temp : normalEnemies) {
		    // Move enemies
			temp.getCentre().ApplyVector(new Vector3f(0,-0.5f,0));
			 
			 
			//see if they get to the top of the screen ( remember 0 is the top )
			if (temp.getCentre().getY()==600.0f) { // current boundary need to pass value to model
				normalEnemies.remove(temp);

				GameObject tempExplosion = createExplosion(new Point3f(temp.getCentre().getX(), temp.getCentre().getY(), 0));
				explosionList.add(tempExplosion);
				sound.playSound("res/sounds/explosion.wav");

				Timer timer = new Timer();
				timer.schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						explosionList.remove(tempExplosion);
					}
				}, 1000);
				
				// enemies win so score decreases
				takeDamage(temp.getDamage());
			} 
		}

		if (Math.random() < 0.5 && NUMBER_OF_NORMAL_ENEMIES > 0 && normalEnemies.size() <= 2) {
			normalEnemies.add(new Enemy(new Point3f(((float)Math.random()*1000), 0,0)));
			NUMBER_OF_NORMAL_ENEMIES--;
		}

		// Heavy Enemies
		for (HeavyEnemy heavyEnemy : heavyEnemies) {

			heavyEnemy.getCentre().ApplyVector(new Vector3f(0,-1.0f,0));

			if (heavyEnemy.getCentre().getY()==600.0f) {
				heavyEnemies.remove(heavyEnemy);

				GameObject tempExplosion = createExplosion(new Point3f(heavyEnemy.getCentre().getX(), heavyEnemy.getCentre().getY(), 0));
				explosionList.add(tempExplosion);
				sound.playSound("res/sounds/explosion.wav");

				Timer timer = new Timer();
				timer.schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						explosionList.remove(tempExplosion);
					}
				}, 1000);

				// enemies win so score decreases
				takeDamage(heavyEnemy.getDamage());
			}
		}

		if (normalEnemies.size() <= NUMBER_OF_HEAVY_ENEMIES / 2 && NUMBER_OF_HEAVY_ENEMIES > 0 && heavyEnemies.size() < 3) {
			heavyEnemies.add(new HeavyEnemy(new Point3f(((float)Math.random()*1000), 0,0)));
			NUMBER_OF_HEAVY_ENEMIES--;
		}
	}

	private void bulletLogic() {

		// move bullets
		for (GameObject temp : BulletList) {
		    //check to move them
			  
			temp.getCentre().ApplyVector(new Vector3f(0,1,0));
			//see if they hit anything 
			
			//see if they get to the top of the screen ( remember 0 is the top 
			if (temp.getCentre().getY()==0) {
			 	BulletList.remove(temp);
			} 
		}
	}

	private void AmmoCrateLogic() {

		for (GameObject temp : ammoList)
		{
			// Move enemies

			temp.getCentre().ApplyVector(new Vector3f(0,-1,0));


			//see if they get to the top of the screen ( remember 0 is the top
			if (temp.getCentre().getY()==600.0f)  // current boundary need to pass value to model
			{
				ammoList.remove(temp);
			}
		}

		if (bulletCount < 4 && NUMBER_OF_AMMO_CRATES > 0 && ammoList.isEmpty() && Math.random() < 0.1) {
			CreateAmmoCrate();
			NUMBER_OF_AMMO_CRATES--;
		}
	}

	// Health Crate Logic
	private void HealthCrateLogic() {

		for (GameObject temp : healthCrateList) {
			// Move enemies

			temp.getCentre().ApplyVector(new Vector3f(0,-1,0));


			//see if they get to the top of the screen ( remember 0 is the top
			if (temp.getCentre().getY()==600.0f) {  // current boundary need to pass value to model
				healthCrateList.remove(temp);
			}
		}

		if (health < 4 && NUMBER_OF_HEALTH_CRATES > 0 && healthCrateList.isEmpty() && Math.random() < 0.1) {
			CreateHealthCrate();
			NUMBER_OF_HEALTH_CRATES--;
		}
	}

	private void playerLogic() {

		// Region: Player 1
		if(Controller.getInstance().isKeyAPressed()) {
			playerOne.moveLeft();
		}

		if (isPlayerColliding(playerOne, playerTwo)) {
			if(Controller.getInstance().isKeyDPressed()) {
				playerOne.moveRight();
			}
		}

		if(Controller.getInstance().isKeySpacePressed()) {
			fireBullet();
			Controller.getInstance().setKeySpacePressed(false);
		}

		// Region: Player 2
		if (isPlayerColliding(playerOne, playerTwo)) {
			if(Controller.getInstance().isKeyLeftPressed()) {
				playerTwo.moveLeft();
			}
		}

		if(Controller.getInstance().isKeyRightPressed()) {
			playerTwo.moveRight();
		}

		if(Controller.getInstance().isKeyUpPressed()) {
			fireBulletPlayerTwo();
			Controller.getInstance().setKeyUpPressed(false);
		}
	}

	private void CreateBullet() {
		BulletList.add(playerOne.CreateBullet());
	}

	private void playerTwo_CreateBullet() {
		BulletList.add(playerTwo.CreateBullet());
	}

	private GameObject createExplosion (Point3f point) {
		return new GameObject("res/sprites/ExplosionStrip.png", 50, 50, point);
	}

	private void CreateAmmoCrate() {
		ammoList.add(new GameObject("res/sprites/AmmoCrate.png",50,50,new Point3f(((float)Math.random()*1200), 0,0)));
	}

	private void CreateHealthCrate() {
		healthCrateList.add(new GameObject("res/sprites/ShieldCrate.png",50,50,new Point3f(((float)Math.random()*1200), 0,0)));
	}

	public void fireBullet () {
		if (bulletCount > 0) {
			CreateBullet();
			sound.playSound("res/sounds/gun_shot.wav");
			bulletCount--;
		}

		if (bulletCount == 0) {
			sound.playSound("res/sounds/empty_gun.wav");
		}
	}

	public void fireBulletPlayerTwo () {
		if (getBulletCount() > 0) {
			playerTwo_CreateBullet();
			sound.playSound("res/sounds/gun_shot.wav");
			bulletCount--;
		}

		if (getBulletCount() == 0) {
			sound.playSound("res/sounds/empty_gun.wav");
		}
	}

	public int getBulletCount() {
		return bulletCount;
	}

	public void takeDamage(int damage) {
		health -= damage;
	}

	public int getHealth () {
		return health;
	}

	private void replenishBullets() {
		bulletCount = 10;
	}

	private void replenishHealth() {
		health = 10;
	}

	public GameObject getPlayerOne() {
		return playerOne;
	}

	public GameObject getPlayerTwo() {
		return playerTwo;
	}

	public Levels getLevel() {
		return Level;
	}

	public CopyOnWriteArrayList<Enemy> getNormalEnemies() {
		return normalEnemies;
	}

	public CopyOnWriteArrayList<HeavyEnemy> getHeavyEnemies() {
		return heavyEnemies;
	}

	public CopyOnWriteArrayList<GameObject> getBullets() {
		return BulletList;
	}

	public CopyOnWriteArrayList<GameObject> getAmmoCrates() {
		return ammoList;
	}

	public CopyOnWriteArrayList<GameObject> getHealthCrates() {
		return healthCrateList;
	}

	public CopyOnWriteArrayList<GameObject> getExplosions() {
		return explosionList;
	}
}

