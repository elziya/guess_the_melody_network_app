package ru.kpfu.itis.messages;

import java.io.Serializable;

public class ActionResult implements Serializable {
    private boolean isSuccessful;
    private String description;

    public ActionResult(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public ActionResult(boolean isSuccessful, String description) {
        this.isSuccessful = isSuccessful;
        this.description = description;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getDescription() {
        return description;
    }
}

