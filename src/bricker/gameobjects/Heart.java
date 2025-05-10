package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class for the Heart object of the game\
 * @author - roni.shpitzer,shukka
 */
public class Heart extends GameObject {
	private static final String HEART = "heart";
	private static final String PADDLE = "Paddle";
	private final LifeHandler lifeHandler;

	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner Position of the object, in window coordinates (pixels).
	 *                      Note that (0,0) is the top-left corner of the window.
	 * @param dimensions    Width and height in window coordinates.
	 * @param renderable    The renderable representing the object. Can be null, in which case
	 *                      the GameObject will not be rendered.
	 * @param lifeHandler   the life handler of the game
	 */
	public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				 LifeHandler lifeHandler) {
		super(topLeftCorner, dimensions, renderable);
		this.lifeHandler = lifeHandler;
		this.setTag(HEART);
	}

	/**
	 * GameObject
	 * Should this object be allowed to collide the specified other object.
	 *
	 * @param other The other GameObject.
	 * @return - True if so, False if isn't.
	 */
	@Override
	public boolean shouldCollideWith(GameObject other) {
		return other instanceof Paddle;
	}

	/**
	 * A method for handling what happens when the object collides with other objects
	 *
	 * @param other     The GameObject with which a collision occurred.
	 * @param collision Information regarding this collision.
	 *                  A reasonable elastic behavior can be achieved with:
	 *                  setVelocity(getVelocity().flipped(collision.getNormal()));
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if (other.getTag().equals(PADDLE)) {
			lifeHandler.setLives(lifeHandler.getLives() + 1);
		}
	}
}
