package ru.kpfu.itis.messages;

public class ClickAnswerButtonMessage extends Message {
    private int senderId;

    public ClickAnswerButtonMessage(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.CLICK_ANSWER_BUTTON;
    }

    @Override
    public Object getContent() {
        return null;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}

