package bricker.brick_strategies;

import bricker.gameobjects.ExtraPaddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddleStrategy extends BasicCollisionStrategy{
    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;
    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);

    public ExtraPaddleStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter, Vector2 windowDimensions, UserInputListener inputListener, ImageReader imageReader) {
        super(gameObjectCollection, brickCounter);
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;
        this.imageReader = imageReader;
    }

    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        Vector2 extraPaddleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        ExtraPaddle extraPaddle = new ExtraPaddle(Vector2.ZERO, PADDLE_DIMENSIONS, paddleImage, inputListener, windowDimensions, gameObjectCollection);

    }
}
