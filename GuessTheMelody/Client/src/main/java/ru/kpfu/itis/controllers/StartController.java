package ru.kpfu.itis.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import ru.kpfu.itis.GuessTheMelodyApp;

public class StartController {

    @FXML
    public AnchorPane anchorPane;

    private GuessTheMelodyApp parent;

    public void setParent(GuessTheMelodyApp parent) {
        this.parent = parent;
    }

    @FXML
    public void startBtnClicked(ActionEvent event) {
       parent.setScene(SceneTypes.REGISTER);
    }
}

