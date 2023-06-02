package ru.kpfu.itis.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.kpfu.itis.GuessTheMelodyApp;

public class RegisterController {

    @FXML
    public TextField nickName;

    private GuessTheMelodyApp parent;

    public void setParent(GuessTheMelodyApp parent) {
        this.parent = parent;
    }

    @FXML
    public void setNickname(ActionEvent event){
        checkUsername(nickName);
        parent.startSession(nickName.getText());

    }

    private void checkUsername(TextField name){
        if (name.getText() == null || name.getText().equals("")){
            name.setText("user");
        }
    }

}

