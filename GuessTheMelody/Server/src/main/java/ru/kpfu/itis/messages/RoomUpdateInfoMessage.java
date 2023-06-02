package ru.kpfu.itis.messages;

import ru.kpfu.itis.network.RoomInfo;

public class RoomUpdateInfoMessage extends Message<RoomInfo> {

    private final RoomInfo info;
    private final int playerId;

    public RoomUpdateInfoMessage(RoomInfo info, int playerId) {
        this.info = info;
        this.playerId = playerId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.UPDATE_ROOM_INFO;
    }

    @Override
    public RoomInfo getContent() {
        return info;
    }

    @Override
    public int getSenderId() {
        return playerId;
    }
}
