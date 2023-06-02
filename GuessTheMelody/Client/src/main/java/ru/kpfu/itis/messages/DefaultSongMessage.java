package ru.kpfu.itis.messages;

public class DefaultSongMessage extends Message<Integer>{

    private int senderId;
    private Integer songId;

    public DefaultSongMessage(Integer songId, int senderId) {
        this.songId = songId;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.DEFAULT_SONG;
    }

    @Override
    public Integer getContent() {
        return songId;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}

