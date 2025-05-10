package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The class that's in charge of handling the balls of the game
 *
 * @author - roni.shpitzer,shukka
 */
public class Ball extends GameObject {
	private static final int MAX_TURBO_COLLISIONS = 6;
	private static final float TURBO_MODE_SPEED_FACTOR = 1.40f;
	private final Sound collisionSound;
	private final Renderable redBallImage;
	private final Renderable renderable;
	private int collisionCounter = 0;
	private int turboCounter = MAX_TURBO_COLLISIONS;
	private boolean isTurbo = false;

	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner  Position of the object, in window coordinates (pixels).
	 *                       Note that (0,0) is the top-left corner of the window.
	 * @param dimensions     Width and height in window coordinates.
	 * @param renderable     The renderable representing the object. Can be null, in which case
	 *                       the GameObject will not be rendered.
	 * @param redBallImage   The image used when the ball is in turbo mode.
	 * @param collisionSound The sound to be played on collision.
	 */
	public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				Sound collisionSound, Renderable redBallImage) {
		super(topLeftCorner, dimensions, renderable);
		this.collisionSound = collisionSound;
		this.redBallImage = redBallImage;
		this.renderable = renderable;

	}

	/**
	 * A public method in charge of turning if turbo mode, also called by the brickerGameManager
	 */
	public void turnOffTurbo() {
		this.setVelocity(this.getVelocity().mult(1 / TURBO_MODE_SPEED_FACTOR));
		turboCounter = MAX_TURBO_COLLISIONS;
		this.renderer().setRenderable(renderable);
		isTurbo = false;
	}

	/**
	 * A getter for the is on turbo mode field
	 *
	 * @return - true if in turbo mode, false if in basic mode
	 */
	public boolean getTurbo() {
		return isTurbo;
	}

	/**
	 * A public method for turning on the turbo mode. Called by the Turbo /collision Strategy.
	 */
	public void turnOnTurbo() {
		turboCounter = MAX_TURBO_COLLISIONS;
		this.setVelocity(this.getVelocity().mult(TURBO_MODE_SPEED_FACTOR));
		this.renderer().setRenderable(redBallImage);
		isTurbo = true;
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
		if (isTurbo) {
			turboCounter--;
			if (turboCounter == 0) {
				turnOffTurbo();
				this.renderer().setRenderable(renderable);
			}
		}
		Vector2 newVel = getVelocity().flipped(collision.getNormal());
		setVelocity(newVel);
		collisionSound.play();
		collisionCounter++;

	}

	/**
	 * A getter for the ball's collision counter
	 *
	 * @return - the amount of collisions of the ball
	 */
	public int getCollisionCounter() {
		return collisionCounter;
	}
}
