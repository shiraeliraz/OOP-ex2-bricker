package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A collision strategy that enables turbo mode on the ball if it is not already in turbo mode.
 * Turbo mode makes the ball faster and changes its image.
 * @author - roni.shpitzer,shukka
 */
public class TurboCollisionStrategy extends BasicCollisionStrategy {
	private final Ball ball;
	/** Tracks the number of bricks remaining in the game. */
	protected Counter brickCounter;

	/**
	 * Constructor for a new TurboCollisionStrategy.
	 *
	 * @param gameObjectCollection - The collection of game objects in the game.
	 * @param brickCounter         - Counter tracking the number of remaining bricks.
	 * @param ball                 - The ball that will be put into turbo mode upon collision.
	 */
	public TurboCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter,
								  Ball ball) {
		super(gameObjectCollection, brickCounter);
		this.ball = ball;
		this.brickCounter = brickCounter;
	}


	/**
	 * Handles a collision between two game objects.
	 * If the ball collides with a brick and is not already in turbo mode, turbo mode is activated.
	 *
	 * @param gameObject1 The brick that was hit.
	 * @param gameObject2 The object that hit the brick.
	 */
	@Override
	public void onCollision(GameObject gameObject1, GameObject gameObject2) {
		super.onCollision(gameObject1, gameObject2);
		if (gameObject2 != ball) {
			return;
		}
		if (!ball.getTurbo()) {
			ball.turnOnTurbo();
		}
	}
}