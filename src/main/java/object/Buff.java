package object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Buff extends Circle{
    private String buffType;

    public Buff(double xMyBlock, double yMyBlock, double widthMyBlock, double heightMyBlock,
                double radius, double speed, double dx, double dy, String buffType) {
        super(xMyBlock + widthMyBlock / 2, yMyBlock + heightMyBlock + radius, radius, speed, dx, dy);
        this.buffType = buffType;
    }

    public boolean updateBuff (double xPaddle, double yPaddle,
                            double widthPadle, double heightPaddle, ManageBall manageBall) {
        setBallX(getBallX() + getDx());
        setBallY(getBallY() + getDy());

        if (checkContactToBlock(xPaddle, yPaddle, widthPadle, heightPaddle)) {
            if (buffType.equals("Add 3 balls")) {
                manageBall.addBall(xPaddle, yPaddle, widthPadle);

            }
            return true;
        }
        return false;
    }

    @Override
    public void addOnScene(GraphicsContext gc) {
        if (buffType.equals("Add 3 ball")) {
            gc.setFill(Color.YELLOW);

        }
        gc.fillOval(getBallX() - getRadius(), getBallY() - getRadius(),
                getRadius() * 2, getRadius() * 2);
    }
}
