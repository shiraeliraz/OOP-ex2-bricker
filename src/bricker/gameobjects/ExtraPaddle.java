package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddle extends Paddle{
//    static boolean isAlreadyExists = false;
    private final GameObjectCollection gameObjectCollection;
    private int collisionCounter = 0;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener
     * @param windowDimensions
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener, Vector2 windowDimensions, GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
        this.gameObjectCollection = gameObjectCollection;
    }

    private void resetExtraPaddle() {
        collisionCounter = 0;
        gameObjectCollection.removeGameObject(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (collisionCounter == 4) {
            resetExtraPaddle();
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter++;
    }

}
