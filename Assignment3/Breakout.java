/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File; 
import java.io.IOException; 
  
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;
	
/** Dimensions of game board (usually the same) */
	private static final int WIDTH = 400;
	private static final int HEIGHT = 600;
	
/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;
	
/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 60;
	
/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;
	
/** Number of total bricks */
	private static final int BRICKS_COUNT = NBRICKS_PER_ROW*NBRICK_ROWS;
	
/** Separation between bricks */
	private static final int BRICK_SEP = 4;
	
/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;
	
/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;
	
/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;
	
/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;
	
/** Number of turns */
	private static final int NTURNS = 3;
	
/** Playing board dimensions and coordinates */	
	private static final double BOARD_START_X = (APPLICATION_WIDTH - WIDTH)/2;
	private static final double BOARD_START_Y = (APPLICATION_HEIGHT-HEIGHT)/2;
	private static final double BOARD_END_X = (APPLICATION_WIDTH + WIDTH)/2;
	private static final double BOARD_END_Y = (APPLICATION_HEIGHT + HEIGHT)/2;
	private static final double BOARD_CENTER_X = BOARD_START_X + WIDTH/2;
	private static final double BOARD_CENTER_Y = BOARD_START_Y + HEIGHT/2;
	
/** The length of pause */
	private static final int PAUSE = 10;
	
/** Choose theme rectangle dimensions and coordinates */
	private static final double THEME_RECT_WIDTH = WIDTH/3;
	private static final double THEME_RECT_HEIGHT = HEIGHT/3;
	private static final double THEME_LIGHT_RECT_START_X = BOARD_START_X + WIDTH/9;
	private static final double THEME_DARK_RECT_START_X = THEME_LIGHT_RECT_START_X + THEME_RECT_WIDTH + WIDTH/9;
	private static final double THEME_RECT_START_Y = BOARD_START_Y + THEME_RECT_HEIGHT;
	private static final double THEME_RECT_OFFSET = 5;
	
/** Ball's speed */
	private static double vx, vy;

/** Color of paddle and ball */
	private static Color objectsColor = Color.BLACK;

/** Color of playing board's background*/
	private static Color backgroundColor = Color.WHITE;
	
	private static int remainedBricks = BRICKS_COUNT;
	private static int remainedLives = NTURNS;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private static AudioClip bounceClip;
	private static GRect paddle; 
	private static GOval ball;
	private static GRect lightThemeRectangle;
	private static GRect darkThemeRectangle;
	private static GRect board;
	private static GLabel scoreText;
	private static int highestScore;
	private static int score = 0;
	private Clip clip = null;
	private AudioInputStream str = null;
	
	
/** Run method*/
	public void run() {
		playMusic("intro.wav");
		chooseTheme();
		waitForClick();
		clip.stop();
		playMusic("playing.au");
		gameInitialization();
		waitForUserToClickStartPlaying();
		playGame();
	}
