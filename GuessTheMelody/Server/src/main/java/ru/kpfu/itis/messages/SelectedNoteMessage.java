package ru.kpfu.itis.messages;

import ru.kpfu.itis.models.Song;

public class SelectedNoteMessage extends Message<Song> {

    private final Song song;
    private final int playerId;

    public SelectedNoteMessage(Song song, int playerId) {
        this.song = song;
        this.playerId = playerId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.NOTE_SELECTED;
    }

    @Override
    public Song getContent() {
        return song;
    }

    @Override
    public int getSenderId() {
        return playerId;
    }
}
