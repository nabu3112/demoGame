package object;

import InitResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Buff extends Circle{
    private String buffType;
    private Ball ballCreateBuff;

    private int state = 0;

    private final Image[] coin;
    private final Image increasePaddle;
    private final Image bullet;

    public Buff(double xMyBlock, double yMyBlock, double widthMyBlock, double heightMyBlock,
                double radius, double speed, double dx, double dy, String buffType, Ball ballCreateBuff) {
        super(xMyBlock + widthMyBlock / 2, yMyBlock + heightMyBlock + radius, radius, speed, dx, dy);
        this.buffType = buffType;
        this.ballCreateBuff = ballCreateBuff;
        coin = LoadImage.getCoin();
        increasePaddle = LoadImage.getIncreasePaddle();
        bullet =LoadImage.getBullet();
    }

    public String updateBuff (Paddle paddle, ManageBall manageBall) {
        setBallX(getBallX() + getDx());
        setBallY(getBallY() + getDy());

        if (checkContactToBlock(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight())) {
            if (buffType.equals("Add 3 balls")) {
                manageBall.addBall(paddle.getX(), paddle.getY(), paddle.getWidth());
            } else if (buffType.equals("Clone Ball")) {
                manageBall.buffCloneBall(ballCreateBuff);
            } else if (buffType.equals("Increase Paddle Width")) {
                paddle.increaseWidth();
            } else if (buffType.equals("Bullet")) {
                return "AIMING";
            } else if (buffType.equals("Coin")) {
                ManageBuff.extraCoins++;
            }
            return "HIT";
        }
        return "NOHIT";
    }

    @Override
    public void addOnScene(GraphicsContext gc) {
        if (state > 23) {
            state = 0;
        }
        if (buffType.equals("Coin")) {
            gc.drawImage(coin[state / 4], getBallX() - getRadius(), getBallY() - getRadius(),
                    getRadius() * 2, getRadius() * 2);
        } else {
            Image currentImage = null;
            if (buffType.equals("Add 3 ball")) {
                currentImage = increasePaddle;
            } else if (buffType.equals("Clone Ball")) {
                currentImage = increasePaddle;
            } else if (buffType.equals("Increase Paddle Width")) {
                currentImage = increasePaddle;
            } else if (buffType.equals("Bullet")) {
                currentImage = bullet;
            }

            double angleRad = Math.toRadians(state * 15.0);
            double scaleX = Math.cos(angleRad);
            double x = getBallX();
            double y = getBallY();
            double r = getRadius();
            double size = r * 2;

            gc.save();
            gc.translate(x, y);
            gc.scale(scaleX, 1.0);
            gc.drawImage(currentImage, -r, -r, size, size);
            gc.restore();
        }
        state++;
    }
}
