package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * A class for handling the main paddle of the game
 * @author - roni.shpitzer,shukka
 */
public class Paddle extends GameObject {

	private static final float MOVEMENT_SPEED = 300f;
	private final static String PADDLE_TAG = "Paddle";
	private static final int WALL_WIDTH = 5;
	private final UserInputListener inputListener;
	private final Vector2 windowDimensions;

	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner    Position of the object, in window coordinates (pixels).
	 *                         Note that (0,0) is the top-left corner of the window.
	 * @param dimensions       Width and height in window coordinates.
	 * @param renderable       The renderable representing the object. Can be null, in which case
	 *                         the GameObject will not be rendered.
	 * @param windowDimensions - the window's dimension
	 * @param inputListener    - the input listener for the keyboard
	 */
	public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				  UserInputListener inputListener, Vector2 windowDimensions) {
		super(topLeftCorner, dimensions, renderable);
		this.inputListener = inputListener;
		this.windowDimensions = windowDimensions;
		this.setTag(PADDLE_TAG);
	}

	/**
	 * Updating the paddle every deltaTime
	 *
	 * @param deltaTime The time elapsed, in seconds, since the last frame. Can
	 *                  be used to determine a new position/velocity by multiplying
	 *                  this delta with the velocity/acceleration respectively
	 *                  and adding to the position/velocity:
	 *                  velocity += deltaTime*acceleration
	 *                  pos += deltaTime*velocity
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		Vector2 movementDir = Vector2.ZERO;
		if (getTopLeftCorner().x() <= WALL_WIDTH + 1) {
			setTopLeftCorner(new Vector2(WALL_WIDTH + 1, this.getTopLeftCorner().y()));
		}

		if (getTopLeftCorner().x() >= windowDimensions.x() - this.getDimensions().x() - WALL_WIDTH - 1) {
			setTopLeftCorner(new Vector2(windowDimensions.x() - this.getDimensions().x() - WALL_WIDTH - 1,
					this.getTopLeftCorner().y()));

		}

		if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
			movementDir = movementDir.add(Vector2.LEFT);
		}
		if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
			movementDir = movementDir.add(Vector2.RIGHT);
		}
		setVelocity(movementDir.mult(MOVEMENT_SPEED));
	}
}
