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

public class Paddle extends Block {
    private double speed;
    private double defaultWidth;
    private double increasedWidth = 30;
    private double maxBuffedTime = 5;
    private Image image;

    private Timeline currentBuff = null;
    private Timeline startBlinkingEffect;
    private Timeline endBlinkingEffect;

    private DoubleProperty opacity = new SimpleDoubleProperty(1.0);

    public Paddle(double speed) {
        this.speed = speed;
        this.image = LoadImage.getPaddle();
    }

    public Paddle(double width, double height, double speed) {
        super((HelloApplication.WIDTH - width) / 2, 570, width, height);
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
        stopAllBlinking();
        if (currentBuff != null) {
            currentBuff.stop();
            currentBuff = null;
        }
        setOpacity(1.0);
    }

    public void collisionHandling () {
        if ((getX() + getWidth()) > HelloApplication.WIDTH) {
            setX(HelloApplication.WIDTH - getWidth());
        } else if ((getX() < 0)) {
            setX(0);
        }
    }

    public double getOpacity() {
        return opacity.get();
    }

    public void setOpacity(double value) {
        opacity.set(value);
    }

    public DoubleProperty opacityProperty() {
        return opacity;
    }

    private void stopAllBlinking() {
        if (startBlinkingEffect != null) {
            startBlinkingEffect.stop();
            startBlinkingEffect = null;
        }
        if (endBlinkingEffect != null) {
            endBlinkingEffect.stop();
            endBlinkingEffect = null;
        }
        setOpacity(1.0);
    }


    public void increaseWidth() {
        if (currentBuff != null) {
            currentBuff.stop();
        }

        stopAllBlinking();

        startBlinkingEffect = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(50), new KeyValue(opacityProperty(), 0.3))
        );
        startBlinkingEffect.setCycleCount(6);
        startBlinkingEffect.setAutoReverse(true);
        startBlinkingEffect.setOnFinished(e -> {
            setOpacity(1.0);
            startBlinkingEffect = null;
        });

        startBlinkingEffect.play();

        if (currentBuff == null){
            setX(getX() - increasedWidth / 2);
            setWidth(defaultWidth + increasedWidth);
        }

        currentBuff = new Timeline(
                new KeyFrame(Duration.seconds(maxBuffedTime), e -> {
                    currentBuff = null;
                    triggerEndEffect();
                })
        );
        currentBuff.playFromStart();
    }

    private void triggerEndEffect() {
        if (startBlinkingEffect != null) {
            startBlinkingEffect.stop();
            startBlinkingEffect = null;
        }

        setWidth(defaultWidth);
        setX(getX() + increasedWidth / 2);

        endBlinkingEffect = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(50), new KeyValue(opacityProperty(), 0.1))
        );
        endBlinkingEffect.setCycleCount(6);
        endBlinkingEffect.setAutoReverse(true);

        endBlinkingEffect.setOnFinished(e -> {
            setOpacity(1.0);
            endBlinkingEffect = null;
        });

        endBlinkingEffect.play();
    }

    @Override
    public void addOnScene(GraphicsContext gc) {
        gc.setGlobalAlpha(getOpacity());
        gc.drawImage(image, getX(), getY(), getWidth(), getHeight());
        gc.setGlobalAlpha(1.0);
    }
}
