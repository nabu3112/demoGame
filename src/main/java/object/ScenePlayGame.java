package object;

import InitResource.LoadImage;
import InitResource.ReadWriteData;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
    private GameSession gameSession;

    private Ball aimingBall;             //Thêm Ball ngắm bắn.
    private boolean isAiming = true;     //Biến xác nhận ngắm bắn.
    private boolean isBuffBullet = false;//Biến ngắm bắn lúc có buff.
    private int existingCoins;           //Thêm thuộc tính xu.

    private float blockSpawnTimer = 0.0f;
    private long lastFrameTime = 0;
    private static final float BLOCK_SPAWN_TIME = 15.0f;

    private Image backGround1 = LoadImage.getBackGround1();


    public void runGame(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        canvas.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));

        initObject();

        startLevel(gc, canvas);
    }

    //Thêm khởi tạo ball, level, xu.
    private void initObject() {
        paddle = new Paddle(70, 10, 4);
        mainCharacter = new Character();
        map = new Map(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());
        listBlocks = new ManageGameBlock();

        listBalls = new ManageBall();
        listBuffs = new ManageBuff();
        aimingBall = new Ball(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - 6, 6, 1, 1, 0);

        level = ReadWriteData.getLevel();
        existingCoins = ReadWriteData.getExistingCoins();
        gameSession = new GameSession();
        listBlocks.resetGameBlock(level);
    }

    //Thêm reset.
    public void resetObject() {
        paddle.resetMyBlock();
        listBlocks.resetGameBlock(level);
        listBalls.resetBall();
        listBuffs.resetBuff();
        isAiming = true;
        isBuffBullet = false;
        blockSpawnTimer = 0.0f;

        // FIX 1: Reset lastFrameTime để deltaTime được tính lại
        lastFrameTime = 0;

        // Reset luôn cả GameSession
        gameSession.reset();
        System.out.println("xu hien co: " + existingCoins);
    }

    public void saveData() {
        ReadWriteData.setLevel(level);
        ReadWriteData.setExistingCoins(existingCoins);
        ReadWriteData.saveGameData();
    }

    private void startLevel(GraphicsContext gc, Canvas canvas) {
        //listBlocks.resetGameBlock(level);
        lastFrameTime = 0;

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTime == 0) {
                    lastFrameTime = now;
                    return; // Bỏ qua frame này
                }

                // 2. Tính deltaTime (bằng GIÂY)
                float deltaTime = (now - lastFrameTime) / 1_000_000_000.0f;

                // 3. Cập nhật lastFrameTime cho frame tiếp theo
                lastFrameTime = now;
                if (running) {
                    if (isIngame) {
                        updateInGame(deltaTime);
                        renderInGame(gc, canvas);
                        if (listBlocks.getNumberBlock() == 0) {
                            if (level <= 3) {
                                isIngame = false;
                                level++;
                                existingCoins += ManageBuff.extraCoins;
                                resetObject();
                            } else {
                                blockSpawnTimer = BLOCK_SPAWN_TIME;
                            }
                        }
                        if ((listBalls.getNumOfBalls() == 0 && !isAiming) || paddle.getLife() <= 0) {
                            if (level >= 4) {
                                existingCoins += ManageBuff.extraCoins;
                                GameStats.addGameSession(gameSession);
                            }
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

    //Thêm xử lí ngắm bắn.
    private void updateInGame(float deltaTime) {
        if (!isAiming) {
            if (level >= 4) {
                gameSession.update(deltaTime);
                blockSpawnTimer += deltaTime;
            }
            listBuffs.setTimeCreateObstacle(paddle.getX(), paddle.getWidth(), level, deltaTime);  //Thêm dòng này.
        }

        if (pressedKeys.contains(KeyCode.LEFT)) {
            paddle.setX(paddle.getX() - paddle.getSpeed());
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            paddle.setX(paddle.getX() + paddle.getSpeed());
        }
        paddle.collisionHandling();

        if (isAiming || isBuffBullet) {
            aimingBall.inPaddle(paddle.getX(), paddle.getWidth());
            if (pressedKeys.contains(KeyCode.SPACE)) {
                if (isAiming) {
                    listBalls.addNewBall(aimingBall.getBallX(), aimingBall.getBallY());
                    isAiming = false;
                }
                if (isBuffBullet) {
                    listBalls.buffBullet(aimingBall.getBallX(), aimingBall.getBallY());
                    isBuffBullet = false;
                }
            }
        }
        if (blockSpawnTimer >= BLOCK_SPAWN_TIME) {
            listBlocks.addBlock();
            blockSpawnTimer = 0.0f;
        }
        if (listBlocks.getStateAboutToLose() == 4) {
            existingCoins += ManageBuff.extraCoins;
            GameStats.addGameSession(gameSession);
            isIngame = false;
            resetObject();
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

    //Thêm render.
    private void renderInGame(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(backGround1, 0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 1; i <= paddle.getLife(); i++) {
            gc.drawImage(LoadImage.getHeart(), 10 + (i - 1) * 25, 570, 20, 20);
        }
        paddle.addOnScene(gc);
        listBlocks.addListOnScene(gc, level);
        listBalls.addListOnScene(gc, paddle, listBlocks.getGameBlocks(), listBuffs, gameSession);
        boolean b = listBuffs.addBuffOnScene(gc, paddle, listBalls);
        if (!isBuffBullet) {
            isBuffBullet = b;
        }
        if (isAiming || isBuffBullet) {
            aimingBall.addOnScene(gc);
        }
        gc.drawImage(backGround1, 0, 0, backGround1.getWidth(), backGround1.getHeight() * 65 / 600,
                0, 0, 800, 65);
        gc.setFill(Color.color(0, 0, 0, 0.5));
        gc.fillRect(0, 0, HelloApplication.WIDTH, 65);
        if (level >= 4) {
            gameSession.renderClock(gc, listBlocks.getStateAboutToLose());
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
