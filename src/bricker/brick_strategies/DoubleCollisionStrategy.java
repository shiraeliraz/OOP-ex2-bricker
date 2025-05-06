package bricker.brick_strategies;
import java.util.Random;
import danogl.GameObject;
import bricker.brick_strategies.StrategyFactory;
import danogl.util.Counter;

public class DoubleCollisionStrategy implements CollisionStrategy{
    private final Random random = new Random();
    private final StrategyFactory strategyFactory;
    private final Counter brickCounter;
    private CollisionStrategy collision1;
    private CollisionStrategy collision2;
    private CollisionStrategy collision3 = null;
    private int doubleCollisionStrategyCounter = 0;
    private String[] strategies = {"addBalls", "extraLife", "extraPaddle", "turbo", "double"};

    public DoubleCollisionStrategy(StrategyFactory strategyFactory, Counter brickCounter) {
        this.strategyFactory = strategyFactory;
        this.brickCounter = brickCounter;
        chooseRandomStrategies();
    }

    private void chooseRandomStrategies() {
        int firstRand = random.nextInt(strategies.length);
        int secondRand;
        int thirdRand = -1;
        if (strategies[firstRand].equals("double")) {
            System.out.println("creates double");
            doubleCollisionStrategyCounter++;
            firstRand = random.nextInt(strategies.length - 1);
            secondRand = random.nextInt(strategies.length - 1);
            thirdRand = random.nextInt(strategies.length - 1);
        } else {
            secondRand = random.nextInt(strategies.length);
            if (strategies[secondRand].equals("double")) {
                System.out.println("creates double");
                secondRand = random.nextInt(strategies.length - 1);
                thirdRand = random.nextInt(strategies.length - 1);
            }
        }
        // build with factory
        collision1 = strategyFactory.buildStrategy(strategies[firstRand]);
        System.out.println("created " + strategies[firstRand]);
        collision2 = strategyFactory.buildStrategy(strategies[secondRand]);
        System.out.println("created " + strategies[secondRand]);
        if (thirdRand > -1) {
            collision3 = strategyFactory.buildStrategy(strategies[thirdRand]);
            System.out.println("created " + strategies[thirdRand]);
        }
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        collision1.onCollision(gameObject1, gameObject2);
        collision2.onCollision(gameObject1, gameObject2);
        if (collision3 != null) {
            collision3.onCollision(gameObject1, gameObject2);
        }
        System.out.println("double: " + brickCounter.value());
    }
}
