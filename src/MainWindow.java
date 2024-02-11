import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

import util.UnitTests;

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



public class MainWindow {

	// Final Values
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 700;
	private static final int TARGET_FPS = 100;

	// JFrame Components
	private static final JFrame frame = new JFrame("Protect the City"); // Change to the name of your game
	private static Viewer gamecanvas;
	private static JLabel BackgroundImageForStartMenu;
	private static JLabel Title;
	private static Clip menuMusic;
	private static JButton playButton;
	private static JButton nextRound;
	private static JButton exitButton;
	private static JButton menuButton;
	// Game Components
	private static Model gameworld;
	private static int gameState = -3;
	private static final KeyListener Controller = new Controller();
	  
	public MainWindow() {

		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);

		File Background = new File("res/gui/backgrounds/BG_MainMenu.png");
		File title = new File("res/gui/titles/Title_Game.png");
		File play = new File("res/gui/buttons/Button_Start.png");
		File exit = new File("res/gui/buttons/Button_Exit.png");
		File menu = new File("res/sounds/menu_music.wav");

		// Add Play Button to Frame
		try {
			BufferedImage playImage = ImageIO.read(play);
			Image img = playImage.getScaledInstance(250, 100, Image.SCALE_SMOOTH);

			playButton = new JButton(new ImageIcon(img));
			playButton.setBorder(BorderFactory.createEmptyBorder());
			playButton.setContentAreaFilled(false);
			playButton.setBounds(475, 350, 250, 100);
			frame.add(playButton);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add Exit Button to Frame
		try {
			BufferedImage exitImage = ImageIO.read(exit);
			Image img = exitImage.getScaledInstance(125, 50, Image.SCALE_SMOOTH);

			exitButton = new JButton(new ImageIcon(img));
			exitButton.setBorder(BorderFactory.createEmptyBorder());
			exitButton.setContentAreaFilled(false);
			exitButton.setBounds(475, 550, 125, 50);
			frame.add(exitButton);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add Title to Frame
		try {
			BufferedImage titleImage = ImageIO.read(title);
			Image img = titleImage.getScaledInstance(900, 225, Image.SCALE_SMOOTH);

			Title = new JLabel(new ImageIcon(img));
			Title.setBounds(150, 100, 900, 225);
			frame.add(Title);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add Background to Frame
		try {
			BufferedImage backgroundImage = ImageIO.read(Background);
			BackgroundImageForStartMenu = new JLabel(new ImageIcon(backgroundImage));
			BackgroundImageForStartMenu.setBounds(0, 0, WIDTH, HEIGHT);
			frame.add(BackgroundImageForStartMenu);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add Menu Music to Frame
		try {
			AudioInputStream menuAudio = AudioSystem.getAudioInputStream(menu);
			DataLine.Info mInfo = new DataLine.Info(Clip.class, menuAudio.getFormat());
			menuMusic = (Clip) AudioSystem.getLine(mInfo);
			menuMusic.open(menuAudio);
			FloatControl menuControl = (FloatControl) menuMusic.getControl(FloatControl.Type.MASTER_GAIN);
			menuControl.setValue(0f);
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}

		// Add Play Button Action Listener
		playButton.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playButton.setVisible(false);
				Title.setVisible(false);
				BackgroundImageForStartMenu.setVisible(false);

				startGame();
			}
		}));

		frame.setVisible(true);
		menuMusic.setMicrosecondPosition(0);
		menuMusic.start();
	}

	public static void main(String[] args) {
		MainWindow window = new MainWindow();

		while (true) {

			int TimeBetweenFrames = 1000 / TARGET_FPS;
			long FrameCheck = System.currentTimeMillis() + (long) TimeBetweenFrames;

			// Wait until net time step
			while (FrameCheck > System.currentTimeMillis()) {
				// Do nothing
			}

			if (gameState == 0) { // Game Running

				gameloop();

			} else if (gameState == -1) { // Main Menu

				gamecanvas.setVisible(false);
				playButton.setVisible(true);
				Title.setVisible(true);
				BackgroundImageForStartMenu.setVisible(true);

				// gameMusic.stop();
				menuMusic.loop(1000);
				menuMusic.start();

			} else if (gameState == -2) { // Game Complete
				gamecanvas.setVisible(false);
				nextRound.setVisible(false);
				showGameOver();
			} else if (gameState == 1) { // Round Complete
				gamecanvas.setVisible(false);
				showRoundOver();
			}

			// UNIT test to see if framerate matches
			UnitTests.CheckFrameRate(System.currentTimeMillis(), FrameCheck, TARGET_FPS);
		}
	}

	private static void startGame() {
		gameworld = new Model();
		gamecanvas = new Viewer(gameworld);

		frame.add(gamecanvas);

		menuMusic.stop();

		gamecanvas.setBounds(0, 0, WIDTH, HEIGHT);
		gamecanvas.setBackground(new Color(255, 255, 255)); //white background  replaced by Space background but if you remove the background method this will draw a white screen
		gamecanvas.setVisible(true);
		gamecanvas.addKeyListener(Controller); // add the controller to the canvas
		gamecanvas.requestFocusInWindow(); // making sure that the Canvas is in focus so it can get input from the keyboard
		gameState = 0;
	}

	private static void showRoundOver() {

		// Add Round Over Label to Frame
		JLabel roundOver = new JLabel("Round Over");
		roundOver.setBounds(500, 100, 200, 50);
		frame.add(roundOver);

		// Add Play Button to Frame
		nextRound = new JButton("Next Round");
		nextRound.setBounds(500, 500, 100, 50);
		frame.add(nextRound);

		// Add Black Background to Frame
		JLabel blackBackground = new JLabel();
		blackBackground.setOpaque(true);
		blackBackground.setBackground(Color.BLACK);
		blackBackground.setBounds(0, 0, WIDTH, HEIGHT);
		frame.add(blackBackground);

		// Add Next Round Button Action Listener
		nextRound.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				roundOver.setVisible(false);
				nextRound.setVisible(false);
				startGame();
				gameworld.nextLevel();
			}
		}));

	}

	private static void showGameOver() {

		BackgroundImageForStartMenu.setVisible(true);

		// Add Game Over Label to Frame
		JLabel gameOver = new JLabel("Game Over");
		gameOver.setBounds(500, 100, 200, 50);
		frame.add(gameOver);

		// Add Exit Button to Frame
		exitButton = new JButton("Exit");
		exitButton.setBounds(500, 500, 100, 50);
		frame.add(exitButton);

		// Add Main Menu Button to Frame
		menuButton = new JButton("Main Menu");
		menuButton.setBounds(500, 400, 100, 50);
		frame.add(menuButton);

		// Add Black Background to Frame
		JLabel blackBackground = new JLabel();
		blackBackground.setOpaque(true);
		blackBackground.setBackground(Color.BLACK);
		blackBackground.setBounds(0, 0, WIDTH, HEIGHT);
		frame.add(blackBackground);

		// Add Restart Button Action Listener
		exitButton.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}));

		// Add Main Menu Button Action Listener
		menuButton.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameOver.setVisible(false);
				exitButton.setVisible(false);
				menuButton.setVisible(false);
				blackBackground.setVisible(false);

				gameState = -1;
			}
		}));
	}

	//Basic Model-View-Controller pattern 
	private static void gameloop() { 
		// GAMELOOP  
		
		// controller input  will happen on its own thread 
		// So no need to call it explicitly 
		
		// model update   
		gameworld.gamelogic();
		gameState = gameworld.gameState();
		// view update 
		gamecanvas.updateview();

		frame.setTitle("Reclaim The City");
		
		 
	}

}