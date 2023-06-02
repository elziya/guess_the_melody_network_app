package ru.kpfu.itis.messages;

public class JoinRoomMessage extends Message<Integer> {

    private final int roomId;
    private final int playerId;

    public JoinRoomMessage(int roomId, int playerId) {
        this.roomId = roomId;
        this.playerId = playerId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.JOIN_ROOM;
    }

    @Override
    public Integer getContent() {
        return roomId;
    }

    @Override
    public int getSenderId() {
        return playerId;
    }
}

