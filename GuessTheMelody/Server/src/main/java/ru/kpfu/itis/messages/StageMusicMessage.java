package ru.kpfu.itis.messages;

import ru.kpfu.itis.models.Stage;

public class StageMusicMessage extends Message<Stage> {

    private int senderId;
    private Stage stage;

    public StageMusicMessage(Stage stage, int senderId) {
        this.stage = stage;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.STAGE_MUSIC;
    }

    @Override
    public Stage getContent() {
        return stage;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
