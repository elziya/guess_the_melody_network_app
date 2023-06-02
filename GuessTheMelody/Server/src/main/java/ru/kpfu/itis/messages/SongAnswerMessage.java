package ru.kpfu.itis.messages;

public class SongAnswerMessage extends Message<String> {
    private String songNameAnswer;
    private int senderId;

    public SongAnswerMessage(String  songNameAnswer, int senderId) {
        this.songNameAnswer = songNameAnswer;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.SEND_ANSWER;
    }

    @Override
    public String getContent() {
        return songNameAnswer;
    }

    @Override
    public int getSenderId() {
        return -1;
    }
}

