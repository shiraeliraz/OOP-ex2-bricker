package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class for handling the lives of the game
 * @author - roni.shpitzer,shukka
 */
public class LifeHandler {
	private static final int GREEN_LIVES_AMOUNT = 3;
	private static final int YELLOW_LIVES_AMOUNT = 2;
	private static final int NUMBER_WIDTH = 10;
	private static final int INIT_HEIGHT = 30;
	private static final int NUMBER_HEART_SIZE = 20;
	private static final String ASSETS_HEART_PNG = "assets/heart.png";
	private static final int HEART_BEGIN_X = 40;
	private GameObject[] hearts;
	private final GameObjectCollection gameObjectCollection;
	private final ImageReader imageReader;
	private int lives;
	private float height;
	private GameObject number;
	private static final int MAX_LIVES = 4;

	/**
	 * A constructor if the life handler
	 *
	 * @param gameObjectCollection - the game objects collection
	 * @param lives                - amount of lives
	 * @param imageReader          - the image reader for the heart image
	 * @param height               - the height of the hearts
	 */
	public LifeHandler(GameObjectCollection gameObjectCollection, int lives, ImageReader imageReader,
					   float height) {
		hearts = new GameObject[lives];
		this.gameObjectCollection = gameObjectCollection;
		this.imageReader = imageReader;
		this.lives = lives;
		this.height = height;
	}

	/**
	 * A method in charge of choosing the color of the text that shows the amount of remaining lives for
	 * the game
	 *
	 * @return - the color for the text
	 */
	private Color colorChooser() {
		if (lives >= GREEN_LIVES_AMOUNT) {
			return Color.GREEN;
		} else if (lives == YELLOW_LIVES_AMOUNT) {
			return Color.YELLOW;
		} else {
			return Color.RED;
		}
	}

	/**
	 * Creates the text that shows the amount of remaining lives for the game
	 */
	private void createNumber() {
		TextRenderable textRenderable = new TextRenderable(Integer.toString(lives));
		textRenderable.setString(Integer.toString(lives));
		textRenderable.setColor(colorChooser());
		GameObject number = new GameObject(new Vector2(NUMBER_WIDTH, height - INIT_HEIGHT),
				new Vector2(NUMBER_HEART_SIZE, NUMBER_HEART_SIZE), textRenderable);
		this.gameObjectCollection.addGameObject(number, Layer.UI);
		this.number = number;
	}

	/**
	 * A method in charge of creating the heart object
	 *
	 * @param topLeftCorner   - the top left corner of the height
	 * @param heartDimensions - the heart dimensions
	 * @return - the Heart object
	 */
	private GameObject createSingleHeart(Vector2 topLeftCorner, Vector2 heartDimensions) {
		Renderable heartImage = imageReader.readImage(ASSETS_HEART_PNG, true);
		Heart heart = new Heart(topLeftCorner, heartDimensions, heartImage, this);
		gameObjectCollection.addGameObject(heart, Layer.UI);
		return heart;

	}

	/**
	 * Create all the hearts of the game
	 */
	public void createAllHearts() {
		Vector2 topLeftCorner = new Vector2(HEART_BEGIN_X, height - INIT_HEIGHT);
		Vector2 heartDimensions = new Vector2(NUMBER_HEART_SIZE, NUMBER_HEART_SIZE);
		for (int i = 0; i < lives; i++) {
			hearts[i] = createSingleHeart(topLeftCorner, heartDimensions);
			topLeftCorner = topLeftCorner.add(new Vector2(INIT_HEIGHT, 0));
		}
	}

	/**
	 * Delete all the hearts in the game
	 */
	private void deleteAllHearts() {
		for (int i = 0; i < lives; i++) {
			gameObjectCollection.removeGameObject(hearts[i], Layer.UI);
		}
	}

	/**
	 * Set the amount of lives and shows it on the screen
	 *
	 * @param lives - the amount of lives
	 */
	public void setLives(int lives) {
		if (lives > MAX_LIVES) {
			return;
		}
		gameObjectCollection.removeGameObject(number, Layer.UI);
		deleteAllHearts();
		this.lives = lives;
		this.hearts = new GameObject[lives];
		createNumber();
		createAllHearts();

	}

	/**
	 * A getter for the amount of lives
	 *
	 * @return - the amount of lives
	 */
	public int getLives() {
		return lives;
	}
}
