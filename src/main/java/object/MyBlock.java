package object;

import InitResource.LoadImage;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.example.myarkanoid.HelloApplication;

import java.util.Objects;

public class MyBlock extends Block {
    private double speed;
    private double defaultWidth;
    private double increasedWidth = 30;
    private double maxBuffedTime = 5;
    private Timeline currentBuff = null;
    private Image image;

    public MyBlock(double speed) {
        this.speed = speed;
        this.image = LoadImage.getPaddle();
    }

    public MyBlock(double width, double height, double speed) {
        super((HelloApplication.WIDTH - width) / 2, 500, width, height);
        this.defaultWidth = width;
        this.speed = speed;
        this.image = LoadImage.getPaddle();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void resetMyBlock() {
        setX((HelloApplication.WIDTH - getWidth()) / 2);
        setWidth(defaultWidth);
    }

    public void collisionHandling () {
        if ((getX() + getWidth()) > HelloApplication.WIDTH) {
            setX(HelloApplication.WIDTH - getWidth());
        } else if ((getX() < 0)) {
            setX(0);
        }
    }

    public void increaseWidth() {
        if (currentBuff != null) {
            currentBuff.stop();
            currentBuff = null;
        } else {
            setX(getX() - increasedWidth / 2);
            setWidth(defaultWidth + increasedWidth);
        }

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(maxBuffedTime), e -> {
                    setWidth(defaultWidth);
                    setX(getX() + increasedWidth / 2);
                    currentBuff = null;
                })
        );

        currentBuff = timeline;
        timeline.playFromStart();
    }

    @Override
    public void addOnScene(GraphicsContext gc) {
        gc.drawImage(image, getX(), getY(), getWidth(), getHeight());
    }
}
