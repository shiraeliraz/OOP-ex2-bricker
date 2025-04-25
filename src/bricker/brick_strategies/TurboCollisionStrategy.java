package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class TurboCollisionStrategy extends BasicCollisionStrategy{
//    private final GameObjectCollection gameObjectCollection;
//    private final Counter brickCounter;
    private final Ball ball;
    private final Renderable redBallImage;
    private final float FACTOR = 1.4f;
    private final Vector2 ORIGINAL_VELOCITY;
    private final Renderable ORIGINAL_RENDERABLE;
    private final BrickerGameManager gameManager;
    private int turboStartCollisionCount = -1;


    public TurboCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter, Ball ball, ImageReader imageReader, BrickerGameManager gameManager) {
        super(gameObjectCollection, brickCounter);
//        this.gameObjectCollection = gameObjectCollection;
//        this.brickCounter = brickCounter;
        this.ball = ball;
        this.redBallImage = imageReader.readImage("assets/redball.png", true);
        this.ORIGINAL_VELOCITY = ball.getVelocity();
        this.ORIGINAL_RENDERABLE = ball.renderer().getRenderable();
        this.gameManager = gameManager;
    }

    private void resetBall() {
        ball.renderer().setRenderable(ORIGINAL_RENDERABLE);
        ball.setVelocity(ORIGINAL_VELOCITY);
        ball.setTag("Basic Mode");
        turboStartCollisionCount = -1;
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        if (gameObject2 != ball) {
            return;
        }
        if (!ball.getTag().equals("Turbo Mode")) {
            ball.setVelocity(ball.getVelocity().mult(1.4f));
            ball.renderer().setRenderable(redBallImage);
            ball.setTag("Turbo Mode");
//            gameManager.setTurboCollisionCounter(ball.getCollisionCounter());
        }
    }
}
