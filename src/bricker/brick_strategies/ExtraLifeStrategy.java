package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A collision strategy that spawns a falling heart which gives the player an extra life.
 * If the player already has the maximum number of lives, the heart is not spawned.
 * @author - roni.shpitzer,shukka
 */

public class ExtraLifeStrategy extends BasicCollisionStrategy {

	private static final String HEART_IMG_PATH = "assets/heart.png";
	private static final float HEART_SIZE = 20f;
	private static final int FALL_SPEED = 100;
	private static final int MAX_LIVES = 4;
	private final GameObjectCollection gameObjectCollection;
	private final ImageReader imageReader;
	private BrickerGameManager brickerGameManager;


	/**
	 * Constructor for a new ExtraLifeStrategy.
	 *
	 * @param gameObjectCollection - The game object collection to which the heart will be added.
	 * @param imageReader          - ImageReader used to read the heart image asset.
	 * @param brickCounter         - Counter tracking the number of remaining bricks.
	 * @param brickerGameManager   - Reference to the main game manager, used to access life count
	 *                             and life handler.
	 */
	public ExtraLifeStrategy(GameObjectCollection gameObjectCollection, ImageReader imageReader,
							 Counter brickCounter, BrickerGameManager brickerGameManager) {
		super(gameObjectCollection, brickCounter);
		this.gameObjectCollection = gameObjectCollection;
		this.imageReader = imageReader;
		this.brickerGameManager = brickerGameManager;
	}

	/**
	 * Creates and adds a falling heart game object to the game.
	 * The heart falls straight down and can be collected by the player for an extra life.
	 *
	 * @param topLeftCorner - The position at which the heart should appear.
	 */
	private void createFallingHeart(Vector2 topLeftCorner) {
		Renderable heartImage = imageReader.readImage(HEART_IMG_PATH, true);
		Heart heart = new Heart(topLeftCorner, new Vector2(HEART_SIZE, HEART_SIZE), heartImage,
				brickerGameManager.getLifeHandler());
		heart.setVelocity(Vector2.DOWN.mult(FALL_SPEED));
		gameObjectCollection.addGameObject(heart);
	}

	/**
	 * Called when a brick collides with another game object.
	 * If the player has fewer than the maximum number of lives, a falling heart is spawned at
	 * the brick's location. Otherwise, the brick is simply removed.
	 *
	 * @param gameObject1 - The brick game object.
	 * @param gameObject2 - The game object that collided with the brick.
	 */
	@Override
	public void onCollision(GameObject gameObject1, GameObject gameObject2) {
		super.onCollision(gameObject1, gameObject2);
		if (brickerGameManager.getLives() >= MAX_LIVES) {
			gameObjectCollection.removeGameObject(gameObject1);
			return;
		}
		Vector2 topLeftCorner = gameObject1.getCenter();
		gameObjectCollection.removeGameObject(gameObject1);
		createFallingHeart(topLeftCorner);
	}
}