package object;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;

public class ScenePlayGame {
    private MyBlock myBlock;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private boolean running = true;

    private ManageGameBlock listBlocks = new ManageGameBlock();
    private int level = 1;

    private ManageBall listBalls = new ManageBall();

    private AnimationTimer gameLoop;

    public void runGame(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        myBlock = new MyBlock(70, 20, 4);

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        canvas.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));


        startLevel(gc, canvas);
    }

    private void update() {
        if (pressedKeys.contains(KeyCode.LEFT)) {
            myBlock.setX(myBlock.getX() - myBlock.getSpeed());
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            myBlock.setX(myBlock.getX() + myBlock.getSpeed());
        }
        myBlock.collisionHandling();
    }

    public void pauseGame() {
        running = false;
    }

    public void resumeGame() {
        running = true;
    }

    public void startLevel(GraphicsContext gc, Canvas canvas) {
        listBlocks.updateList(level);
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(running) {
                    update();
                    render(gc, canvas);
                }
                if (listBlocks.getNumberBlock() == 0) {
                    gameLoop.stop();
                    level++;
                    startLevel(gc, canvas);
                }
            }
        };
        gameLoop.start();
    }

    public void render(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        myBlock.addOnScene(gc);
        listBlocks.addListOnScene(gc);
        listBalls.addListOnScene(gc, myBlock, listBlocks.getGameBlocks());
    }
}
