package ru.kpfu.itis.messages;

public class StageMusicMessage extends Message<ru.kpfu.itis.models.Stage> {

    private int senderId;
    private ru.kpfu.itis.models.Stage stage;

    public StageMusicMessage(ru.kpfu.itis.models.Stage stage, int senderId) {
        this.stage = stage;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.STAGE_MUSIC;
    }

    @Override
    public ru.kpfu.itis.models.Stage getContent() {
        return stage;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}

