package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A collision strategy that, when triggered, adds two additional balls (pucks) into the game.
 * These pucks are visually different and move in a random initial direction.
 * This class decorates inherits from BasicCollisionStrategy.
 * @author - roni.shpitzer,shukka
 */
public class AddBallsCollisionStrategy extends BasicCollisionStrategy {

	/** Path to the image asset used for the puck ball. */
	public static final String ASSETS_MOCK_BALL_PNG = "assets/mockBall.png";

	/** Path to the sound asset played when the puck collides. */
	public static final String ASSETS_BLOP_WAV = "assets/blop.wav";

	private final GameObjectCollection gameObjectCollection;
	private final ImageReader imageReader;
	private final SoundReader soundReader;
	private final Random random = new Random();
	private static final int BALL_SPEED = 200;
	private static final Vector2 PUCK_DIMENSIONS = new Vector2(15, 15);


	/**
	 * Constructor for the AddBallsCollisionStrategy.
	 *
	 * @param gameObjectCollection - The game object collection to which new balls will be added.
	 * @param imageReader          - ImageReader used to load the puck's image.
	 * @param soundReader          - SoundReader used to load the puck's collision sound.
	 * @param counter              - A counter tracking the number of bricks in the game.
	 */
	public AddBallsCollisionStrategy(GameObjectCollection gameObjectCollection,
									 ImageReader imageReader, SoundReader soundReader, Counter counter) {
		super(gameObjectCollection, counter);
		this.gameObjectCollection = gameObjectCollection;
		this.imageReader = imageReader;
		this.soundReader = soundReader;
	}


	/**
	 * Creates a puck at the given position, with a random direction and speed.
	 *
	 * @param topLeftCorner - The location where the puck will be created.
	 */
	private void createPuck(Vector2 topLeftCorner) {
		Renderable puckImage = imageReader.readImage(ASSETS_MOCK_BALL_PNG, true);
		Sound collisionSound = soundReader.readSound(ASSETS_BLOP_WAV);
		Ball ball = new Ball(topLeftCorner, PUCK_DIMENSIONS, puckImage, collisionSound, null);
		gameObjectCollection.addGameObject(ball);
		initPuck(ball);
	}


	/**
	 * Initializes the given ball (puck) with a random velocity.
	 *
	 * @param ball - The puck to initialize.
	 */
	private void initPuck(Ball ball) {
		double angle = random.nextFloat() * Math.PI;
		float velocityX = (float) Math.cos(angle) * BALL_SPEED;
		float velocityY = (float) Math.sin(angle) * BALL_SPEED;
		ball.setVelocity(new Vector2(velocityX, velocityY));
	}


	/**
	 * Handles a collision by performing the basic collision behavior and spawning two new balls.
	 *
	 * @param gameObject1 - The brick that was hit.
	 * @param gameObject2 - The object that hit the brick.
	 */
	@Override
	public void onCollision(GameObject gameObject1, GameObject gameObject2) {
		super.onCollision(gameObject1, gameObject2);
		Vector2 puckTopLeftCorner = gameObject1.getCenter();
		createPuck(puckTopLeftCorner);
		createPuck(puckTopLeftCorner);
	}
}