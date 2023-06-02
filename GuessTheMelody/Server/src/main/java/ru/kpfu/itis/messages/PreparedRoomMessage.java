package ru.kpfu.itis.messages;

public class PreparedRoomMessage extends Message {
    private int senderId;

    public PreparedRoomMessage(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.PREPARED_ROOM_MESSAGE;
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