/** This method plays music */	
	private void playMusic(String fileName) {
		
		try {
			str = AudioSystem.getAudioInputStream(new File(fileName).getAbsoluteFile());
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clip.open(str);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		clip.loop(Clip.LOOP_CONTINUOUSLY); 
	}
/** This method draws board on a canvas */
	private void addBoard() {
		board = new GRect(BOARD_START_X, BOARD_START_Y, WIDTH, HEIGHT);
		board.setFilled(true);
		board.setFillColor(backgroundColor);
		add(board);
	}
/** With this method user chooses which theme he/she wants, light or dark */	
	private void chooseTheme() {
		GLabel chooseTheme = new GLabel("Choose theme");
		if(highestScore > 0) {
			GLabel highestScoreMessage = new GLabel("Highest score: " + highestScore);
			highestScoreMessage.setFont(new Font("Sans-Serif", Font.BOLD, 18));
			highestScoreMessage.setColor(Color.RED);
			add(
				highestScoreMessage, 
				BOARD_CENTER_X-highestScoreMessage.getWidth()/2, 
				THEME_RECT_START_Y + THEME_RECT_HEIGHT + (HEIGHT/3 - chooseTheme.getHeight())/2
			);
		}
		chooseTheme.setFont(new Font("Sans-Serif", Font.BOLD, 18));
		chooseTheme.setColor(Color.RED);
		add(chooseTheme, BOARD_CENTER_X-chooseTheme.getWidth()/2, BOARD_START_Y + (HEIGHT/3 - chooseTheme.getHeight())/2);
		drawRectangles(true);
		drawBricksInThemeRectangles();
		drawPaddlesInThemeRectangles();
		drawBallsInThemeRectangles();
		drawRectangles(false);
	}
	
/** Draws balls in theme choosing rectangles */
	private void drawBallsInThemeRectangles() {
		double lightThemeBallStartX = THEME_LIGHT_RECT_START_X + THEME_RECT_WIDTH/2 - BALL_RADIUS;
		double darkThemeBallStartX = THEME_DARK_RECT_START_X + THEME_RECT_WIDTH/2 - BALL_RADIUS;
		drawBallInThemeRectangle(lightThemeBallStartX, Color.BLACK);
		drawBallInThemeRectangle(darkThemeBallStartX, Color.WHITE);
	}
	
/** Draws paddle in theme choosing rectangles */
	private void drawPaddlesInThemeRectangles() {
		double lightThemePaddleStartX = THEME_LIGHT_RECT_START_X + (THEME_RECT_WIDTH - PADDLE_WIDTH)/2;
		double darkThemePaddleStartX = THEME_DARK_RECT_START_X + (THEME_RECT_WIDTH - PADDLE_WIDTH)/2;
		drawPaddleInThemeRectangle(lightThemePaddleStartX, Color.BLACK);
		drawPaddleInThemeRectangle(darkThemePaddleStartX, Color.WHITE);
	}
	
/** Draws bricks in theme choosing rectangles */
	private void drawBricksInThemeRectangles() {
		double lightThemeBricksStartX = THEME_LIGHT_RECT_START_X + (THEME_RECT_WIDTH - 3*BRICK_WIDTH - 2*BRICK_SEP)/2;
		double darkThemeBricksStartX = THEME_DARK_RECT_START_X + (THEME_RECT_WIDTH - 3*BRICK_WIDTH - 2*BRICK_SEP)/2;
		drawBricksInThemeRectangle(lightThemeBricksStartX);
		drawBricksInThemeRectangle(darkThemeBricksStartX);
	}
/** Draws ball in theme choosing rectangle */
	private void drawBallInThemeRectangle(double ballStartX, Color color) {
		double diameter = 2*BALL_RADIUS;
		double ballStartY = THEME_RECT_START_Y + THEME_RECT_HEIGHT - PADDLE_HEIGHT - 2*THEME_RECT_OFFSET - diameter;
		GOval ball = new GOval(ballStartX, ballStartY, diameter, diameter);
		ball.setFilled(true);
		ball.setColor(color);
		ball.setFillColor(color);
		add(ball);
	}
/** Draws paddle in theme choosing rectangle */	
	private void drawPaddleInThemeRectangle(double paddleStartX, Color color) {
		double paddleStartY = THEME_RECT_START_Y + THEME_RECT_HEIGHT - PADDLE_HEIGHT - THEME_RECT_OFFSET;
		GRect themePaddle = new GRect(paddleStartX, paddleStartY, PADDLE_WIDTH, PADDLE_HEIGHT);
		themePaddle.setColor(color);
		themePaddle.setFilled(true);
		themePaddle.setFillColor(color);
		add(themePaddle);
	}
/** Draws bricks in theme choosing rectangle */	
	private void drawBricksInThemeRectangle(double bricksStartX) {
		double bricksStartY = THEME_RECT_START_Y - BRICK_Y_OFFSET + THEME_RECT_OFFSET;
		for(int i=0; i<10; i+=2) {
			for(int j=0; j<2; j++) {
				drawOneLine(bricksStartX, bricksStartY, getColorByNumber(i), 3);
				bricksStartY += BRICK_HEIGHT;
				bricksStartY += BRICK_SEP;
			}
		}
	}
/** Draws theme rectangles on a canvas */
	private void drawRectangles(boolean isVisible) {
		if(isVisible) {
			GRect rectForLightTheme = new GRect(THEME_LIGHT_RECT_START_X, THEME_RECT_START_Y, THEME_RECT_WIDTH, THEME_RECT_HEIGHT);
			GRect rectForDarkTheme = new GRect(THEME_DARK_RECT_START_X, THEME_RECT_START_Y, THEME_RECT_WIDTH, THEME_RECT_HEIGHT);
			rectForDarkTheme.setFilled(true);
			rectForDarkTheme.setFillColor(Color.BLACK);
			add(rectForLightTheme);
			add(rectForDarkTheme);
		}else {
			addInvisibleThemeRectangles();
		}
	}
/** Adds invisible rectangles on the top of theme rectangles so we can determine on which one user has clicked */
	private void addInvisibleThemeRectangles() {
		lightThemeRectangle = new GRect(THEME_LIGHT_RECT_START_X, THEME_RECT_START_Y, THEME_RECT_WIDTH, THEME_RECT_HEIGHT);
		darkThemeRectangle = new GRect(THEME_DARK_RECT_START_X, THEME_RECT_START_Y, THEME_RECT_WIDTH, THEME_RECT_HEIGHT);
		lightThemeRectangle.setFilled(true);
		lightThemeRectangle.setFillColor(Color.BLACK);
		darkThemeRectangle.setFilled(true);
		darkThemeRectangle.setFillColor(Color.BLACK);
		lightThemeRectangle.setVisible(false);
		darkThemeRectangle.setVisible(false);
		add(lightThemeRectangle);
		add(darkThemeRectangle);
		addThemeRectangleMouseEventListeners();
	}
/** Adds mouse listeners to that invisible rectangles */
	private void addThemeRectangleMouseEventListeners() {
		darkThemeRectangle.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				objectsColor = Color.WHITE;
				backgroundColor = Color.BLACK;
				removeAll();
			}

			@Override
			public void mouseEntered(MouseEvent arg0){}
			@Override
			public void mouseExited(MouseEvent arg0){}
			@Override
			public void mousePressed(MouseEvent arg0){}
			@Override
			public void mouseReleased(MouseEvent arg0){}
		});
		
		lightThemeRectangle.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				objectsColor = Color.BLACK;
				backgroundColor = Color.WHITE;
				removeAll();
			}

			@Override
			public void mouseEntered(MouseEvent arg0){}
			@Override
			public void mouseExited(MouseEvent arg0){}
			@Override
			public void mousePressed(MouseEvent arg0){}
			@Override
			public void mouseReleased(MouseEvent arg0){}
		});
	}
	
