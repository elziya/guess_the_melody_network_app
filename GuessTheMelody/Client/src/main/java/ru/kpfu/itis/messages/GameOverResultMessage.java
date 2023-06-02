package ru.kpfu.itis.messages;

import java.util.HashMap;

public class GameOverResultMessage extends Message<HashMap<String,Integer>>{

    private final HashMap<String,Integer> result;
    private final int senderId;



    public GameOverResultMessage(HashMap<String, Integer> result, int senderId) {
        this.result = result;
        this.senderId = senderId;
    }


    @Override
    public MessageTypes getType() {
        return MessageTypes.SEND_GAME_OVER_RESULTS;
    }

    @Override
    public HashMap<String, Integer> getContent() {
        return result;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
