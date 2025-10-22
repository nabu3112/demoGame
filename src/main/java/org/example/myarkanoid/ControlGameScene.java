package org.example.myarkanoid;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import object.ScenePlayGame;

public class ControlGameScene {
    ScenePlayGame scenePlayGame;

    @FXML private Canvas canvas;
    @FXML private Button mainButton;
    @FXML private Button btn1;
    @FXML private Button btn2;

    private boolean expanded = false;

    @FXML
    private void initialize() {
        scenePlayGame = new ScenePlayGame();
        scenePlayGame.runGame(canvas);
    }

    @FXML
    public void backToMenu(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/MyArkanoid/hello-view.fxml"));
            Scene menuScene = new Scene(loader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(menuScene);
            stage.setTitle("My Game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMainButtonClick() {
        if (!expanded) {
            showButtons();
            scenePlayGame.pauseGame();
        } else {
            hideButtons();
            canvas.requestFocus();
            scenePlayGame.resumeGame();
        }
        expanded = !expanded;
    }

    private void showButtons() {
        animateButton(btn1, 30, true);
        animateButton(btn2, 60, true);
    }

    private void hideButtons() {
        animateButton(btn1, 0, false);
        animateButton(btn2, 0, false);
    }

    private void animateButton(Button btn, double translateY, boolean visible) {
        TranslateTransition move = new TranslateTransition(Duration.millis(250), btn);
        move.setToY(translateY);

        FadeTransition fade = new FadeTransition(Duration.millis(250), btn);
        fade.setToValue(visible ? 1 : 0);

        ParallelTransition pt = new ParallelTransition(move, fade);

        // Đảm bảo ẩn hẳn khỏi layout khi xong animation
        pt.setOnFinished(e -> btn.setVisible(visible));

        btn.setVisible(true); // cần bật lại nếu đang ẩn
        pt.play();
    }
}
