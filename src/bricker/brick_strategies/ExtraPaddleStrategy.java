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

public class ExtraPaddleStrategy extends BasicCollisionStrategy{
    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;
    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);
    private ExtraPaddle extraPaddle;

    public ExtraPaddleStrategy(BrickerGameManager brickerGameManager, GameObjectCollection gameObjectCollection,
                               Counter brickCounter, UserInputListener inputListener, ImageReader imageReader) {
        super(gameObjectCollection, brickCounter);
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
        this.windowDimensions = brickerGameManager.getWindowDimensions();
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        createExtraPaddle();
    }

    private boolean checkIfExtraPaddleExists() {
        for (GameObject gameObject : gameObjectCollection) {
            if (gameObject == extraPaddle) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        if (!checkIfExtraPaddleExists()) {
            gameObjectCollection.addGameObject(extraPaddle);
        }
    }

    private void createExtraPaddle() {
        Vector2 extraPaddleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        ExtraPaddle extraPaddle = new ExtraPaddle(Vector2.ZERO, PADDLE_DIMENSIONS, paddleImage, inputListener, windowDimensions, gameObjectCollection);
        extraPaddle.setCenter(extraPaddleCenter);
        this.extraPaddle = extraPaddle;
        System.out.println("extra paddle: " + brickCounter.value());
    }
}
