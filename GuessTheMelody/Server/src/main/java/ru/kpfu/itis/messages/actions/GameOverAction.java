package ru.kpfu.itis.messages.actions;

import java.util.Collection;

public class GameOverAction extends Action<String> {

    private final Collection<String> systemMessages;
    private final String message;

    public GameOverAction(Collection<String> messages, String message) {
        this.systemMessages = messages;
        this.message = message;
    }

    @Override
    public ActionTypes getType() {
        return ActionTypes.GAME_OVER;
    }

    @Override
    public Collection<String> getSystemMessages() {
        return systemMessages;
    }

    @Override
    public String getContent() {
        return message;
    }
}
