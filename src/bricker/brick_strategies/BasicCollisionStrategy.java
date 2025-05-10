package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * A basic collision strategy that removes the brick from the game and decreases the brick counter.
 * @author - roni.shpitzer,shukka
 */

public class BasicCollisionStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjectCollection;
	private final Counter brickCounter;


	/**
	 * Constructor for the BasicCollisionStrategy.
	 *
	 * @param gameObjectCollection - The game object collection from which bricks will be removed.
	 * @param brickCounter         - A counter tracking the number of bricks in the game.
	 */
	public BasicCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter) {
		this.gameObjectCollection = gameObjectCollection;
		this.brickCounter = brickCounter;
	}

	/**
	 * Handles a collision by removing the brick from the game object collection.
	 *
	 * @param gameObject1 - The brick that was hit.
	 * @param gameObject2 - The object that hit the brick.
	 */
	@Override
	public void onCollision(GameObject gameObject1, GameObject gameObject2) {
		if (gameObjectCollection.removeGameObject(gameObject1, Layer.STATIC_OBJECTS)) {
			brickCounter.decrement();
		}
	}
}