package ru.kpfu.itis.messages;

public class DisableAnswerButtonsMessage extends Message {
    private int senderId;

    public DisableAnswerButtonsMessage(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.DISABLE_ANSWER_BUTTONS;
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

