package ru.kpfu.itis;

import ru.kpfu.itis.controllers.SceneTypes;
import ru.kpfu.itis.messages.Message;
import ru.kpfu.itis.models.Song;
import ru.kpfu.itis.models.Stage;
import ru.kpfu.itis.network.RoomInfo;

public interface ClientApp {

    void startGame(Stage stage);

    void startSession(String username);

    void disconnect();

    void exit();

    void setScene(SceneTypes name);

    void joinedRoom(boolean isSuccessful, RoomInfo info);

    void updateRoomInfo(RoomInfo info);

    void stopLoading();

    void sendPreparingSceneTextWhileLoading();

    void startListenToMusic(Song song);

    IClient getClient();

    void sendMessage(Message<?> message);
  
    RoomInfo getRoomInfo();

    void disableAnswerButton();

    void playDefaultSong();
}

