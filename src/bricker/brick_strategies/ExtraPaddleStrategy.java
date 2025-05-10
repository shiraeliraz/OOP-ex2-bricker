package bricker.brick_strategies;

import bricker.gameobjects.ExtraPaddle;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A collision strategy that adds an extra paddle to the game upon a brick collision.
 * Only one extra paddle can exist at a time.
 * @author - roni.shpitzer,shukka
 */
public class ExtraPaddleStrategy extends BasicCollisionStrategy {
	private static final String ASSETS_PADDLE_PNG = "assets/paddle.png";
	private final GameObjectCollection gameObjectCollection;
	private final Counter brickCounter;
	private final Vector2 windowDimensions;
	private final UserInputListener inputListener;
	private final ImageReader imageReader;
	private static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);
	private static ExtraPaddle extraPaddle;


	/**
	 * Constructor a new ExtraPaddleStrategy.
	 *
	 * @param brickerGameManager   - The main game manager used to retrieve window dimensions.
	 * @param gameObjectCollection - The collection of game objects in the game.
	 * @param brickCounter         - Counter tracking the number of remaining bricks.
	 * @param inputListener        - Listener for keyboard input, passed to the extra paddle.
	 * @param imageReader          - Used to read the paddle image.
	 */

	public ExtraPaddleStrategy(BrickerGameManager brickerGameManager,
							   GameObjectCollection gameObjectCollection,
							   Counter brickCounter, UserInputListener inputListener,
							   ImageReader imageReader) {
		super(gameObjectCollection, brickCounter);
		this.gameObjectCollection = gameObjectCollection;
		this.brickCounter = brickCounter;
		this.windowDimensions = brickerGameManager.getWindowDimensions();
		this.inputListener = inputListener;
		this.imageReader = imageReader;
		createExtraPaddle();
	}


	/**
	 * Checks if the extra paddle already exists in the game object collection.
	 *
	 * @return True if the extra paddle is present in the game, false otherwise.
	 */
	private boolean checkIfExtraPaddleExists() {
		for (GameObject gameObject : gameObjectCollection) {
			if (gameObject == extraPaddle) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Handles a collision between two game objects.
	 * Adds the extra paddle to the game if it does not already exist.
	 *
	 * @param gameObject1 - The brick that was hit.
	 * @param gameObject2 - The object that hit the brick.
	 */
	@Override
	public void onCollision(GameObject gameObject1, GameObject gameObject2) {
		super.onCollision(gameObject1, gameObject2);
		if (!checkIfExtraPaddleExists()) {
			gameObjectCollection.addGameObject(extraPaddle);
		}
	}

	/**
	 * Creates the extra paddle and sets its initial position at the center of the window.
	 */
	private void createExtraPaddle() {
		Vector2 extraPaddleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
		Renderable paddleImage = imageReader.readImage(ASSETS_PADDLE_PNG, true);
		ExtraPaddle extraPaddle = new ExtraPaddle(Vector2.ZERO, PADDLE_DIMENSIONS,
				paddleImage, inputListener, windowDimensions, gameObjectCollection);
		extraPaddle.setCenter(extraPaddleCenter);
		ExtraPaddleStrategy.extraPaddle = extraPaddle;
	}
}