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


    public TurboCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter, Ball ball) {
        super(gameObjectCollection, brickCounter);
//        this.gameObjectCollection = gameObjectCollection;
//        this.brickCounter = brickCounter;
        this.ball = ball;
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        if (gameObject2 != ball) {
            return;
        }
        if (!ball.getTag().equals("Turbo Mode")) {
            ball.turnOnTurbo();
        }
    }
}
