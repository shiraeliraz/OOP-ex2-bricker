package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;

import java.util.Random;

/**
 * A factory class for creating various collision strategy instances based on a given name or probability.
 * @author - roni.shpitzer,shukka
 */
public class StrategyFactory {
	private static final String BASIC = "basic";
	private static final String ADD_BALLS = "addBalls";
	private static final String EXTRA_LIFE = "extraLife";
	private static final String EXTRA_PADDLE = "extraPaddle";
	private static final String TURBO = "turbo";
	private static final String DOUBLE = "double";
	private final GameObjectCollection gameObjectCollection;
	private final Counter brickCounter;
	private final BrickerGameManager brickerGameManager;
	private final UserInputListener userInputListener;
	private final ImageReader imageReader;
	private final Ball ball;
	private final SoundReader soundReader;
	private final Random random = new Random();

	private static final int RANDOM_BOUND = 10;
	private static final int BASIC_INDEX = 4;
	private static final int ADD_BALLS_INDEX = 5;
	private static final int EXTRA_LIFE_INDEX = 6;
	private static final int EXTRA_PADDLE_INDEX = 7;
	private static final int TURBO_INDEX = 8;
	private static final int DOUBLE_INDEX = 9;


	/**
	 * Constructor fo a StrategyFactory with references to all game components needed to
	 * create different strategies.
	 *
	 * @param gameObjectCollection - the game object collection
	 * @param ball                 - the ball if the game
	 * @param brickCounter         - the brickCounter of the game
	 * @param brickerGameManager   - the game manager of the game
	 * @param imageReader          - an image reader for the images
	 * @param soundReader          - a sound reader for the sound of the ball
	 * @param userInputListener    - a user input listener for the extra paddle
	 */
	public StrategyFactory(GameObjectCollection gameObjectCollection,
						   Counter brickCounter, BrickerGameManager brickerGameManager,
						   UserInputListener userInputListener, ImageReader imageReader, Ball ball,
						   SoundReader soundReader) {
		this.gameObjectCollection = gameObjectCollection;
		this.brickCounter = brickCounter;
		this.brickerGameManager = brickerGameManager;
		this.userInputListener = userInputListener;
		this.imageReader = imageReader;
		this.ball = ball;
		this.soundReader = soundReader;
	}

	/**
	 * Randomly selects a collision strategy based on weighted probability.
	 * 50% chance to select the basic strategy, and equal smaller chances for others.
	 *
	 * @return - the randomly chosen Collision Strategy.
	 */
	public CollisionStrategy buildStrategyByProbability() {
		int randomCollision = random.nextInt(RANDOM_BOUND);
		String strategyName;

		if (randomCollision <= BASIC_INDEX) {
			strategyName = BASIC;
		} else if (randomCollision == ADD_BALLS_INDEX) {
			strategyName = ADD_BALLS;
		} else if (randomCollision == EXTRA_LIFE_INDEX) {
			strategyName = EXTRA_LIFE;
		} else if (randomCollision == EXTRA_PADDLE_INDEX) {
			strategyName = EXTRA_PADDLE;
		} else if (randomCollision == TURBO_INDEX) {
			strategyName = TURBO;
		} else {
			strategyName = DOUBLE;
		}

		return buildStrategy(strategyName);
	}

	/**
	 * Builds a specific collision strategy based on the given strategy name.
	 *
	 * @param strategyName - the name of the strategy to construct
	 * @return - the instance on the collisionStrategy we constructed
	 */
	public CollisionStrategy buildStrategy(String strategyName) {
		switch (strategyName) {
			case ADD_BALLS:
				return new AddBallsCollisionStrategy(gameObjectCollection, imageReader, soundReader,
						brickCounter);
			case EXTRA_LIFE:
				return new ExtraLifeStrategy(gameObjectCollection, imageReader, brickCounter,
						brickerGameManager);
			case EXTRA_PADDLE:
				return new ExtraPaddleStrategy(brickerGameManager,
						gameObjectCollection, brickCounter, userInputListener, imageReader);
			case TURBO:
				return new TurboCollisionStrategy(gameObjectCollection, brickCounter, ball);
			case DOUBLE:
				return new DoubleCollisionStrategy(this, brickCounter);
			case BASIC:
				return new BasicCollisionStrategy(gameObjectCollection, brickCounter);
		}
		return null;
	}
}