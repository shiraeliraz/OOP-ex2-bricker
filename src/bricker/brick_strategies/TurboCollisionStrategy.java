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


    public TurboCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter, Ball ball) {
        super(gameObjectCollection, brickCounter);
        this.ball = ball;
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        if (gameObject2 != ball) {
            return;
        }
        if (!ball.getTurbo()) {
            System.out.println("Collision with a brick turbo mode");
            ball.setTurbo(true);
            ball.turnOnTurbo();
        }
    }
}
