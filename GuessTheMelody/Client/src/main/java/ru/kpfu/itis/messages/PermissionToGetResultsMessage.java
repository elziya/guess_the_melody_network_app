package ru.kpfu.itis.messages;

public class PermissionToGetResultsMessage extends Message<Boolean> {

    private final Boolean content;
    private int senderId;

    public PermissionToGetResultsMessage(Boolean ststus, int senderId) {
        this.content = ststus;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.SEND_PERMISSION_TO_GET_RESULTS;
    }

    @Override
    public Boolean getContent() {
        return content;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
