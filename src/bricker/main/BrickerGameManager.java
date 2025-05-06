    package bricker.main;

    import bricker.brick_strategies.*;
    import bricker.gameobjects.Ball;
    import bricker.gameobjects.Brick;
    import bricker.gameobjects.LifeHandler;
    import bricker.gameobjects.Paddle;
    import danogl.GameManager;
    import danogl.GameObject;
    import danogl.collisions.GameObjectCollection;
    import danogl.collisions.Layer;
    import danogl.components.CoordinateSpace;
    import danogl.gui.*;
    import danogl.gui.rendering.ImageRenderable;
    import danogl.gui.rendering.RectangleRenderable;
    import danogl.gui.rendering.Renderable;
    import danogl.gui.rendering.TextRenderable;
    import danogl.util.Counter;
    import danogl.util.Vector2;

    import java.awt.event.KeyEvent;
    import java.util.Random;

    import java.awt.*;

    public class BrickerGameManager extends GameManager {


        private static final float BALL_SPEED = 150f;
        private static final int GAP_WIDTH = 4;
        private static final int BRICK_HEIGHT = 15;
        private static final int WALL_WIDTH = 5;
        private static final String LOSE_MESSAGE = "You lose! Play again?";
        private static final String WIN_MESSAGE = "You win! Play again?";
        private int numberOfRows = 7;
        private int numberOfColumns = 8;
        private Vector2 windowDimensions;
        private Ball ball;
        private WindowController windowController;
        private UserInputListener inputListener;
        private LifeHandler lifeHandler;
        private Counter brickCounter = new Counter(0);
        private int remainingLives = 3;
        private ImageRenderable ballImage;
//        private boolean turboModeOn = false;
//        private int turboCollisionCounter;
        private ImageReader imageReader;
//        private int turboCollisionCounter;

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
        public void setRemainingLives(int remainingLives) {
            this.remainingLives = remainingLives;
        }
        public int getLives(){
            return remainingLives;
        }

        public LifeHandler getLifeHandler() {
            return lifeHandler;
        }

        private void createWalls(Vector2 windowDimensions) { // why is it public?
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
            for (int i = 0; i < numberOfColumns; i++) {
                createSingleBrick(topLeftCorner, brickDimensions,imageReader, collisionStrategy);
                topLeftCorner = topLeftCorner.add(new Vector2(brickWidth+GAP_WIDTH, 0));
                brickCounter.increment();
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
            this.windowController = windowController;
            lifeHandler = new LifeHandler(this.gameObjects(), remainingLives,imageReader, windowDimensions.y());
            lifeHandler.createAllHearts();
            lifeHandler.setLives(remainingLives);
            this.inputListener = inputListener;
            this.imageReader = imageReader;

            // creating ball
            createBall(imageReader, soundReader);
            initBall();
            //creating background
            createBackground(imageReader);

            // creating paddle
            createPaddle(imageReader, inputListener);

            // creating a brick strategy
            GameObjectCollection gameObjectCollection = this.gameObjects();
//            BasicCollisionStrategy basicCollisionStrategy = new BasicCollisionStrategy(gameObjectCollection, brickCounter);
//            AddBallsCollisionStrategy addBallsCollisionStrategy = new AddBallsCollisionStrategy(gameObjectCollection, imageReader, soundReader, brickCounter);
//            //Place all bricks
//            placeBricks(imageReader, basicCollisionStrategy);

//            ExtraLifeStrategy extraLifeStrategy = new ExtraLifeStrategy(gameObjectCollection, imageReader, brickCounter, this);
//            placeBricks(imageReader, extraLifeStrategy);

//            ExtraPaddleStrategy extraPaddleStrategy = new ExtraPaddleStrategy(gameObjects(), brickCounter, windowDimensions, inputListener, imageReader);
//            placeBricks(imageReader, extraPaddleStrategy);

            TurboCollisionStrategy turboCollisionStrategy = new TurboCollisionStrategy(gameObjectCollection, brickCounter, ball);
            placeBricks(imageReader, turboCollisionStrategy);

        }



        private void createBall(ImageReader imageReader, SoundReader soundReader) {
            this.ballImage =
            imageReader.readImage("assets/ball.png", true);
            Sound collisionSound = soundReader.readSound("assets/blop.wav");
            Renderable redBallImage = imageReader.readImage("assets/redball.png", true);
            this.ball = new Ball(Vector2.ZERO, new Vector2(20,20), this.ballImage , collisionSound, redBallImage);
        }

        private void createBackground(ImageReader imageReader) {
            Renderable backGroundImage =
                    imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
            GameObject background =
                    new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), windowDimensions.y()), backGroundImage);
            this.gameObjects().addGameObject(background, Layer.BACKGROUND);
            background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        }

        private void placeBricks(ImageReader imageReader, CollisionStrategy CollisionStrategy) {
            int y = GAP_WIDTH+WALL_WIDTH;
            BasicCollisionStrategy basicCollisionStrategy = new BasicCollisionStrategy(this.gameObjects(), brickCounter);
            for (int i = 0; i < numberOfRows; i++) {
                if (i > 3) {
                    placeRow(imageReader, basicCollisionStrategy, y);
                }
                else {
                    placeRow(imageReader, CollisionStrategy,y);

                }
                y+=BRICK_HEIGHT+GAP_WIDTH;
            }
        }

        private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
            Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
            Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(100, 15), paddleImage, inputListener, windowDimensions);
            paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
            this.gameObjects().addGameObject(paddle);
            createWalls(windowDimensions);
        }

        private void initBall() {
            ball.setCenter(windowDimensions.mult(0.5f));
            ball.renderer().setRenderable(ballImage);
            System.out.println("Change to green at" + System.currentTimeMillis());
            this.gameObjects().addGameObject(ball);
            ball.turnOffTurbo();
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
        }

        private void removeElementsOutOfBound() {
            for (GameObject gameObject : gameObjects()) {
                if (gameObject == ball) {
                    continue;
                }
                float height = gameObject.getCenter().y();
                if (height > windowDimensions.y()) {
                    this.gameObjects().removeGameObject(gameObject);
                }
            }
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            if (remainingLives != lifeHandler.getLives()) {
                remainingLives = lifeHandler.getLives();
            }
            removeElementsOutOfBound();
            if (brickCounter.value() == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
                playAgain(WIN_MESSAGE);
            } else if (remainingLives == 0) {
                playAgain(LOSE_MESSAGE);

            } else {
                if (ball != null) {
                    checkIfLost();
                }
            }
            destroyObjectsNotOnScreen();
        }

        private void checkIfLost() {
            float ballHeight = ball.getCenter().y();
            if (ballHeight > windowDimensions.y()) {
                remainingLives--;
                lifeHandler.setLives(remainingLives);
                initBall();
            }
        }

        private void playAgain(String message) {
            if (windowController.openYesNoDialog(message)){
                remainingLives = 3;
                windowController.resetGame();
                brickCounter = new Counter(0);
                initBall();

            }
            else {
                windowController.closeWindow();
            }
        }
        private void destroyObjectsNotOnScreen() {
            for (GameObject gameObject : gameObjects()) {
                if (gameObject.getTopLeftCorner().y() > windowDimensions.y() ) {
                    gameObjects().removeGameObject(gameObject);
                }
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
            gameManager.run();  // run game

        }
    }
