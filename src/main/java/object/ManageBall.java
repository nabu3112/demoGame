package object;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ManageBall {
    private List<Ball> balls = new ArrayList<>();

    //Sửa hàm khởi tạo thành mặc định.
    public ManageBall() {

    }

    public int getNumOfBalls() {
        return balls.size();
    }

    public void resetBall() {
        balls.clear();
    }

    //Hàm thêm bóng nếu dùng mũi tên.
    public void addNewBall(double xPaddle, double yPaddle, double widthPaddle, double dx, double dy) {
        balls.add(new Ball(xPaddle + widthPaddle / 2, yPaddle - 6 - 1, 6, 3, dx, dy));
    }

    //Hàm thêm bóng nếu dùng bóng ngắm.
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
            double newDx = (ballCreateBuff.getDx() - ballCreateBuff.getDy()) * Math.sqrt(2) / 2.0;
            double newDy = (ballCreateBuff.getDx() + ballCreateBuff.getDy()) * Math.sqrt(2) / 2.0;
            ballCreateBuff.setDx(newDx);
            ballCreateBuff.setDy(newDy);
            balls.add(new Ball(ballCreateBuff.getBallX(), ballCreateBuff.getBallY(),
                    ballCreateBuff.getRadius(), ballCreateBuff.getSpeed(),
                    newDy, -newDx));
        }
    }

    //Buff bóng xuyên phá nếu dùng mũi tên.
    public void buffBullet(double xPaddle, double yPaddle, double widthPaddle, double dx, double dy) {
        Ball newBall = new Ball(xPaddle + widthPaddle / 2, yPaddle - 6 - 1, 6, 10,dx, dy);
        newBall.setIsthrough(true);
        balls.add(newBall);
    }

    //Buff bóng xuyên phá nếu dùng bóng ngắm.
    public void buffBullet(double ballX, double ballY) {
        Ball newBall = new Ball(ballX, ballY, 6, 10,0, 1);
        newBall.setIsthrough(true);
        balls.add(newBall);
    }

    public void addListOnScene(GraphicsContext gc, Paddle paddle,
                               List<GameBlock> blocks, ManageBuff listBuffs, GameSession gameSession) {
        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).updateBall(paddle, blocks, listBuffs, gameSession);
            balls.get(i).addOnScene(gc);
            if (balls.get(i).checkOutScreen()) {
                balls.remove(i);
                i--;
            }
        }
    }
}
