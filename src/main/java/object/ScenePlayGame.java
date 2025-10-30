package object;

import InitResource.ReadWriteData;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.example.myarkanoid.HelloApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ScenePlayGame {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private AnimationTimer gameLoop;

    private boolean running = true;
    private int level;
    boolean isIngame = false;

    private Paddle paddle;
    private ManageGameBlock listBlocks;
    private ManageBall listBalls;
    private ManageBuff listBuffs;
    private Character mainCharacter;
    private Map map;

    private Ball aimingBall;
    private Arrow aimingArrow;
    private boolean isAiming = true;
    private boolean isBuffBullet = false;
    private int existingCoins;

    public void runGame(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        canvas.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));

        initObject();

        startLevel(gc, canvas);
    }

    private void initObject() {
        paddle = new Paddle(70, 10, 4);
        mainCharacter = new Character();
        map = new Map(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());
        listBlocks = new ManageGameBlock();
        listBalls = new ManageBall();
        listBuffs = new ManageBuff();
        aimingArrow = new Arrow(paddle.getX() + paddle.getWidth() / 2, paddle.getY(),
                50, -80, 80, 150);
        aimingBall = new Ball(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - 6, 6, 1, 1, 0);

        level = ReadWriteData.getLevel();
        existingCoins = ReadWriteData.getExistingCoins();
    }

    public void resetObject() {
        paddle.resetMyBlock();
        listBlocks.resetGameBlock(level);
        listBalls.resetBall();
        listBuffs.resetBuff();
        isAiming = true;
        isBuffBullet = false;
        System.out.println("xu hien co: " + existingCoins);
    }

    public void saveData() {
        ReadWriteData.setLevel(level);
        ReadWriteData.setExistingCoins(existingCoins);
        ReadWriteData.saveGameData();
    }

    private void startLevel(GraphicsContext gc, Canvas canvas) {
        listBlocks.resetGameBlock(level);
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    if (isIngame) {
                        updateInGame();
                        renderInGame(gc, canvas);
                        if (listBlocks.getNumberBlock() == 0) {
                            isIngame = false;
                            level ++;
                            existingCoins += ManageBuff.extraCoins;
                            resetObject();
                        }
                        if (listBalls.getNumOfBalls() == 0 && !isAiming) {
                            isIngame = false;
                            resetObject();
                        }
                    } else {
                        updateInLoppy();
                        renderInLoppy(gc, canvas);
                    }
                }
            }
        };
        gameLoop.start();
    }

    private void updateInGame() {

        // --- Logic chung (Luôn chạy) ---
        if (pressedKeys.contains(KeyCode.LEFT)) {
            paddle.setX(paddle.getX() - paddle.getSpeed());
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            paddle.setX(paddle.getX() + paddle.getSpeed());
        }
        paddle.collisionHandling();

        // --- Logic theo trạng thái ---
        if (isAiming || isBuffBullet) {

//            double paddleCenterX = myBlock.getX() + myBlock.getWidth() / 2;
//            double paddleTopY = myBlock.getY();
//
//            aimingArrow.setPosition(paddleCenterX, paddleTopY);
//            aimingArrow.update(0.016);
            aimingBall.inPaddle(paddle.getX(), paddle.getWidth());


            if (pressedKeys.contains(KeyCode.SPACE)) {
                if (isAiming) {
//                    listBalls.addNewBall(myBlock.getX(), myBlock.getY(), myBlock.getWidth(),
//                            Math.sin(aimingArrow.getAngleInRadians()), - Math.cos(aimingArrow.getAngleInRadians()));
                    listBalls.addNewBall(aimingBall.getBallX(), aimingBall.getBallY());
                    isAiming = false;
                }
                if (isBuffBullet) {
//                    listBalls.buffBullet(myBlock.getX(), myBlock.getY(), myBlock.getWidth(),
//                            Math.sin(aimingArrow.getAngleInRadians()), - Math.cos(aimingArrow.getAngleInRadians()));
                    listBalls.buffBullet(aimingBall.getBallX(), aimingBall.getBallY());
                    isBuffBullet = false;
                }

            }
        }
    }

    private void updateInLoppy() {

        boolean isW = pressedKeys.contains(KeyCode.W);
        boolean isA = pressedKeys.contains(KeyCode.A);
        boolean isS = pressedKeys.contains(KeyCode.S);
        boolean isD = pressedKeys.contains(KeyCode.D);

        if (!(isA || isD || isW || isS) || (isA && isD && isW && isS)
                || (isA && isD && !isS && !isW) || (!isA && !isD && isS && isW)) {
            mainCharacter.setRunning(false);
        } else {
            mainCharacter.setRunning(true);
            if (isA && !isD) {
                mainCharacter.setDirection(1);
                if (isW && isS || !isW && !isS) {
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() - mainCharacter.getSpeed());
                } else if (isW) {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() - mainCharacter.getSpeed() * Math.sin(45));
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() - mainCharacter.getSpeed() * Math.sin(45));
                } else {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() + mainCharacter.getSpeed() * Math.sin(45));
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() - mainCharacter.getSpeed() * Math.sin(45));
                }
            } else if (isD && !isA) {
                mainCharacter.setDirection(2);
                if (isW && isS || !isW && !isS) {
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() + mainCharacter.getSpeed());
                } else if (isW) {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() - mainCharacter.getSpeed() * Math.sin(45));
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() + mainCharacter.getSpeed() * Math.sin(45));
                } else {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() + mainCharacter.getSpeed() * Math.sin(45));
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() + mainCharacter.getSpeed() * Math.sin(45));
                }
            } else {
                if (isW) {
                    mainCharacter.setDirection(3);
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() - mainCharacter.getSpeed());
                } else {
                    mainCharacter.setDirection(0);
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() + mainCharacter.getSpeed());
                }
            }
        }

        map.setXYOnScreen(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());

        if (pressedKeys.contains(KeyCode.ENTER)) {
            isIngame = true;
        }
    }

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
    }

    private void renderInGame(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.PEACHPUFF);
        gc.fillRect(0, 0, HelloApplication.WIDTH, 65);
        paddle.addOnScene(gc);
        listBlocks.addListOnScene(gc);
        listBalls.addListOnScene(gc, paddle, listBlocks.getGameBlocks(), listBuffs);
        Boolean b = listBuffs.addBuffOnScene(gc, paddle, listBalls);
        if (!isBuffBullet) {
            isBuffBullet = b;
        }
        if (isAiming || isBuffBullet) {
            //aimingArrow.draw(gc);
            aimingBall.addOnScene(gc);
        }
    }

    private void renderInLoppy(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        map.addMapOnScreen(gc);
        mainCharacter.addCharacterOnScreen(gc);

    }

    public boolean isIngame() {
        return isIngame;
    }

    public void setIngame(boolean ingame) {
        this.isIngame = ingame;
    }

    public void restartRPG(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // reset đối tượng
        mainCharacter = new Character();
        map = new Map(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());
        pressedKeys.clear();

        // reset trạng thái
        isIngame = false;
        level = 1;
        running = true;

        // chạy lại vòng lặp
        if (gameLoop != null) gameLoop.stop();
        startLevel(gc, canvas);
    }

    public void restartArkanoid(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        resetObject();
        pressedKeys.clear();

        isIngame = true;
        running = true;

        if (gameLoop != null) gameLoop.stop();
        startLevel(gc, canvas);
    }

    public boolean isInArkanoid() {
        return isIngame; // true = đang trong mini game bắn bóng
    }

    public void quitToMainGame() {
        isIngame = false;   // quay lại màn hình RPG
        running = true;     // đảm bảo vòng lặp tiếp tục chạy
    }
}
