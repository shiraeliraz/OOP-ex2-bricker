package bricker.brick_strategies;

import danogl.GameObject;

public interface CollisionStrategy {
    public void onCollision(GameObject gameObject1, GameObject gameObject2);
}
