package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

public class BasicCollisionStrategy implements CollisionStrategy {
    private final String COLLISION_MESSAGE = "collision with brick detected";
    private final GameObjectCollection gameObjectCollection;

    public BasicCollisionStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjectCollection = gameObjectCollection;
    }
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        System.out.println(COLLISION_MESSAGE);
        gameObjectCollection.removeGameObject(gameObject1);
    }
}
