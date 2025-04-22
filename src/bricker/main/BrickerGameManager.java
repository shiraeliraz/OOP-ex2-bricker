package bricker.main;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class BrickerGameManager extends GameManager {

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
        ball.setVelocity(Vector2.DOWN.mult(100));

        //creating background
        Renderable backGroundImage =
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background =
                new GameObject(Vector2.ZERO, new Vector2(windowDimension.x(), windowDimension.y()), backGroundImage);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // creating paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(100, 15), paddleImage, inputListener);
        paddle.setCenter(new Vector2(windowDimension.x() / 2, windowDimension.y() - 30));
        this.gameObjects().addGameObject(paddle);
        createWalls(windowDimension);

    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager("Bricker", new Vector2(700,500));
        gameManager.run();

    }
}
