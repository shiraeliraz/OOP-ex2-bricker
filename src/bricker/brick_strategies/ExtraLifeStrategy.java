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

public class ExtraLifeStrategy implements CollisionStrategy {

    private static final float HEART_SIZE = 20f;
    private static final int FALL_SPEED = 100;
    private static final int MAX_LIVES = 4;
    private final GameObjectCollection gameObjectCollection;
    private final ImageReader imageReader;
    private Counter brickCounter;
    private BrickerGameManager brickerGameManager;
    private Heart[] hearts;

    public ExtraLifeStrategy(GameObjectCollection gameObjectCollection, ImageReader imageReader,
                             Counter brickCounter, BrickerGameManager brickerGameManager) {
        this.gameObjectCollection = gameObjectCollection;
        this.imageReader = imageReader;
        this.brickCounter = brickCounter;
        this.brickerGameManager = brickerGameManager;
        this.hearts = new Heart[MAX_LIVES];
    }

    private void createFallingHeart(Vector2 topLeftCorner) {
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        Heart heart = new Heart(topLeftCorner, new Vector2(HEART_SIZE, HEART_SIZE), heartImage, brickerGameManager.getLifeHandler());
        heart.setVelocity(Vector2.DOWN.mult(FALL_SPEED));
        gameObjectCollection.addGameObject(heart);
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        if (brickerGameManager.getLives() >= MAX_LIVES){
            gameObjectCollection.removeGameObject(gameObject1);
            return;
        }
        Vector2 topLeftCorner = gameObject1.getCenter();
        gameObjectCollection.removeGameObject(gameObject1);
        brickCounter.decrement();
        createFallingHeart(topLeftCorner);
    }
}
