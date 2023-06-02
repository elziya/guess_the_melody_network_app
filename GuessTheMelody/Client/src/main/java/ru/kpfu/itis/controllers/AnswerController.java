package ru.kpfu.itis.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ru.kpfu.itis.models.Song;
import ru.kpfu.itis.services.ColorAnimationService;

public class AnswerController {
    private final Color FROM_COLOR = Color.rgb(255, 160, 0);
    private final Color TO_COLOR = Color.rgb(245, 124, 0);
    public Text tvMinute;
    public Text tvSecond;
    public TextField etAnswer;
    public Button btnSend;
    public AnchorPane answerPanel;
    public BorderPane backgroundPane;

    private int timer;
    private MainController owner;
    private Song song;
    private boolean isAnswered;

    @FXML
    private void initialize() {
        isAnswered = false;
    }

    public void setData(MainController owner, int timer, Song song){
        this.owner = owner;
        this.timer = timer;
        this.song = song;
        startAnimation();
        System.out.println(song.getName().toLowerCase());
    }

    private void startAnimation(){
        new ColorAnimationService(backgroundPane, FROM_COLOR, TO_COLOR, 1).start();
        createTimerAnimation(timer);
    }

    private void setSeconds(int sec){
        int s = sec % 60;
        if (s < 10) tvSecond.setText("0" + s);
        else tvSecond.setText(String.valueOf(s));
    }

    private void setMinutes(int min){
        int m = min / 60;
        if (m < 10) tvMinute.setText("0" + m);
        else tvMinute.setText(String.valueOf(m));
    }

    @FXML
    private void sendAnswer(ActionEvent actionEvent) {
        isAnswered = true;
        processAnswer(etAnswer.getText());
        hideWindow();
    }

    public void hideWindow(){
        if(!isAnswered) {
            processAnswer("");
        }
        owner.stopMedia();
        answerPanel.getScene().getWindow().hide();
    }

    private void processAnswer(String answer) {
        boolean correct = answer.toLowerCase().equals(song.getName().toLowerCase());
        owner.sendAnswer(song.getPoints(), correct);
    }

    private void createTimerAnimation(int seconds){
        new Task<Void>() {
            @Override
            protected Void call(){
                int[] sec = {seconds};
                setMinutes(sec[0]);
                setSeconds(sec[0]);
                Timeline timeline = new Timeline(
                        new KeyFrame(
                                Duration.millis(1000),
                                ae -> {
                                    sec[0]--;
                                    setMinutes(sec[0]);
                                    setSeconds(sec[0]);
                                }
                        )
                );
                timeline.setCycleCount(seconds);
                timeline.setOnFinished(event -> hideWindow());
                timeline.play();
                return null;
            }
        }.call();
    }
}

