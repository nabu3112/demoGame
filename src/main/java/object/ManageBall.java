package object;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ManageBall {
    private List<Ball> balls = new ArrayList<>();

    public ManageBall() {

    }

    public int getNumOfBalls() {
        return balls.size();
    }

    public void resetBall() {
        balls.clear();
    }

    public void addNewBall(double xPaddle, double yPaddle, double widthPaddle, double dx, double dy) {
        balls.add(new Ball(xPaddle + widthPaddle / 2, yPaddle - 6 - 1, 6, 3, dx, dy));
    }

    public void addNewBall(double ballX, double ballY) {
        balls.add(new Ball(ballX, ballY, 6, 3, 0, 1));
    }

    //Buff thêm bóng.
    public void addBall(double xPaddle, double yPaddle, double widthPaddle) {
        balls.add(new Ball(xPaddle + widthPaddle / 2, yPaddle - 6 - 1, 6, 3, -1, -1));
        balls.add(new Ball(xPaddle + widthPaddle / 2, yPaddle - 6 - 1, 6, 3, 0, -1));
        balls.add(new Ball(xPaddle + widthPaddle / 2, yPaddle - 6 - 1, 6, 3, 1, -1));
    }

    //Buff bóng phân thân.
    public void buffCloneBall(Ball ballCreateBuff) {
        if (ballCreateBuff.isInScreen()) {
            balls.add(new Ball(ballCreateBuff.getBallX(), ballCreateBuff.getBallY(),
                    ballCreateBuff.getRadius(), ballCreateBuff.getSpeed(),
                    - ballCreateBuff.getDx(), -ballCreateBuff.getDy()));
        }
    }

    //Buff bóng xuyên phá.
    public void buffBullet(double xPaddle, double yPaddle, double widthPaddle, double dx, double dy) {
        Ball newBall = new Ball(xPaddle + widthPaddle / 2, yPaddle - 6 - 1, 6, 10,dx, dy);
        newBall.setIsthrough(true);
        balls.add(newBall);
    }

    public void buffBullet(double ballX, double ballY) {
        Ball newBall = new Ball(ballX, ballY, 6, 10,0, 1);
        newBall.setIsthrough(true);
        balls.add(newBall);
    }

    public void addListOnScene(GraphicsContext gc, Paddle paddle,
                               List<GameBlock> blocks, ManageBuff listBuffs) {
        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).updateBall(paddle, blocks, listBuffs);
            balls.get(i).addOnScene(gc);
            if (balls.get(i).checkOutScreen()) {
                balls.remove(i);
                i--;
            }
        }
    }
}
