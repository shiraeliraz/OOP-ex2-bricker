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
    private final Ball ball;
    protected Counter brickCounter;

    public TurboCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter, Ball ball) {
        super(gameObjectCollection, brickCounter);
        this.ball = ball;
        this.brickCounter = brickCounter;
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        if (gameObject2 != ball) {
            return;
        }
        if (!ball.getTurbo()) {
//            System.out.println("turbo mode: " + brickCounter.value());
            ball.setTurbo(true);
            ball.turnOnTurbo();
        }
    }
}
