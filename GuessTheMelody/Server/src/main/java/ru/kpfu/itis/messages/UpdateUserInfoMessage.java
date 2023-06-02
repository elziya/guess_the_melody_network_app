package ru.kpfu.itis.messages;

import ru.kpfu.itis.network.RoomInfo;

public class UpdateUserInfoMessage extends Message<RoomInfo> {

    private int senderId;
    private RoomInfo roomInfo;

    public UpdateUserInfoMessage(RoomInfo roomInfo, int senderId) {
        this.roomInfo = roomInfo;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.UPDATE_USER_INFO;
    }

    @Override
    public RoomInfo getContent() {
        return roomInfo;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
