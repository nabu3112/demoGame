package object;

import javafx.scene.image.Image;
import org.example.myarkanoid.HelloApplication;

public class MyBlock extends Block {
    private double speed;

    public MyBlock(double speed) {
        this.speed = speed;
    }

    public MyBlock(double width, double height, double speed) {
        super((HelloApplication.WIDTH - width) / 2, 500, width, height);
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void collisionHandling () {
        if ((getX() + getWidth()) > HelloApplication.WIDTH) {
            setX(HelloApplication.WIDTH - getWidth());
        } else if ((getX() < 0)) {
            setX(0);
        }
    }
}
