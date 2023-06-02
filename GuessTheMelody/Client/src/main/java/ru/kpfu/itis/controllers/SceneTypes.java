package ru.kpfu.itis.controllers;

public enum SceneTypes {

    START("/assets/startScene.fxml"),
    LOAD("/assets/loadScene.fxml"),
    REGISTER("/assets/registerScene.fxml"),
    MAIN("/assets/mainScene.fxml"),
    RESULTS("/assets/resultScene.fxml");

    private String title;

    SceneTypes(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

