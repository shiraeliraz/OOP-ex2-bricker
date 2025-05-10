package bricker.brick_strategies;

import java.util.Random;

import danogl.GameObject;
import danogl.util.Counter;

/**
 * A collision strategy that executes two or three other random collision strategies upon brick collision.
 * It randomly selects from available strategies and can include double strategies,
 * making sure to prevent infinite excessive double behaviours.
 * @author - roni.shpitzer, shukka
 */
public class DoubleCollisionStrategy implements CollisionStrategy {
	private static final String ADD_BALLS = "addBalls";
	private static final String EXTRA_LIFE = "extraLife";
	private static final String EXTRA_PADDLE = "extraPaddle";
	private static final String TURBO = "turbo";
	private static final String DOUBLE = "double";

	private final Random random = new Random();
	private final StrategyFactory strategyFactory;
	private final Counter brickCounter;
	private CollisionStrategy collision1;
	private CollisionStrategy collision2;
	private CollisionStrategy collision3 = null;

	private final String[] strategies = {ADD_BALLS, EXTRA_LIFE, EXTRA_PADDLE, TURBO, DOUBLE};

	/**
	 * Constructs a DoubleCollisionStrategy with a reference to the StrategyFactory and brick counter.
	 * Randomly selects and initializes 2 or 3 collision strategies.
	 *
	 * @param strategyFactory - A factory used to create instances of specific collision strategies.
	 * @param brickCounter    - Counter tracking the number of remaining bricks.
	 */
	public DoubleCollisionStrategy(StrategyFactory strategyFactory, Counter brickCounter) {
		this.strategyFactory = strategyFactory;
		this.brickCounter = brickCounter;
		chooseRandomStrategies();
	}

	/**
	 * Randomly selects 2 or 3 different strategies from the list,
	 * limiting how many "double" strategies are selected.
	 */
	private void chooseRandomStrategies() {
		int firstRand = random.nextInt(strategies.length);
		int secondRand;
		int thirdRand = -1;
		if (strategies[firstRand].equals(DOUBLE)) {
			firstRand = random.nextInt(strategies.length - 1);
			secondRand = random.nextInt(strategies.length - 1);
			thirdRand = random.nextInt(strategies.length - 1);
		} else {
			secondRand = random.nextInt(strategies.length);
			if (strategies[secondRand].equals(DOUBLE)) {
				secondRand = random.nextInt(strategies.length - 1);
				thirdRand = random.nextInt(strategies.length - 1);
			}
		}
		collision1 = strategyFactory.buildStrategy(strategies[firstRand]);
		collision2 = strategyFactory.buildStrategy(strategies[secondRand]);
		if (thirdRand > -1) {
			collision3 = strategyFactory.buildStrategy(strategies[thirdRand]);
		}
	}

	/**
	 * Executes the selected collision strategies when a collision occurs.
	 *
	 * @param gameObject1 - The first game object involved in the collision.
	 * @param gameObject2 - The second game object involved in the collision.
	 */
	@Override
	public void onCollision(GameObject gameObject1, GameObject gameObject2) {
		collision1.onCollision(gameObject1, gameObject2);
		collision2.onCollision(gameObject1, gameObject2);
		if (collision3 != null) {
			collision3.onCollision(gameObject1, gameObject2);
		}
	}
}
