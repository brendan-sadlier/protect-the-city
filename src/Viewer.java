import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


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
 
 * Credits: Kelly Charles (2020)
 */ 
public class Viewer extends JPanel {
	private long CurrentAnimationTime= 0;
	private boolean roundFailed = false;
	
	Model gameworld = new Model();
	 
	public Viewer(Model World) {
		this.gameworld=World;
		// TODO Auto-generated constructor stub
	}

	public Viewer(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public Viewer(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public Viewer(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public void updateview() {
		
		this.repaint();
		// TODO Auto-generated method stub
		
	}
	
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		CurrentAnimationTime++; // runs animation time step


		//Draw Player One Game Object
		int playerOneX = (int) gameworld.getPlayerOne().getCentre().getX();
		int playerOneY = (int) gameworld.getPlayerOne().getCentre().getY();
		int playerOneWidth = (int) gameworld.getPlayerOne().getWidth();
		int playerOneHeight = (int) gameworld.getPlayerOne().getHeight();
		String playerOneTexture = gameworld.getPlayerOne().getTexture();

		//Draw Player Two Game Object
		int playerTwoX = (int) gameworld.getPlayerTwo().getCentre().getX();
		int playerTwoY = (int) gameworld.getPlayerTwo().getCentre().getY();
		int playerTwoWidth = (int) gameworld.getPlayerTwo().getWidth();
		int playerTwoHeight = (int) gameworld.getPlayerTwo().getHeight();
		String playerTwoTexture = gameworld.getPlayerTwo().getTexture();


		//Draw background
		drawBackground(g);

		//Draw player one
		drawPlayer(playerOneX, playerOneY, playerOneWidth, playerOneHeight, playerOneTexture, g);

		//Draw player two
		drawPlayer(playerTwoX, playerTwoY, playerTwoWidth, playerTwoHeight, playerTwoTexture, g);

		//Draw Bullets
		// change back
		gameworld.getBullets().forEach((temp) -> {
			drawBullet((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(),g);
		});

		gameworld.getExplosions().forEach((temp) -> {
			drawExplosion((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(),g);
		});


		//Draw Enemies
		gameworld.getNormalEnemies().forEach((temp) -> {
			drawEnemies((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(), g);
		});

		gameworld.getHeavyEnemies().forEach((temp) -> {
			drawEnemies((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(), g);
		});


		gameworld.getAmmoCrates().forEach((temp) -> {
			drawAmmoCrate((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(), g);

		});


		gameworld.getHealthCrates().forEach((temp) -> {
			drawHealthCrate((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(),g);

		});

		// Show Bullet Count
		showBulletCount(g);
		showShieldCount(g);

	}

	private void drawEnemies (int x, int y, int width, int height, String texture, Graphics g) {

		File TextureToLoad = new File(texture);  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE

		try {
			int currentPositionInAnimation= ((int) (CurrentAnimationTime%4)*320); //slows down animation so every 10 frames we get another frame so every 100ms
			Image myImage = ImageIO.read(TextureToLoad);
			g.drawImage(myImage, x,y, x+width, y+height, currentPositionInAnimation  , 0, currentPositionInAnimation+319, 320, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void showBulletCount(Graphics g) {

		int padding = 0;

		g.setColor(Color.WHITE);
		g.fillRect(10, 10, 215, 25);

		File TextureToLoad = new File("res/bullet_1_icon.png");

		for (int i = gameworld.getBulletCount(); i > 0; i--) {

			int bulletWidth = 14;
			int bulletHeight = 17;

//			g.setColor(Color.BLUE);
//			g.fillRect(20 + padding, 15, 15, 20);

			try {
				Image myImage = ImageIO.read(TextureToLoad);
				g.drawImage(myImage, 20 + padding, 15, 20 + padding + bulletWidth, 15 + bulletHeight, 0 , 0, myImage.getWidth(null), myImage.getHeight(null), null);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			padding += bulletWidth + 6;
		}
	}

	private void showShieldCount (Graphics g) {

		int padding = 0;

		g.setColor(Color.WHITE);
		g.fillRect(10, 50, 215, 25);

		File TextureToLoad = new File("res/shield_icon.png");

		for (int i = gameworld.getHealth(); i > 0; i--) {

			int shieldWidth = 14;
			int shieldHeight = 17;

//			g.setColor(Color.BLUE);
//			g.fillRect(20 + padding, 15, 15, 20);

			try {
				Image myImage = ImageIO.read(TextureToLoad);
				g.drawImage(myImage, 20 + padding, 55, 20 + padding + shieldWidth, 55 + shieldHeight, 0, 0, myImage.getWidth(null), myImage.getHeight(null), null);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			padding += shieldWidth + 6;
		}
	}

	private void drawBackground(Graphics g)
	{
//		File TextureToLoad = new File("res/gui/backgrounds/BG_MainMenu.png");  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
		File TextureToLoad = new File(gameworld.getLevel().getBackground());
		try {
			Image myImage = ImageIO.read(TextureToLoad); 
			 g.drawImage(myImage, 0,0, 1200, 700, 0 , 0, 1200, 700, null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void drawBullet(int x, int y, int width, int height, String texture,Graphics g)
	{
		File TextureToLoad = new File(texture);  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE 
		try {
			Image myImage = ImageIO.read(TextureToLoad); 
			//64 by 128 
			 g.drawImage(myImage, x,y, x+width, y+height, 0 , 0, 149, 199, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void drawAmmoCrate(int x, int y, int width, int height, String texture,Graphics g)
	{
		File TextureToLoad = new File(texture);  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
		try {
			Image myImage = ImageIO.read(TextureToLoad);
			//64 by 128
			g.drawImage(myImage, x,y, x+width, y+height, 0 , 0, 50, 50, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void drawHealthCrate(int x, int y, int width, int height, String texture,Graphics g)
	{
		File TextureToLoad = new File(texture);  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
		try {
			Image myImage = ImageIO.read(TextureToLoad);
			//64 by 128
			g.drawImage(myImage, x,y, x+width, y+height, 0 , 0, 999, 999, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void drawExplosion (int x, int y, int width, int height, String texture,Graphics g) {
		File TextureToLoad = new File(texture);  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
		try {
			Image myImage = ImageIO.read(TextureToLoad);
			//The spirte is 32x32 pixel wide and 4 of them are placed together so we need to grab a different one each time
			//remember your training :-) computer science everything starts at 0 so 32 pixels gets us to 31
			int currentPositionInAnimation= ((int) ((CurrentAnimationTime%10)))*320; //slows down animation so every 10 frames we get another frame so every 100ms
			g.drawImage(myImage, x,y, x+width, y+height, currentPositionInAnimation  , 0, currentPositionInAnimation+319, 320, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void drawPlayer(int x, int y, int width, int height, String texture,Graphics g) {
		File TextureToLoad = new File(texture);  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
		try {
			Image myImage = ImageIO.read(TextureToLoad);
			//The spirte is 32x32 pixel wide and 4 of them are placed together so we need to grab a different one each time
			//remember your training :-) computer science everything starts at 0 so 32 pixels gets us to 31
			int currentPositionInAnimation= ((int) ((CurrentAnimationTime%4)/2))*320; //slows down animation so every 10 frames we get another frame so every 100ms
			g.drawImage(myImage, x,y, x+width, y+height, currentPositionInAnimation  , 0, currentPositionInAnimation+329, 320, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
