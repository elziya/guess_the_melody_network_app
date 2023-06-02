package ru.kpfu.itis.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import ru.kpfu.itis.GuessTheMelodyApp;
import ru.kpfu.itis.services.DownloadService;


public class LoadController {
    private final DownloadService serviceExample = new DownloadService();
    @FXML
    public ProgressIndicator progressIndicator;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public Text loadText;

    private GuessTheMelodyApp parent;

    public void setParent(GuessTheMelodyApp parent) {
        this.parent = parent;
    }

    @FXML
    void initialize() {
        initialProgress();
        serviceExample.restart();
    }

    public void changeMessage(String message){
        if(message != null){
            this.loadText.setText(message);
        }
    }

    private void initialProgress(){

        progressIndicator.visibleProperty().bind(serviceExample.runningProperty());
        serviceExample.setOnSucceeded(event -> {
            parent.setScene(SceneTypes.MAIN);
        });
    }

    // вызываем этот метод когда комната с игроками создалась
    public void closeService(){
        serviceExample.disableProgress();
    }
}

