package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Interface for defining different collision strategies that occur when a brick is hit.
 * @author - roni.shpitzer,shukka
 */

public interface CollisionStrategy {
	/**
	 * Explains what to do when a collision occurs.
	 *
	 * @param gameObject1 - The brick that was hit.
	 * @param gameObject2 - the object that hit the brick.
	 */
	void onCollision(GameObject gameObject1, GameObject gameObject2);
}