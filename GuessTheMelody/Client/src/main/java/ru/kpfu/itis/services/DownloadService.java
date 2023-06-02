package ru.kpfu.itis.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DownloadService extends Service<Void> {

    private boolean isProgress = true;

    @Override
    protected Task<Void> createTask() {

        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                 while(isProgress){
                   Thread.sleep(10);
                 }
                return null;
            }
        };
    }

    public boolean disableProgress(){
        return isProgress=false;
    }
}

