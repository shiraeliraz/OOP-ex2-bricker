package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class BasicCollisionStrategy implements CollisionStrategy {
    private final String COLLISION_MESSAGE = "collision with brick detected";
    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;

    public BasicCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
    }
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        System.out.println(COLLISION_MESSAGE);
        if (gameObjectCollection.removeGameObject(gameObject1)) {
            brickCounter.decrement();
        }
    }
}
