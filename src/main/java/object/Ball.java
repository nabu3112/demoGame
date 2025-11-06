package object;

import org.example.myarkanoid.HelloApplication;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Ball extends Circle {
    boolean isthrough = false;
    private double relativeX = -1;

    public Ball(double ballX, double ballY, double radius, double speed, double dx, double dy) {
        super(ballX, ballY, radius, speed, dx, dy);
    }

    public boolean isIsthrough() {
        return isthrough;
    }

    public void setIsthrough(boolean isthrough) {
        this.isthrough = isthrough;
    }

    public void updateBall(Paddle paddle, List<GameBlock> blocks, ManageBuff listBuffs, GameSession gameSession) {
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
        if (getBallY() - getRadius() <= 65) {
            setBallY(getRadius() + 65);
            setDy(-getDy());
            isthrough = false;
            setSpeed(3);
        }

        // Va cham paddle
        if (checkContactToBlock(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight())) {
            double relativeIntersectX = (getBallX() - (paddle.getX() + paddle.getWidth() / 2));
            double normalized = relativeIntersectX / (paddle.getWidth() / 2);
            double bounceAngle = normalized * (Math.PI / 3); // tối đa 60 độ

            setDx(getSpeed() * Math.sin(bounceAngle));
            setDy(-getSpeed() * Math.cos(bounceAngle));

            setBallY(paddle.getY() - getRadius() - 1);
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
            if (!isthrough) {
                collidedBlock.contactGameBlock(this);
            }
            if (collidedBlock.handleBlock()) {
                gameSession.addScore(collidedBlock.getTypeBlock());
                Random rand = new Random();
                int r = rand.nextInt(1);
                if (r == 0) {
                    listBuffs.addBuff(collidedBlock.getX(), collidedBlock.getY(),
                            collidedBlock.getWidth(), collidedBlock.getHeight(), "Increase Paddle Width", this);
                }
                blocks.remove(collidedBlock);
            }
        }

        setDirection();
    }

    //Thêm hàm này.
    public void inPaddle(double xPaddle, double widthPaddle) {
        setDirection();
        if (this.relativeX == -1) {
            this.relativeX = widthPaddle / 2;
        }

        // 2. Cập nhật vị trí trượt TƯƠNG ĐỐI
        this.relativeX += this.getDx();

        // 3. Kiểm tra biên (so với cạnh của paddle)
        double leftEdgeRelative = getRadius();
        double rightEdgeRelative = widthPaddle - getRadius();

        if (this.relativeX > rightEdgeRelative) {
            this.relativeX = rightEdgeRelative;
            this.setDx(-this.getDx()); // Đổi hướng
        } else if (this.relativeX < leftEdgeRelative) {
            this.relativeX = leftEdgeRelative;
            this.setDx(-this.getDx());// Đổi hướng
        }

        // 4. Đặt vị trí TUYỆT ĐỐI (vị trí paddle + vị trí tương đối)
        setBallX(xPaddle + this.relativeX);
    }
}
