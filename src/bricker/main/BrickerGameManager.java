package bricker.main;

import bricker.gameobjects.Ball;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
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
        Renderable ballImage =
        imageReader.readImage("assets/ball.png", true);

        // creating ball
        Ball ball = new Ball(Vector2.ZERO, new Vector2(20,20), ballImage );
        Vector2 windowDimension = windowController.getWindowDimensions();
        ball.setCenter(windowDimension.mult(0.5f));
        this.gameObjects().addGameObject(ball);
        ball.setVelocity(Vector2.DOWN.mult(100));

        // creating paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new GameObject(Vector2.ZERO, new Vector2(100, 15), paddleImage);
        paddle.setCenter(new Vector2(windowDimension.x() / 2, windowDimension.y() - 30));
        this.gameObjects().addGameObject(paddle);
        createWalls(windowDimension);

    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager("Bricker", new Vector2(700,500));
        gameManager.run();

    }
}
