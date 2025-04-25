package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

public class AddBallsCollisionStrategy extends BasicCollisionStrategy{

    private final GameObjectCollection gameObjectCollection;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Counter counter;
    private final Random random = new Random();
    private final int BALL_SPEED = 200; // TODO: maybe same speed as reg ball?
    private final Vector2 puckDimensions = new Vector2(15, 15);

    public AddBallsCollisionStrategy(GameObjectCollection gameObjectCollection, ImageReader imageReader, SoundReader soundReader, Counter counter) {
        super(gameObjectCollection, counter);
        this.gameObjectCollection = gameObjectCollection;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.counter = counter;
    }

    private void createPuck(Vector2 topLeftCorner) {
        Renderable puckImage = imageReader.readImage("assets/mockBall.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        Ball ball = new Ball(topLeftCorner, puckDimensions, puckImage, collisionSound);
        gameObjectCollection.addGameObject(ball);
        initPuck(ball);
    }

    private void initPuck(Ball ball) {
        double angle = random.nextFloat() * Math.PI;
        float velocityX = (float)Math.cos(angle) * BALL_SPEED;
        float velocityY = (float)Math.sin(angle) * BALL_SPEED;
        ball.setVelocity(new Vector2(velocityX, velocityY));
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        Vector2 puckTopLeftCorner = gameObject1.getCenter();
        createPuck(puckTopLeftCorner);
        createPuck(puckTopLeftCorner);
    }
}
