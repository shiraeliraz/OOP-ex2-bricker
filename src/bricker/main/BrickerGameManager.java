package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.util.Random;

import java.awt.*;

public class BrickerGameManager extends GameManager {


    private static final float BALL_SPEED = 150f;
    private int numberOfRows = 7;
    private int numberOfColumns = 8;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numberOfRows, int numberOfColumns) {
        super(windowTitle, windowDimensions);
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
    }

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    public void createWalls(Vector2 windowDimensions) {
        Color pink = new Color(225, 120, 140);
        Renderable rectangle = new RectangleRenderable(pink);
        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(5, windowDimensions.y()), rectangle);
        this.gameObjects().addGameObject(leftWall);

        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() - 5, 0), new Vector2(5, windowDimensions.y()), rectangle);
        this.gameObjects().addGameObject(rightWall);

        GameObject ceiling = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), 5), rectangle);
        this.gameObjects().addGameObject(ceiling);

    }

    private void placeRow() {

    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        // creating ball
        Renderable ballImage =
        imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        Ball ball = new Ball(Vector2.ZERO, new Vector2(20,20), ballImage , collisionSound);
        Vector2 windowDimension = windowController.getWindowDimensions();
        ball.setCenter(windowDimension.mult(0.5f));
        this.gameObjects().addGameObject(ball);
//        ball.setVelocity(Vector2.DOWN.mult());
        Random random = new Random();
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        if (random.nextBoolean()) {
            ballVelX *= -1;
        }
        if (random.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        //creating background
        Renderable backGroundImage =
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background =
                new GameObject(Vector2.ZERO, new Vector2(windowDimension.x(), windowDimension.y()), backGroundImage);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // creating paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(100, 15), paddleImage, inputListener, windowDimension);
        paddle.setCenter(new Vector2(windowDimension.x() / 2, windowDimension.y() - 30));
        this.gameObjects().addGameObject(paddle);
        createWalls(windowDimension);



        // creating a brick
        GameObjectCollection gameObjectCollection = this.gameObjects();
        BasicCollisionStrategy basicCollisionStrategy = new BasicCollisionStrategy(gameObjectCollection);
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        Brick brick = new Brick(Vector2.ZERO.add(new Vector2(7, 7)), new Vector2(windowDimension.x() - 30, 15f), brickImage, basicCollisionStrategy);
        this.gameObjects().addGameObject(brick);

    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager("Bricker", new Vector2(700,500));
        gameManager.run();

    }
}
