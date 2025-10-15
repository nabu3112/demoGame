package object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.myarkanoid.HelloApplication;

import java.awt.*;
import java.util.List;

public class Ball {
    private double ballX;
    private double ballY;
    private double radius = 6;
    private double speed = 3;
    private double dx = 0;
    private double dy = -2;

    public Ball() {

    }

    public Ball(double x, double y) {
        this.ballX = x;
        this.ballY = y;
    }

    public double getBallX() {
        return ballX;
    }

    public void setBallX(double ballX) {
        this.ballX = ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public void setBallY(double ballY) {
        this.ballY = ballY;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDirection() {
        double factor = speed / Math.sqrt(dx * dx + dy * dy);
        dx *= factor;
        dy *= factor;

    }

    public double getRadius() {
        return radius;
    }

    private void setRadius(double radius) {
        this.radius = radius;
    }

    public void addOnScene(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillOval(ballX - radius, ballY - radius, radius * 2, radius * 2);
    }

    private double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    private boolean checkContactToBlock(Block block) {
        double closestX = clamp(ballX, block.getX(), block.getX() + block.getWidth());
        double closestY = clamp(ballY, block.getY(), block.getY() + block.getHeight());
        return (Math.pow(ballX - closestX, 2) + Math.pow(ballY - closestY, 2)) <= (Math.pow(radius, 2));
    }

    public void updateBall(MyBlock myBlock, List<GameBlock> blocks) {
        ballX += dx;
        ballY += dy;

        if (ballX - radius < 0 || ballX + radius > HelloApplication.WIDTH) dx = -dx;
        if (ballY - radius < 0) dy = -dy;

        // Paddle collision
        if (checkContactToBlock(myBlock)) {
            dy = -Math.abs(dy);
            dx = 5 * (ballX - (myBlock.getX() + myBlock.getWidth() / 2)) / (myBlock.getWidth() / 2);
        }

        // Block collision
        for (GameBlock b : blocks) {
            if (checkContactToBlock(b)) {
                b.contactGameBlock(this);
                if (b.handleBlock()) {
                    blocks.remove(b);
                }
                break;
            }
        }
        setDirection();
    }

}
