package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import java.util.Random;

public class StrategyFactory {
    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;
    private final BrickerGameManager brickerGameManager;
    private final UserInputListener userInputListener;
    private final ImageReader imageReader;
    private final Ball ball;
    private final SoundReader soundReader;
    private final Random random = new Random();

    public StrategyFactory(GameObjectCollection gameObjectCollection, Counter brickCounter, BrickerGameManager brickerGameManager, UserInputListener userInputListener, ImageReader imageReader, Ball ball, SoundReader soundReader) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
        this.brickerGameManager = brickerGameManager;
        this.userInputListener = userInputListener;
        this.imageReader = imageReader;
        this.ball = ball;
        this.soundReader = soundReader;
    }

    public CollisionStrategy buildStrategyByProbability() {
        int randomCollision = random.nextInt(10);
        String strategyName;
        if (randomCollision < 5) {
            strategyName = "basic";
        } else if (randomCollision == 5) {
            strategyName = "addBalls";
        } else if (randomCollision == 6) {
            strategyName = "extraLife";
        } else if (randomCollision == 7) {
            strategyName = "extraPaddle";
        } else if (randomCollision == 8) {
            strategyName = "turbo";
        } else {
            strategyName = "double";
        }
        System.out.println("created " + strategyName);
        return buildStrategy(strategyName);
    }

    public CollisionStrategy buildStrategy(String strategyName) {
        switch (strategyName) {
            case "addBalls":
                return new AddBallsCollisionStrategy(gameObjectCollection, imageReader, soundReader, brickCounter);
            case "extraLife":
                return new ExtraLifeStrategy(gameObjectCollection, imageReader, brickCounter, brickerGameManager);
            case "extraPaddle":
                return new ExtraPaddleStrategy(brickerGameManager, gameObjectCollection, brickCounter, userInputListener, imageReader);
            case "turbo":
                return new TurboCollisionStrategy(gameObjectCollection, brickCounter, ball);
            case "double":
                return new DoubleCollisionStrategy(this, brickCounter);
            case "basic":
                return new BasicCollisionStrategy(gameObjectCollection, brickCounter);
        }
        return null;
    }
}
