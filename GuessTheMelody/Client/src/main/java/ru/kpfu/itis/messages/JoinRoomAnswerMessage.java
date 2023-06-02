package ru.kpfu.itis.messages;

import ru.kpfu.itis.network.RoomInfo;

public class JoinRoomAnswerMessage extends Message<ActionResult>{
    private final ActionResult result;
    private final RoomInfo roomInfo;
    private final int playerId;

    public JoinRoomAnswerMessage(ActionResult result, RoomInfo roomInfo, int playerId) {
        this.result = result;
        this.roomInfo = roomInfo;
        this.playerId = playerId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.JOIN_ROOM_ANSWER;
    }

    @Override
    public ActionResult getContent() {
        return result;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    @Override
    public int getSenderId() {
        return playerId;
    }

    @Override
    public String toString() {
        return "JoinRoomAnswerMessage{" +
                "result=" + result +
                ", roomInfo=" + roomInfo +
                ", playerId=" + playerId +
                '}';
    }
}

