package ru.kpfu.itis.messages;

public class ShowResultsMessage extends Message<Void>{
    private Integer senderId;

    public ShowResultsMessage(Integer senderId){
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.SHOW_RESULTS;
    }

    @Override
    public Void getContent() {
        return null;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
