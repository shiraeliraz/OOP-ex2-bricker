package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class LifeHandler {
    private GameObject[] hearts;
    private GameObjectCollection gameObjectCollection;
    private final ImageReader imageReader;
    private int lives;
    private float height;

    public LifeHandler(GameObjectCollection gameObjectCollection, int lives, ImageReader imageReader, float height) {
        hearts = new GameObject[lives];
        this.gameObjectCollection = gameObjectCollection;
        this.imageReader = imageReader;
        this.lives = lives;
        this.height = height;


    }

    private Color colorChooser() {
        if (lives >= 3) {
            return Color.GREEN;
        } else if (lives == 2) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }

    private void createNumber() {
        TextRenderable textRenderable = new TextRenderable(Integer.toString(lives));
        textRenderable.setString(Integer.toString(lives));
        textRenderable.setColor(colorChooser());
        GameObject number = new GameObject(new Vector2(10,  - 30), new Vector2(20, 20), textRenderable);
        this.gameObjectCollection.addGameObject(number); //
    }

    private void createSingleHeart(Vector2 topLeftCorner, Vector2 heartDimensions) {
        Renderable heartImage = imageReader.readImage("assets/heart.png", false);
        GameObject heart = new GameObject(topLeftCorner, heartDimensions, heartImage);
        gameObjectCollection.addGameObject(heart);
    }

    public void createAllHearts() {
//        Vector2 topLeftCorner =

        for (int i = 0; i < lives; i++) {
//            hearts[i] = createSingleHeart();
        }
    }
}
