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
    private static final int GAP_WIDTH = 4;
    private static final int BRICK_HEIGHT = 15;
    private static final int WALL_WIDTH = 5;
    private int numberOfRows = 7;
    private int numberOfColumns = 8;
    private Vector2 windowDimensions;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numberOfRows, int numberOfColumns) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
    }

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;

    }

    public void createWalls(Vector2 windowDimensions) {
        Color pink = new Color(225, 120, 140);
        Renderable rectangle = new RectangleRenderable(pink);
        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(5, windowDimensions.y()), rectangle);
        this.gameObjects().addGameObject(leftWall);

        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() - WALL_WIDTH, 0), new Vector2(WALL_WIDTH, windowDimensions.y()), rectangle);
        this.gameObjects().addGameObject(rightWall);

        GameObject ceiling = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), (float)WALL_WIDTH), rectangle);
        this.gameObjects().addGameObject(ceiling);

    }

    private void placeRow(ImageReader imageReader, CollisionStrategy collisionStrategy, float y) {
        float brickWidth = (windowDimensions.x() - WALL_WIDTH*2-(numberOfColumns+1)*GAP_WIDTH) / numberOfColumns;
        Vector2 topLeftCorner = new Vector2(WALL_WIDTH+GAP_WIDTH, y+WALL_WIDTH+GAP_WIDTH);
        Vector2 brickDimensions = new Vector2(brickWidth, 15);
        System.out.println("brickDimensions: " + brickDimensions);
        for (int i = 0; i < numberOfColumns; i++) {
            createSingleBrick(topLeftCorner, brickDimensions,imageReader, collisionStrategy);
            topLeftCorner = topLeftCorner.add(new Vector2(brickWidth+GAP_WIDTH, 0));
            System.out.println("Created brick " + i);
        }
    }

    private void createSingleBrick(Vector2 topLeftCorner, Vector2 brickDimensions, ImageReader imageReader, CollisionStrategy collisionStrategy) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        Brick brick = new Brick(topLeftCorner, brickDimensions, brickImage, collisionStrategy);
        this.gameObjects().addGameObject(brick);
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



        // creating a brick strategy
        GameObjectCollection gameObjectCollection = this.gameObjects();
        BasicCollisionStrategy basicCollisionStrategy = new BasicCollisionStrategy(gameObjectCollection);

        //Place all bricks
        int y = GAP_WIDTH+WALL_WIDTH;
        for (int i = 0; i < numberOfRows; i++) {
            placeRow(imageReader, basicCollisionStrategy,y);
            y+=BRICK_HEIGHT+GAP_WIDTH;
        }

    }

    public static void main(String[] args) {
        BrickerGameManager gameManager;
        if (args.length == 2) {
            int rows = Integer.parseInt(args[0]);
            int cols = Integer.parseInt(args[1]);
            gameManager = new BrickerGameManager("Bricker", new Vector2(700,500), rows, cols);
        } else {
            gameManager = new BrickerGameManager("Bricker", new Vector2(700, 500));
        }
        gameManager.run();

    }
}
