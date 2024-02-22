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

import util.GlobalGameState;
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



@SuppressWarnings("ALL")
public class MainWindow {

	// Final Values
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 700;
	private static final int TARGET_FPS = 100;

	// JFrame Components
	private static final JFrame frame = new JFrame("Protect the City"); // Change to the name of your game
	private static Viewer gamecanvas;
	private static JLabel BackgroundImageForStartMenu;
	private static JLabel BackgroundImageForRoundFailed;
	private static JLabel BackgroundImageForRoundComplete;
	private static JLabel BackgroundImageForGameOver;
	private static JLabel gameTitle;
	private static JLabel roundFailedTitle;
	private static JLabel roundCompleteTitle;
	private static JLabel gameCompleteTitle;
	private static Clip menuMusic;
	private static Clip roundFailedMusic;
	private static Clip gameMusic;
	private static Clip roundCompleteMusic;
	private static JButton playButton;
	private static JButton nextRoundButton;
	private static JButton retryButton;
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

		// Region: Import Files

		// * Backgrounds * //
		File menu_background = new File("res/gui/backgrounds/BG_MainMenu.png");
		File round_failed_background = new File("res/gui/backgrounds/BG_MissionFailed.png");
		File round_complete_background = new File("res/gui/backgrounds/BG_MainMenu.png");
		File game_complete_background = new File("res/gui/backgrounds/BG_GameOver.png");

		// * Titles * //
		File game_title = new File("res/gui/titles/Title_Game.png");
		File round_complete_title = new File("res/gui/titles/Title_MissionComplete.png");
		File round_failed_title = new File("res/gui/titles/Title_MissionFailed.png");
		File game_complete_title = new File("res/gui/titles/Title_GameComplete.png");

		// * Buttons * //
		File play = new File("res/gui/buttons/Button_Start.png");
		File exit = new File("res/gui/buttons/Button_Exit.png");
		File next = new File("res/gui/buttons/Button_NextLevel.png");
		File retry = new File("res/gui/buttons/Button_Retry.png");
		File menu = new File("res/gui/buttons/Button_MainMenu.png");

		// * Music * //
		File menu_music = new File("res/sounds/menu_music.wav");
		File round_failed_music = new File("res/sounds/mission_failed.wav");
		File game_music = new File("res/sounds/game_music.wav");
		File round_complete_music = new File("res/sounds/mission_success.wav");

		// Region: Buttons