/** Draws board, bricks, paddle and ball on the canvas */
	private void gameInitialization() {
		addBoard();
		drawBricks();
		initializePaddle();
		updateScore(0);
		initializeBall();
	}
/** This method updates score whenever it changes */
	private void updateScore(int scoreAtCurrentMoment) {
		scoreText = new GLabel("Your score: " + scoreAtCurrentMoment);
		scoreText.setColor(Color.RED);
		add(scoreText, BOARD_CENTER_X - scoreText.getWidth()/2, BOARD_END_Y - (PADDLE_Y_OFFSET + scoreText.getHeight())/2);
	}
	
/** This method draws ball*/
	private void initializeBall() {
		double diameter = 2*BALL_RADIUS;
		ball = new GOval(BOARD_CENTER_X - BALL_RADIUS, BOARD_CENTER_Y - BALL_RADIUS, diameter, diameter);
		ball.setFilled(true);
		ball.setFillColor(objectsColor);
		ball.setColor(objectsColor);
		add(ball);
	}
/** This method is responsible for playing */
	private void playGame() {
		bounceClip = MediaTools.loadAudioClip("bounce.au");
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy = 5;
		
		while(true) {
			GObject collider = getCollidingObject();
			if(collider != null) {
				if(collider != paddle) {
					score += getScoreByColor(collider.getColor());
					remove(scoreText);
					updateScore(score);
					remove(collider);
					remainedBricks--;
					if(remainedBricks == 0) {
						gameOver(true);
						break;
					}

					checkForLeftOrRightColliders();
				}else if(ball.getY() + BALL_RADIUS <= paddle.getY()) {
					if(vy>0) vy=-vy;
				}
				bounceClip.play();
			}
			
			if(ball.getX() + 2*BALL_RADIUS >= BOARD_END_X || ball.getX() <= BOARD_START_X) {
				vx = -vx;
				bounceClip.play();
			}
			
			if(ball.getY() + 2*BALL_RADIUS >= BOARD_END_Y || ball.getY() <= BOARD_START_Y) {
				if(ball.getY() +2*BALL_RADIUS >= BOARD_END_Y) {
					lostTurn();
					break;
				}else {
					vy = -vy;
					bounceClip.play();
				}
			}
			
			ball.move(vx, vy);
			pause(PAUSE);
		}
	}

