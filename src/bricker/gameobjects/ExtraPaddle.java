package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class for handling the extra paddle of the Extra Paddle Collision Strategy
 * @author - roni.shpitzer,shukka
 */
public class ExtraPaddle extends Paddle {
	private static final String EXTRA_PADDLE = "ExtraPaddle";
	private static final int MAX_EXTRA_PADDLE_COLLISION = 4;
	//    static boolean isAlreadyExists = false;
	private final GameObjectCollection gameObjectCollection;
	private int collisionCounter = 0;


	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner        Position of the object, in window coordinates (pixels).
	 *                             Note that (0,0) is the top-left corner of the window.
	 * @param dimensions           Width and height in window coordinates.
	 * @param renderable           The renderable representing the object. Can be null, in which case
	 *                             the GameObject will not be rendered.
	 * @param inputListener        - input listener for the keyboard input
	 * @param windowDimensions     - the window's dimensions
	 * @param gameObjectCollection - the game object collection of the game
	 */
	public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener
			inputListener, Vector2 windowDimensions, GameObjectCollection gameObjectCollection) {
		super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
		this.gameObjectCollection = gameObjectCollection;
		this.setTag(EXTRA_PADDLE);
	}

	/**
	 * A method that resets the extra paddle after hit 4 times
	 */
	private void resetExtraPaddle() {
		collisionCounter = 0;
		gameObjectCollection.removeGameObject(this);
	}

	/**
	 * Handles the collisions of the paddle with other game objects
	 *
	 * @param other     The GameObject with which a collision occurred.
	 * @param collision Information regarding this collision.
	 *                  A reasonable elastic behavior can be achieved with:
	 *                  setVelocity(getVelocity().flipped(collision.getNormal()));
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		collisionCounter++;
		if (collisionCounter == MAX_EXTRA_PADDLE_COLLISION) {
			resetExtraPaddle();
		}
	}

}
