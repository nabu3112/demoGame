package object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.myarkanoid.HelloApplication;

import java.awt.*;
import java.util.List;

public class Ball extends Circle {


    public Ball(double xMyBlock, double yMyBlock, double widthMyBlock, double radius, double speed, double dx, double dy) {
        super(xMyBlock + widthMyBlock / 2, yMyBlock - radius - 1, radius, speed, dx, dy);
    }

    public void setDirection() {
        double length = Math.sqrt(getDx() * getDx() + getDy() * getDy());
        if (length == 0) return;
        setDx((getDx() / length) * getSpeed());
        setDy((getDy() / length) * getSpeed());
    }

    public void updateBall(MyBlock myBlock, List<GameBlock> blocks, ManageBuff listBuffs) {
        setBallX(getBallX() + getDx());
        setBallY(getBallY() + getDy());

        //Va chạm góc
        if (getBallX() - getRadius() <= 0) {
            setBallX(getRadius());
            setDx(-getDx());
        } else if (getBallX() + getRadius() >= HelloApplication.WIDTH) {
            setBallX(HelloApplication.WIDTH - getRadius());
            setDx(-getDx());
        }
        if (getBallY() - getRadius() <= 0) {
            setBallY(getRadius());
            setDy(-getDy());
        }

        // Va cham paddle
        if (checkContactToBlock(myBlock.getX(), myBlock.getY(), myBlock.getWidth(), myBlock.getHeight())) {
            double relativeIntersectX = (getBallX() - (myBlock.getX() + myBlock.getWidth() / 2));
            double normalized = relativeIntersectX / (myBlock.getWidth() / 2);
            double bounceAngle = normalized * (Math.PI / 3); // tối đa 60 độ

            setDx(getSpeed() * Math.sin(bounceAngle));
            setDy(-getSpeed() * Math.cos(bounceAngle));

            setBallY(myBlock.getY() - getRadius() - 1);
        }

        // Va cham block
        GameBlock collidedBlock = null;
        for (GameBlock b : blocks) {
            if (checkContactToBlock(b.getX(), b.getY(), b.getWidth(), b.getHeight())) {
                collidedBlock = b;
                break;
            }
        }

        if (collidedBlock != null) {
            collidedBlock.contactGameBlock(this);
            if (collidedBlock.handleBlock()) {
                System.out.println(1);
                listBuffs.addBuff(collidedBlock.getX(), collidedBlock.getY(),
                        collidedBlock.getWidth(), collidedBlock.getHeight(), "Add 3 balls");
                blocks.remove(collidedBlock);
            }
        }

        setDirection();
    }
}