/** This method checks if ball's right and left points hit any object and if it is not a board, returns it*/
	private void checkForLeftOrRightColliders() {
		GObject ballLeftPointCollider = 
				getElementAt(ball.getX()-1, ball.getY()+BALL_RADIUS) == board 
				? null 
				: getElementAt(ball.getX()-1, ball.getY()+BALL_RADIUS);
		
		GObject ballRightPointCollider = 
				getElementAt(ball.getX()+2*BALL_RADIUS+1, ball.getY()+BALL_RADIUS) == board 
				? null 
				: getElementAt(ball.getX()+2*BALL_RADIUS+1, ball.getY()+BALL_RADIUS);
		
		if(ballLeftPointCollider != null || ballRightPointCollider != null) {
			vx = -vx;
		}else {
			vy = -vy;
		}
		bounceClip.play();
	}
/** This method is called when ball touches a bottom line of the board */
	private void lostTurn() {
		if(remainedLives > 1) {
			remainedLives--;
			remove(ball);
			initializeBall();
			playGame();
		}else {
			gameOver(false);
		}
	}
/** With this method program waits for user to click and start playing */
	private void waitForUserToClickStartPlaying() {
		GLabel startGame = new GLabel("Click to start playing");
		startGame.setColor(Color.RED);
		add(startGame, BOARD_CENTER_X-startGame.getWidth()/2, BOARD_CENTER_Y - startGame.getHeight());
		waitForClick();
		remove(startGame);
	}
/** This method is responsible for ending a game */
	private void gameOver(boolean won) {
		String result;
		
		if(won) {
			result = "You Won!";
			clip.stop();
			playMusic("success.wav");
			clip.loop(0);
		}
		else {
			result = "You lost.";
			clip.stop();
			playMusic("fail.wav");
			clip.loop(0);
		}
		
		GLabel gameOver = new GLabel ("Game Over. " + result + " Click to play again");
		gameOver.setFont(new Font("Sans-Serif", Font.BOLD, 18));
		gameOver.setColor(Color.RED);
		add (gameOver, BOARD_CENTER_X-gameOver.getWidth()/2, BOARD_CENTER_Y - gameOver.getHeight());
		waitForClick();
		if(clip.isRunning())
			clip.stop();
		removeAll();
		if(score > highestScore) highestScore = score;
		score = 0;
		remainedLives = NTURNS;
		remainedBricks = BRICKS_COUNT;
		run();
	}
