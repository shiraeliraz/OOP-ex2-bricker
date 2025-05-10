package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.LifeHandler;
import bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;

import java.awt.*;

/**
 * BrickerGameManager is the main class responsible for initializing and managing
 * the Bricker game. It sets up the game window, loads all game objects,
 * and handles the game loop logic such as lives, collisions, and win/loss conditions.
 *
 * @author - roni.shpitzer,shukka
 */
public class BrickerGameManager extends GameManager {


	/**constants and variables */
	private static final int BALL_SIZE = 20;
	private static final float BALL_SPEED = 150f;
	private static final int GAP_WIDTH = 4;
	private static final int BRICK_HEIGHT = 15;
	private static final int WALL_WIDTH = 5;
	private static final String LOSE_MESSAGE = "You lose! Play again?";
	private static final String WIN_MESSAGE = "You win! Play again?";
	private static final String ASSETS_BRICK_PNG = "assets/brick.png";
	private static final String ASSETS_BALL_PNG = "assets/ball.png";
	private static final String ASSETS_BLOP_WAV = "assets/blop.wav";
	private static final String ASSETS_REDBALL_PNG = "assets/redball.png";
	private static final String ASSETS_DARK_BG_2_SMALL_JPEG = "assets/DARK_BG2_small.jpeg";
	private static final String ASSETS_PADDLE_PNG = "assets/paddle.png";
	private static final int ZERO = 0;
	private static final int PADDLE_WIDTH = 100;
	private static final int PADDLE_HEIGHT = 15;
	private static final int CENTER_HEIGHT_WINDOW = 30;
	private static final String BRICKER = "Bricker";
	private static final int WINDOW_WIDTH = 700;
	private static final int WINDOW_HEIGHT = 500;
	private static final int DEFAULT_ROWS = 7;
	private static final int DEFAULT_COLS = 8;
	private int numberOfRows = DEFAULT_ROWS;
	private int numberOfColumns = DEFAULT_COLS;
	private static final int DEFAULT_LIVES = 3;
	private static final int WALL_COLOR_R = 225;
	private static final int WALL_COLOR_G = 120;
	private static final int WALL_COLOR_B = 140;
	private static final float CENTER_FACTOR = 0.5f;
	private final Vector2 windowDimensions;
	private Ball ball;
	private WindowController windowController;
	private UserInputListener inputListener;
	private LifeHandler lifeHandler;
	private Counter brickCounter = new Counter(0);
	private int remainingLives = DEFAULT_LIVES;
	/** The image used to render the main ball. */
	private ImageRenderable ballImage;
	private StrategyFactory strategyFactory;
	private ImageReader imageReader;

