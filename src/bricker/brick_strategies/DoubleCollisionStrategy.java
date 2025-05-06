package bricker.brick_strategies;
import java.util.Random;
import danogl.GameObject;
import bricker.brick_strategies.StrategyFactory;

public class DoubleCollisionStrategy implements CollisionStrategy{
    private final Random random = new Random();
    private final StrategyFactory strategyFactory;
    private CollisionStrategy collision1;
    private CollisionStrategy collision2;
    private CollisionStrategy collision3 = null;
    private int doubleCollisionStrategyCounter = 0;
    private String[] strategies = {"addBalls", "extraLife", "extraPaddle", "turbo", "double"};

    public DoubleCollisionStrategy(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
        chooseRandomStrategies();
    }

    private void chooseRandomStrategies() {
        int firstRand = random.nextInt(strategies.length - 1);
        int secondRand;
        int thirdRand = -1;
        if (strategies[firstRand].equals("double")) {
            doubleCollisionStrategyCounter++;
            firstRand = random.nextInt(strategies.length - 2);
            secondRand = random.nextInt(strategies.length - 2);
        } else {
            secondRand = random.nextInt(strategies.length - 1);
            if (strategies[secondRand].equals("double")) {
                secondRand = random.nextInt(strategies.length - 2);
                thirdRand = random.nextInt(strategies.length - 2);
            }
        }
        // build with factory
        collision1 = strategyFactory.buildStrategy(strategies[firstRand]);
        collision2 = strategyFactory.buildStrategy(strategies[secondRand]);
        if (thirdRand > -1) {
            collision3 = strategyFactory.buildStrategy(strategies[thirdRand]);
        }
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        collision1.onCollision(gameObject1, gameObject2);
        collision2.onCollision(gameObject1, gameObject2);
        if (collision3 != null) {
            collision3.onCollision(gameObject1, gameObject2);
        }
    }
}
