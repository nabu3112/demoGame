package object;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ManageBall {
    private List<Ball> balls = new ArrayList<>();

    public ManageBall(double xPaddle, double yPaddle, double widthPaddle) {
        balls.add(new Ball(xPaddle, yPaddle, widthPaddle, 6, 3, 0, -2));
    }

    public void addBall(double xPaddle, double yPaddle, double widthPaddle) {
        balls.add(new Ball(xPaddle, yPaddle, widthPaddle, 6, 3, -1, -1));
        balls.add(new Ball(xPaddle, yPaddle, widthPaddle, 6, 3, 0, -1));
        balls.add(new Ball(xPaddle, yPaddle, widthPaddle, 6, 3, 1, -1));
    }

    public void addListOnScene(GraphicsContext gc, MyBlock myBlock,
                               List<GameBlock> blocks, ManageBuff listBuffs) {
        for (Ball b : balls) {
            b.updateBall(myBlock, blocks, listBuffs);
            b.addOnScene(gc);
        }
    }
}
