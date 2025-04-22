package bricker.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class BrickerGameManager extends GameManager {

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Renderable ballImage =
        imageReader.readImage("assets/ball.png", true);

        GameObject ball = new GameObject(Vector2.ZERO, new Vector2(20,20), ballImage );
        Vector2 windowDimension = windowController.getWindowDimensions();
        ball.setCenter(windowDimension.mult(0.5f));
        this.gameObjects().addGameObject(ball);
        ball.setVelocity(Vector2.DOWN.mult(100));
    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager("Bricker", new Vector2(700,500));
        gameManager.run();

    }
}