/** This method returns colliding object */	
	private GObject getCollidingObject() {
		double ballX = ball.getX();
		double ballY = ball.getY();
		
		
		GObject collider = getElementAt(ballX + BALL_RADIUS + 1, ballY);
		
		if(collider != null && collider != board && collider != scoreText) return collider;
		
		collider = getElementAt(ballX, ballY);
		
		if(collider != null && collider != board && collider != scoreText) return collider;
		
		collider = getElementAt(ballX + 2*BALL_RADIUS, ballY);
		
		if(collider != null && collider != board && collider != scoreText) return collider;
		
		collider = getElementAt(ballX + 2*BALL_RADIUS, ballY + 2*BALL_RADIUS);
		
		if(collider != null && collider != board && collider != scoreText) return collider;
		
		collider = getElementAt(ballX, ballY + 2*BALL_RADIUS);
		
		if(collider != null && collider != board && collider != scoreText) return collider;
		
		return null;
	}
/** This method draws paddle*/
	private void initializePaddle() {
		double paddleStartX = BOARD_CENTER_X - PADDLE_WIDTH/2;
		double paddleStartY = BOARD_END_Y - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		paddle = new GRect(paddleStartX, paddleStartY, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setColor(objectsColor);
		paddle.setFilled(true);
		paddle.setFillColor(objectsColor);
		add(paddle);
		addMouseListeners();
	}
/** This is mouse motion event listener for paddle */
	public void mouseMoved(MouseEvent e) {
		if((e.getX() - PADDLE_WIDTH/2) > BOARD_START_X && (e.getX() + PADDLE_WIDTH/2) < BOARD_END_X){
			paddle.setLocation(e.getX() - PADDLE_WIDTH/2, BOARD_END_Y - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
		}
	}
/** This method draws bricks */
	private void drawBricks() {
		double bricksTotalWidth = BRICK_WIDTH*NBRICKS_PER_ROW;
		double separationTotalWidth = BRICK_SEP*(NBRICKS_PER_ROW-1);
		double bricksStartX = BOARD_START_X + (WIDTH - bricksTotalWidth - separationTotalWidth)/2;
		double bricksStartY = BOARD_START_Y;
		for(int i=0; i<NBRICK_ROWS; i++){
			drawOneLine(bricksStartX, bricksStartY + i*(BRICK_HEIGHT + BRICK_SEP), getColorByNumber(i), NBRICKS_PER_ROW);
		}
	}
/** This method draws one line of bricks */
	private void drawOneLine(double startX, double startY, Color color, int count) {
		for(int i=0; i<count; i++){
			drawBrick(startX + i*(BRICK_WIDTH+BRICK_SEP), startY + BRICK_Y_OFFSET, color);
			pause(15);
		}
	}
/** This method draws single brick */
	private void drawBrick(double startX, double startY, Color color){
		GRect brick = new GRect(startX, startY, BRICK_WIDTH, BRICK_HEIGHT);
		brick.setColor(color);
		brick.setFilled(true);
		brick.setFillColor(color);
		add(brick);
	}
/** This method returns brick's score */
	private int getScoreByColor(Color color) {
		if(color == Color.RED) return 500;
		if(color == Color.ORANGE) return 400;
		if(color == Color.YELLOW) return 300;
		if(color == Color.GREEN) return 200;
		if(color == Color.CYAN) return 100;
		
		return 0;
	}
/** This method returns color for brick by number*/
	private Color getColorByNumber(int number){
		if(number == 0 || number == 1) return Color.RED;
		if(number == 2 || number == 3) return Color.ORANGE;
		if(number == 4 || number == 5) return Color.YELLOW;
		if(number == 6 || number == 7) return Color.GREEN;
		if(number == 8 || number == 9) return Color.CYAN;

		return Color.BLACK;
	}
}
