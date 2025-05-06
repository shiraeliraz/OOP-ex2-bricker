package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Ball extends GameObject {
    private final Sound collisionSound;
    private final Renderable redBallImage;
    private final Renderable greenBallImage;
    private int collisionCounter = 0;
    private int turboCounter = 6;
    private boolean isTurbo = false;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param greenBallImage     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param collisionSound The collision sound of the ball
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable greenBallImage, Sound collisionSound, Renderable redBallImage) {
        super(topLeftCorner, dimensions, greenBallImage);
        this.collisionSound = collisionSound;
        this.redBallImage = redBallImage;
        this.greenBallImage = greenBallImage;

    }

    public void turnOffTurbo() {
//        this.setTag("Basic Mode");
        this.setVelocity(this.getVelocity().mult(1/1.40f));
        turboCounter = 6;
        this.renderer().setRenderable(greenBallImage);
        isTurbo = false;
    }

    public void setTurbo(boolean isTurboMode) {
        isTurbo = isTurboMode;
    }

    public boolean getTurbo() { return isTurbo;}

    public void turnOnTurbo() {
        turboCounter = 6;
        this.setVelocity(this.getVelocity().mult(1.40f));
        this.renderer().setRenderable(redBallImage);
        isTurbo = true;
    }

    //Collisions
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        System.out.println(turboCounter);
        if (isTurbo) {
            turboCounter--;
            if (turboCounter == 0) {
                turnOffTurbo();
                this.renderer().setRenderable(greenBallImage);
            }
        }
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        collisionCounter++;

    }

    public int getCollisionCounter() {
        return collisionCounter;
    }
}
