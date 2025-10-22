package object;

import InitResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.myarkanoid.HelloApplication;

public class Character {
    private double xOnMap = 200;
    private double yOnMap = 200;
    private final double size;
    private double speed = 3;
    private int state = 0;
    private int direction = 0;
    private boolean isRunning = false;

    private final Image[] idleAhead;
    private final Image[] idleBehind;
    private final Image[] idleLeft;
    private final Image[] idleRight;

    private final Image[] runAhead;
    private final Image[] runBehind;
    private final Image[] runLeft;
    private final Image[] runRight;

    public Character() {
        this.idleAhead = LoadImage.getIdleAhead();
        this.idleBehind = LoadImage.getIdleBehind();
        this.idleLeft = LoadImage.getIdleLeft();
        this.idleRight = LoadImage.getIdleRight();

        this.runAhead = LoadImage.getRunAhead();
        this.runBehind = LoadImage.getRunBehind();
        this.runLeft = LoadImage.getRunLeft();
        this.runRight = LoadImage.getRunRight();

        this.size = this.idleAhead[0].getWidth() * 2;
    }

    public double getxOnMap() {
        return xOnMap;
    }

    public void setxOnMap(double xOnMap) {
        this.xOnMap = xOnMap;
    }

    public double getyOnMap() {
        return yOnMap;
    }

    public void setyOnMap(double yOnMap) {
        this.yOnMap = yOnMap;
    }

    public double getSize() {
        return size;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void addCharacterOnScreen(GraphicsContext gc) {
        Image[] currentAnimation;
        int frameSkip;
        if (isRunning) {
            if (state > 47) {
                state = 0;
            }
            if (direction == 0) {
                currentAnimation = runAhead;
            } else if (direction == 1) {
                currentAnimation = runLeft;
            } else if (direction == 2) {
                currentAnimation = runRight;
            } else {
                currentAnimation = runBehind;
            }
            frameSkip = 6;
        } else {
            if (state > 71) {
                state = 0;
            }
            if (direction == 0) {
                currentAnimation = idleAhead;
                frameSkip = 6;
            } else if (direction == 1) {
                currentAnimation = idleLeft;
                frameSkip = 6;
            } else if (direction == 2) {
                currentAnimation = idleRight;
                frameSkip = 6;
            } else {
                currentAnimation = idleBehind;
                frameSkip = 18;
            }
        }
        gc.drawImage(currentAnimation[state / frameSkip],
                HelloApplication.WIDTH / 2.0 - size / 2,
                HelloApplication.HEIGHT / 2.0 - size / 2, size, size);
        state ++;
    }
}