	/**
	 * Constructs a game manager with custom board dimensions.
	 *
	 * @param windowTitle      - the window title
	 * @param windowDimensions - the window dimensions
	 * @param numberOfRows     - number of rows of bricks
	 * @param numberOfColumns  - number of cols of brick
	 */
	public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numberOfRows,
							  int numberOfColumns) {
		super(windowTitle, windowDimensions);
		this.windowDimensions = windowDimensions;
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
	}

	/**
	 * Constructs a game manager with default board dimensions.
	 *
	 * @param windowTitle      - the window title
	 * @param windowDimensions - the window dimensions
	 */
	public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
		super(windowTitle, windowDimensions);
		this.windowDimensions = windowDimensions;

	}

	/**
	 * A getter for the amount of lives
	 *
	 * @return - the amount of lives
	 */
	public int getLives() {
		return remainingLives;
	}

	/**
	 * A getter for the life handler instance
	 *
	 * @return - the life handler instance
	 */
	public LifeHandler getLifeHandler() {
		return lifeHandler;
	}

	/**
	 * A getter for the windowDimensions
	 *
	 * @return - the window dimensions
	 */
	public Vector2 getWindowDimensions() {

		return windowDimensions;
	}

	/**
	 * Creating the walls of the game
	 */
	private void createWalls() { // why is it public?
		Color pink = new Color(WALL_COLOR_R, WALL_COLOR_G, WALL_COLOR_B);
		Renderable rectangle = new RectangleRenderable(pink);
		GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(WALL_WIDTH, windowDimensions.y()),
				rectangle);
		this.gameObjects().addGameObject(leftWall);

		GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() - WALL_WIDTH, ZERO),
				new Vector2(WALL_WIDTH, windowDimensions.y()), rectangle);
		this.gameObjects().addGameObject(rightWall);

		GameObject ceiling = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
				(float) WALL_WIDTH), rectangle);
		this.gameObjects().addGameObject(ceiling);

	}

	/**
	 * A method in charge of placing bricks in a row
	 *
	 * @param imageReader - image reader for the brick image
	 * @param y           - the height of the brick
	 */
	private void placeRow(ImageReader imageReader, float y) {
		float brickWidth = (windowDimensions.x() - WALL_WIDTH * 2 -
				(numberOfColumns + 1) * GAP_WIDTH) / numberOfColumns;
		Vector2 topLeftCorner = new Vector2(WALL_WIDTH + GAP_WIDTH, y + WALL_WIDTH + GAP_WIDTH);
		Vector2 brickDimensions = new Vector2(brickWidth, BrickerGameManager.BRICK_HEIGHT);
		for (int i = 0; i < numberOfColumns; i++) {
			createSingleBrick(topLeftCorner, brickDimensions, imageReader);
			topLeftCorner = topLeftCorner.add(new Vector2(brickWidth + GAP_WIDTH, ZERO));
			brickCounter.increment();
		}
	}

	/**
	 * A method in charge of creating a single brick and adding it to the gameObjectCollection
	 *
	 * @param topLeftCorner   - the top left corner of the brick
	 * @param brickDimensions - the brick dimensions
	 * @param imageReader     - the image reader for the brick image
	 */
	private void createSingleBrick(Vector2 topLeftCorner, Vector2 brickDimensions,
								   ImageReader imageReader) {
		CollisionStrategy collisionStrategy = strategyFactory.buildStrategyByProbability();
		Renderable brickImage = imageReader.readImage(ASSETS_BRICK_PNG, false);
		Brick brick = new Brick(topLeftCorner, brickDimensions, brickImage, collisionStrategy);
		this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
	}

	/**
	 * A method to initialize the game
	 *
	 * @param imageReader      Contains a single method: readImage, which reads an image from disk.
	 *                         See its documentation for help.
	 * @param soundReader      Contains a single method: readSound, which reads a wav file from
	 *                         disk. See its documentation for help.
	 * @param inputListener    Contains a single method: isKeyPressed, which returns whether
	 *                         a given key is currently pressed by the user or not. See its
	 *                         documentation.
	 * @param windowController Contains an array of helpful, self explanatory methods
	 *                         concerning the window.
	 */
	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader,
							   UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		this.windowController = windowController;
		lifeHandler = new LifeHandler(this.gameObjects(), remainingLives, imageReader, windowDimensions.y());
		lifeHandler.createAllHearts();
		lifeHandler.setLives(remainingLives);
		this.inputListener = inputListener;
		this.imageReader = imageReader;

		// creating ball
		createBall(soundReader);
		initBall();
		//creating background
		createBackground();

		// creating paddle
		createPaddle(inputListener);

		// creating a brick strategy
		GameObjectCollection gameObjectCollection = this.gameObjects();

		this.strategyFactory = new StrategyFactory(gameObjectCollection, brickCounter,
				this, inputListener, imageReader, ball, soundReader);
		placeBricks();

	}

	/**
	 * A method in charge of creating the main ball of the game
	 *
	 * @param soundReader - sound reader for the ball sound
	 */
	private void createBall(SoundReader soundReader) {
		this.ballImage =
				imageReader.readImage(ASSETS_BALL_PNG, true);
		Sound collisionSound = soundReader.readSound(ASSETS_BLOP_WAV);
		Renderable redBallImage = imageReader.readImage(ASSETS_REDBALL_PNG, true);
		this.ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE),
				this.ballImage, collisionSound, redBallImage);
	}

	/**
	 * A method in charge of creating the background of the game
	 */
	private void createBackground() {
		Renderable backGroundImage =
				imageReader.readImage(ASSETS_DARK_BG_2_SMALL_JPEG, false);
		GameObject background =
				new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), windowDimensions.y()),
						backGroundImage);
		this.gameObjects().addGameObject(background, Layer.BACKGROUND);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
	}

	/**
	 * A method in charge of placing all the bricks of the game
	 */
	private void placeBricks() {
		int y = GAP_WIDTH + WALL_WIDTH;
		for (int i = 0; i < numberOfRows; i++) {
			placeRow(imageReader, y);
			y += BRICK_HEIGHT + GAP_WIDTH;
		}
	}

	/**
	 * A method in charge of creating the main paddle of the game
	 *
	 * @param inputListener - the input listener for moving the paddle
	 */
	private void createPaddle(UserInputListener inputListener) {
		Renderable paddleImage = imageReader.readImage(ASSETS_PADDLE_PNG, true);
		Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
				inputListener, windowDimensions);
		paddle.setCenter(new Vector2(windowDimensions.x() * CENTER_FACTOR, windowDimensions.y() -
				CENTER_HEIGHT_WINDOW));
		this.gameObjects().addGameObject(paddle);
		createWalls();
	}

	/**
	 * A method to init the ball after a fall
	 */
	private void initBall() {
		ball.setCenter(windowDimensions.mult(CENTER_FACTOR));
		ball.renderer().setRenderable(ballImage);
		this.gameObjects().addGameObject(ball);
		ball.turnOffTurbo();
		Random random = new Random();
		float ballVelX = BALL_SPEED;
		float ballVelY = BALL_SPEED;
		if (random.nextBoolean()) {
			ballVelX *= -1;
		}
		if (random.nextBoolean()) {
			ballVelY *= -1;
		}
		ball.setVelocity(new Vector2(ballVelX, ballVelY));
	}

	/**
	 * A method in charge of removing game object from main object collection if are out of the window bounds
	 */
	private void removeElementsOutOfBound() {
		for (GameObject gameObject : gameObjects()) {
			if (gameObject == ball) {
				continue;
			}
			float height = gameObject.getCenter().y();
			if (height > windowDimensions.y()) {
				this.gameObjects().removeGameObject(gameObject);
			}
		}
	}

	/**
	 * The update method of the brickerGameManager, in charge of updating the game every delta time
	 *
	 * @param deltaTime The time, in seconds, that passed since the last invocation
	 *                  of this method (i.e., since the last frame). This is useful
	 *                  for either accumulating the total time that passed since some
	 *                  event, or for physics integration (i.e., multiply this by
	 *                  the acceleration to get an estimate of the added velocity or
	 *                  by the velocity to get an estimate of the difference in position).
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (remainingLives != lifeHandler.getLives()) {
			remainingLives = lifeHandler.getLives();
		}
		removeElementsOutOfBound();
		if (brickCounter.value() == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
			playAgain(WIN_MESSAGE);
		} else if (remainingLives == 0) {
			playAgain(LOSE_MESSAGE);

		} else {
			if (ball != null) {
				checkIfLost();
			}
		}
		removeElementsOutOfBound();
	}

	/**
	 * Checks if a ball falls and go out of the window bounds
	 */
	private void checkIfLost() {
		float ballHeight = ball.getCenter().y();
		if (ballHeight > windowDimensions.y()) {
			remainingLives--;
			lifeHandler.setLives(remainingLives);
			initBall();
		}
	}

	/**
	 * In charge of asking the player if he wants to play another game
	 *
	 * @param message - the message for the player - if won or lost
	 */
	private void playAgain(String message) {
		if (windowController.openYesNoDialog(message)) {
			remainingLives = DEFAULT_LIVES;
			windowController.resetGame();
			brickCounter = new Counter(0);
			initBall();

		} else {
			windowController.closeWindow();
		}
	}

	/**
	 * The main function
	 *
	 * @param args - args for the amount of bricks
	 */
	public static void main(String[] args) {
		BrickerGameManager gameManager;
		if (args.length == 2) {
			int rows = Integer.parseInt(args[0]);
			int cols = Integer.parseInt(args[1]);
			gameManager = new BrickerGameManager(BRICKER, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT), rows,
					cols);
		} else {
			gameManager = new BrickerGameManager(BRICKER, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT));
		}
		gameManager.run();  // run game

	}
}
