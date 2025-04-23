package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
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
    private GameObject number;

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
        GameObject number = new GameObject(new Vector2(10,  height- 30), new Vector2(20, 20), textRenderable);
        this.gameObjectCollection.addGameObject(number, Layer.UI);
        this.number = number;
    }

    private GameObject createSingleHeart(Vector2 topLeftCorner, Vector2 heartDimensions) {
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        GameObject heart = new GameObject(topLeftCorner, heartDimensions, heartImage);
        gameObjectCollection.addGameObject(heart, Layer.UI);
        return heart;

    }

    public void createAllHearts() {
        Vector2 topLeftCorner = new Vector2(40, height-30);
        Vector2 heartDimensions = new Vector2(20, 20);
        for (int i = 0; i < lives; i++) {
            hearts[i] = createSingleHeart(topLeftCorner, heartDimensions);
            topLeftCorner = topLeftCorner.add(new Vector2(30,0));
        }
    }

    private void deleteAllHearts() {
        for (int i = 0; i < lives; i++) {
            gameObjectCollection.removeGameObject(hearts[i], Layer.UI);
        }
    }

    public void setLives(int lives) {
        gameObjectCollection.removeGameObject(number,Layer.UI);
        deleteAllHearts();
        this.lives = lives;
        createNumber();
        createAllHearts();

    }
}
