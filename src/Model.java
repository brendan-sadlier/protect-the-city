import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

import util.*;

import Levels.Levels;
import Levels.LevelOne;
import Levels.LevelTwo;

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
	private final int LAST_LEVEL = 2;
	private int level;
	private Levels Level;

	private int NUMBER_OF_NORMAL_ENEMIES;
	private int NUMBER_OF_HEAVY_ENEMIES;
	private int NUMBER_OF_AMMO_CRATES;

	private boolean roundComplete = false;
	private boolean gameComplete = false;

	public Model() {

		level = 1;
		gameComplete = false;

		setupLevel();
	}

	public void setupLevel () {

		roundComplete = false;

		playerOne = new PlayerOne();
		playerTwo = new PlayerTwo();

        if (level == 2) {
            Level = new LevelTwo();
        } else {
            Level = new LevelOne();
        }

		normalEnemies = Level.getEnemyList();
		heavyEnemies = Level.getHeavyEnemyList();
		NUMBER_OF_AMMO_CRATES = 1;
		NUMBER_OF_NORMAL_ENEMIES = Level.getNumOfNormalEnemies();
		NUMBER_OF_HEAVY_ENEMIES = Level.getNumOfHeavyEnemies();
		bulletCount = Level.getSTART_NUMBER_OF_BULLETS();
		health = Level.getSTART_HEALTH();


	}

	public void nextLevel() {
		level++;
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

//	public void restartGame() {
//		level = 1;
//		setupLevel();
//	}

//	public boolean getRoundFailed () {
//		return roundFailed;
//	}

	private void checkGameOver() {
		if (getLevel().isLevelComplete() && level != LAST_LEVEL) {
			roundComplete = true;
		} else if (getLevel().isLevelComplete() && level == LAST_LEVEL) {
			roundComplete = false;
			gameComplete = true;
		}
	}

	private void gameLogic() {
		
		
		// this is a way to increment across the array list data structure 

		
		//see if they hit anything 
		// using enhanced for-loop style as it makes it alot easier both code wise and reading wise too

		// Normal Enemies
		for (Enemy temp : normalEnemies) {

			if (!temp.isHit()) {

				for (GameObject Bullet : BulletList) {
					if (Math.abs(temp.getCentre().getX() - Bullet.getCentre().getX()) < temp.getWidth()
							&& Math.abs(temp.getCentre().getY() - Bullet.getCentre().getY()) < temp.getHeight()) {

						GameObject tempExplosion = createExplosion(new Point3f(temp.getCentre().getX(), temp.getCentre().getY(), 0));
						explosionList.add(tempExplosion);
						sound.playSound("res/sounds/explosion.wav");

						normalEnemies.remove(temp);
						BulletList.remove(Bullet);

						Timer timer = new Timer();
						timer.schedule(new java.util.TimerTask() {
							@Override
							public void run() {
								explosionList.remove(tempExplosion);
							}
						}, 500);
					}
				}
			}
		}

		// Heavy Enemies
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
						}, 500);
					}
				}
			}
		}

		// AmmoCrate
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

		// HealthCrate
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
			 
			 
			//see if they get to the top of the screen ( remember 0 is the top 
			if (temp.getCentre().getY()==600.0f)  // current boundary need to pass value to model
			{
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

		// Heavy Enemies
		for (HeavyEnemy heavyEnemy : heavyEnemies) {

			// Move enemies
			heavyEnemy.getCentre().ApplyVector(new Vector3f(0,-1.0f,0));


			//see if they get to the top of the screen ( remember 0 is the top
			if (heavyEnemy.getCentre().getY()==600.0f)  // current boundary need to pass value to model
			{
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
	}

	private void bulletLogic() {
		// TODO Auto-generated method stub
		// move bullets

	  
		for (GameObject temp : BulletList) 
		{
		    //check to move them
			  
			temp.getCentre().ApplyVector(new Vector3f(0,1,0));
			//see if they hit anything 
			
			//see if they get to the top of the screen ( remember 0 is the top 
			if (temp.getCentre().getY()==0)
			{
			 	BulletList.remove(temp);
			} 
		} 
		
	}

	private void AmmoCrateLogic() {
		// TODO Auto-generated method stub

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

		if (ammoList.isEmpty() && NUMBER_OF_AMMO_CRATES > 0 && Level.getEnemyList().size() <= NUMBER_OF_NORMAL_ENEMIES / 2) {
			CreateAmmoCrate();
		}
	}

	// Health Crate Logic
	private void HealthCrateLogic() {
		// TODO Auto-generated method stub

		for (GameObject temp : healthCrateList) {
			// Move enemies

			temp.getCentre().ApplyVector(new Vector3f(0,-1,0));


			//see if they get to the top of the screen ( remember 0 is the top
			if (temp.getCentre().getY()==600.0f) {  // current boundary need to pass value to model
				healthCrateList.remove(temp);
			}
		}
	}

	private void playerLogic() {

		// Player 1
		if(Controller.getInstance().isKeyAPressed()) {
			playerOne.moveLeft();
		}
		
		if(Controller.getInstance().isKeyDPressed()) {
			playerOne.moveRight();
		}
		
		if(Controller.getInstance().isKeySpacePressed()) {
			fireBullet();
			Controller.getInstance().setKeySpacePressed(false);
		}

		// Player 2
		if(Controller.getInstance().isKeyLeftPressed()) {
			playerTwo.moveLeft();
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
		return new GameObject("res/explosion_strip.png", 50, 50, point);
	}

	private void CreateAmmoCrate() {
		ammoList.add(new GameObject("res/ammo_crate.png",50,50,new Point3f(((float)Math.random()*1200), 0,0)));
	}

	private void CreateHealthCrate() {
		healthCrateList.add(new GameObject("res/health_crate.png",50,50,new Point3f(((float)Math.random()*1200), 0,0)));
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

	public int gameState() {

		// Game Complete
		if (gameComplete) {
			return -2;
		} else if (roundComplete) { // Round Complete
			return 1;
		} else {
			return 0;
		}
	}

}

