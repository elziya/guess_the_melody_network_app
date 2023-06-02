package ru.kpfu.itis.messages.actions;


import java.io.Serializable;
import java.util.Collection;

public abstract class Action<T> implements Serializable {

    public abstract ActionTypes getType();
    public abstract Collection<String> getSystemMessages();
    public abstract  T getContent();
}
