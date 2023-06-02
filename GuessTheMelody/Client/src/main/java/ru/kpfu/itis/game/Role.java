package ru.kpfu.itis.game;

public enum Role {
    PLAYER("Участник игры, который может дать ответ"),
    VOTER("Игрок, который выбирает категорию песни. Также может дать ответ"),
    RESPONDER("Игрок, который отвечает на вопрос");

    private String description;
    Role(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

