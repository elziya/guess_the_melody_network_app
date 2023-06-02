package ru.kpfu.itis.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import ru.kpfu.itis.GuessTheMelodyApp;
import ru.kpfu.itis.network.RoomInfo;
import ru.kpfu.itis.network.UserInformation;

import java.util.*;
import java.util.stream.Collectors;

public class ResultController {

    @FXML
    public Text namePlayer1;
    @FXML
    public Text namePlayer2;
    @FXML
    public Text namePlayer3;

    @FXML
    public Text firstPlacePoints;
    @FXML
    public Text secondPlacePoints;
    @FXML
    public Text thirdPlacePoints;

    private GuessTheMelodyApp parent;

    public void setParent(GuessTheMelodyApp parent) {
        this.parent = parent;
    }

    @FXML
    void initialize() {}

    @FXML
    public void setGameOverResults(RoomInfo roomInfo){
        List<UserInformation> userInfo = roomInfo.getMembers().stream()
                .sorted((o1, o2) -> o2.getPoints() - o1.getPoints())
                .collect(Collectors.toList());

        namePlayer1.setText(userInfo.get(0).getUsername());
        namePlayer2.setText(userInfo.get(1).getUsername());
        namePlayer3.setText(userInfo.get(2).getUsername());

        firstPlacePoints.setText(String.valueOf(userInfo.get(0).getPoints()));
        secondPlacePoints.setText(String.valueOf(userInfo.get(1).getPoints()));
        thirdPlacePoints.setText(String.valueOf(userInfo.get(2).getPoints()));
    }
}