		// * Play Button * //
		try {
			BufferedImage playImage = ImageIO.read(play);
			Image img = playImage.getScaledInstance(200, 80, Image.SCALE_SMOOTH);

			playButton = new JButton(new ImageIcon(img));
			playButton.setBorder(BorderFactory.createEmptyBorder());
			playButton.setContentAreaFilled(false);
			playButton.setBounds(500, 340, 200, 80);
			frame.add(playButton);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// * Next Round Button * //
		try {
			BufferedImage nextImage = ImageIO.read(next);
			Image img = nextImage.getScaledInstance(200, 80, Image.SCALE_SMOOTH);

			nextRoundButton = new JButton(new ImageIcon(img));
			nextRoundButton.setBorder(BorderFactory.createEmptyBorder());
			nextRoundButton.setContentAreaFilled(false);
			nextRoundButton.setBounds(500, 340, 200, 80);
			frame.add(nextRoundButton);
			nextRoundButton.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// * Retry Button * //
		try {
			BufferedImage retryImage = ImageIO.read(retry);
			Image img = retryImage.getScaledInstance(200, 80, Image.SCALE_SMOOTH);

			retryButton = new JButton(new ImageIcon(img));
			retryButton.setBorder(BorderFactory.createEmptyBorder());
			retryButton.setContentAreaFilled(false);
			retryButton.setBounds(500, 340, 200, 80);
			frame.add(retryButton);
			retryButton.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// * Exit Button * //
		try {
			BufferedImage exitImage = ImageIO.read(exit);
			Image img = exitImage.getScaledInstance(200, 80, Image.SCALE_SMOOTH);

			exitButton = new JButton(new ImageIcon(img));
			exitButton.setBorder(BorderFactory.createEmptyBorder());
			exitButton.setContentAreaFilled(false);
			exitButton.setBounds(500, 430, 200, 80);
			frame.add(exitButton);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// * Menu Button * //
		try {
			BufferedImage menuImage = ImageIO.read(menu);
			Image img = menuImage.getScaledInstance(200, 80, Image.SCALE_SMOOTH);

			menuButton = new JButton(new ImageIcon(img));
			menuButton.setBorder(BorderFactory.createEmptyBorder());
			menuButton.setContentAreaFilled(false);
			menuButton.setBounds(500, 340, 200, 80);
			frame.add(menuButton);
			menuButton.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Region: Titles

		// * Game Title * //
		try {
			BufferedImage titleImage = ImageIO.read(game_title);
			Image img = titleImage.getScaledInstance(900, 225, Image.SCALE_SMOOTH);

			gameTitle = new JLabel(new ImageIcon(img));
			gameTitle.setBounds(150, 100, 900, 225);
			frame.add(gameTitle);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// * Round Complete Title * //
		try {
			BufferedImage titleImage = ImageIO.read(round_complete_title);
			Image img = titleImage.getScaledInstance(900, 225, Image.SCALE_SMOOTH);

			roundCompleteTitle = new JLabel(new ImageIcon(img));
			roundCompleteTitle.setBounds(150, 100, 900, 225);
			frame.add(roundCompleteTitle);
			roundCompleteTitle.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// * Round Failed Title * //
		try {
			BufferedImage failedImage = ImageIO.read(round_failed_title);
			Image img = failedImage.getScaledInstance(900, 225, Image.SCALE_SMOOTH);

			roundFailedTitle = new JLabel(new ImageIcon(img));
			roundFailedTitle.setBounds(150, 100, 900, 225);
			frame.add(roundFailedTitle);
			roundFailedTitle.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// * Game Complete Title * //
		try {
			BufferedImage titleImage = ImageIO.read(game_complete_title);
			Image img = titleImage.getScaledInstance(900, 225, Image.SCALE_SMOOTH);

			gameCompleteTitle = new JLabel(new ImageIcon(img));
			gameCompleteTitle.setBounds(150, 100, 900, 225);
			frame.add(gameCompleteTitle);
			gameCompleteTitle.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Region: Backgrounds

		// * Main Menu Background * //
		try {
			BufferedImage backgroundImage = ImageIO.read(menu_background);
			BackgroundImageForStartMenu = new JLabel(new ImageIcon(backgroundImage));
			BackgroundImageForStartMenu.setBounds(0, 0, WIDTH, HEIGHT);
			frame.add(BackgroundImageForStartMenu);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// * Round Complete Background * //
		try {
			BufferedImage backgroundImage = ImageIO.read(round_complete_background);
			BackgroundImageForRoundComplete = new JLabel(new ImageIcon(backgroundImage));
			BackgroundImageForRoundComplete.setBounds(0, 0, WIDTH, HEIGHT);
			frame.add(BackgroundImageForRoundComplete);
			BackgroundImageForRoundComplete.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// * Round Failed Background * //
		try {
			BufferedImage backgroundImage = ImageIO.read(round_failed_background);
			BackgroundImageForRoundFailed = new JLabel(new ImageIcon(backgroundImage));
			BackgroundImageForRoundFailed.setBounds(0, 0, WIDTH, HEIGHT);
			frame.add(BackgroundImageForRoundFailed);
			BackgroundImageForRoundFailed.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// * Game Complete Background * //
		try {
			BufferedImage backgroundImage = ImageIO.read(game_complete_background);
			BackgroundImageForGameOver = new JLabel(new ImageIcon(backgroundImage));
			BackgroundImageForGameOver.setBounds(0, 0, WIDTH, HEIGHT);
			frame.add(BackgroundImageForGameOver);
			BackgroundImageForGameOver.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}


		// Region: Audio

		// * Menu Music * //
		try {
			AudioInputStream menuAudio = AudioSystem.getAudioInputStream(menu_music);
			DataLine.Info mInfo = new DataLine.Info(Clip.class, menuAudio.getFormat());
			menuMusic = (Clip) AudioSystem.getLine(mInfo);
			menuMusic.open(menuAudio);
			FloatControl menuControl = (FloatControl) menuMusic.getControl(FloatControl.Type.MASTER_GAIN);
			menuControl.setValue(0f);
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}

		// * Round Failed Music * //
		try {
			AudioInputStream roundFailedAudio = AudioSystem.getAudioInputStream(round_failed_music);
			DataLine.Info mInfo = new DataLine.Info(Clip.class, roundFailedAudio.getFormat());
			roundFailedMusic = (Clip) AudioSystem.getLine(mInfo);
			roundFailedMusic.open(roundFailedAudio);
			FloatControl roundFailedControl = (FloatControl) roundFailedMusic.getControl(FloatControl.Type.MASTER_GAIN);
			roundFailedControl.setValue(0f);
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}

		//* Game Music * //
		try {
			AudioInputStream gameAudio = AudioSystem.getAudioInputStream(game_music);
			DataLine.Info mInfo = new DataLine.Info(Clip.class, gameAudio.getFormat());
			gameMusic = (Clip) AudioSystem.getLine(mInfo);
			gameMusic.open(gameAudio);
			FloatControl gameControl = (FloatControl) gameMusic.getControl(FloatControl.Type.MASTER_GAIN);
			gameControl.setValue(-2f);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// * Round Complete Music * //
		try {
			AudioInputStream roundCompleteAudio = AudioSystem.getAudioInputStream(round_complete_music);
			DataLine.Info mInfo = new DataLine.Info(Clip.class, roundCompleteAudio.getFormat());
			roundCompleteMusic = (Clip) AudioSystem.getLine(mInfo);
			roundCompleteMusic.open(roundCompleteAudio);
			FloatControl roundCompleteControl = (FloatControl) roundCompleteMusic.getControl(FloatControl.Type.MASTER_GAIN);
			roundCompleteControl.setValue(0f);
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}

		// Region: Action Listeners

		// * playButton Action Listener * //
		playButton.addActionListener((e -> {

			// * Hide Main Menu Components * //
			playButton.setVisible(false);
			exitButton.setVisible(false);
			gameTitle.setVisible(false);
			BackgroundImageForStartMenu.setVisible(false);

			menuMusic.stop();

            startGame();
        }));

		// * exitButton Action Listener * //
		exitButton.addActionListener((e -> System.exit(0)));

		// * nextRound Action Listener * //
		nextRoundButton.addActionListener((e -> {
            roundCompleteTitle.setVisible(false);
            nextRoundButton.setVisible(false);
            BackgroundImageForRoundComplete.setVisible(false);

			GlobalGameState.getInstance().nextLevel();
            startGame();
        }));

		// * retryButton Action Listener * //
		retryButton.addActionListener((e -> {

            roundFailedTitle.setVisible(false);
            retryButton.setVisible(false);
            BackgroundImageForRoundFailed.setVisible(false);
			roundFailedMusic.stop();

			GlobalGameState.getInstance().setCurrentLevel(1);

            startGame();
        }));

		// * menuButton Action Listener * //
		menuButton.addActionListener((e -> {
			gameState = -1;
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

				gameMusic.loop(1000);
				gameMusic.start();

				gameloop();

			} else if (gameState == -1) { // Main Menu

				gamecanvas.setVisible(false);

				// * Show Main Menu Components * //
				playButton.setVisible(true);
				exitButton.setVisible(true);
				gameTitle.setVisible(true);
				BackgroundImageForStartMenu.setVisible(true);

				menuMusic.setMicrosecondPosition(0);
				menuMusic.loop(1000);
				menuMusic.start();

			} else if (gameState == 1) { // Game Complete

				gamecanvas.setVisible(false);
				gameMusic.stop();

				// * Show Game Complete Components * //
				menuButton.setVisible(true);
				exitButton.setVisible(true);
				gameCompleteTitle.setVisible(true);
				BackgroundImageForGameOver.setVisible(true);


				GlobalGameState.getInstance().setCurrentLevel(1);

			} else if (gameState == 2) { //Round Complete

				gamecanvas.setVisible(false);
				gameMusic.stop();

				// * Show Round Complete Components * //
				nextRoundButton.setVisible(true);
				roundCompleteTitle.setVisible(true);
				BackgroundImageForRoundComplete.setVisible(true);
				
				roundCompleteMusic.start();

			} else if (gameState == 3) { // Round Failed

				gamecanvas.setVisible(false);
				gameMusic.stop();

				// * Show Round Failed Components * //
				retryButton.setVisible(true);
				roundFailedTitle.setVisible(true);
				BackgroundImageForRoundFailed.setVisible(true);
				menuMusic.setMicrosecondPosition(0);
				roundFailedMusic.start();
			}

			// UNIT test to see if framerate matches
			UnitTests.CheckFrameRate(System.currentTimeMillis(), FrameCheck, TARGET_FPS);
		}
	}

	private static void startGame() {
		gameworld = new Model();
		gamecanvas = new Viewer(gameworld);

		frame.add(gamecanvas);

		gamecanvas.setBounds(0, 0, WIDTH, HEIGHT);
		gamecanvas.setBackground(new Color(255, 255, 255)); //white background  replaced by Space background but if you remove the background method this will draw a white screen
		gamecanvas.setVisible(true);
		gamecanvas.addKeyListener(Controller); // add the controller to the canvas
		gamecanvas.requestFocusInWindow(); // making sure that the Canvas is in focus so it can get input from the keyboard
		gameState = 0;
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