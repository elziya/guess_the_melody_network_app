package ru.kpfu.itis;

import ru.kpfu.itis.controllers.SceneTypes;
import ru.kpfu.itis.exceptions.ServerConnectionException;
import ru.kpfu.itis.messages.*;
import ru.kpfu.itis.network.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MelodyClient implements Notifiable, IClient {
    private Connection connection;
    private int playerId;
    private final String username;
    private ClientApp clientApp;

    public MelodyClient(String username, ClientApp clientApp) {
        this.username = username;
        this.clientApp = clientApp;
    }

    @Override
    public void start() throws IllegalStateException {
        System.out.println("Is going to start client");
        try {
            connection = new UserConnection(new Socket(InetAddress.getByName(Protocol.host), Protocol.port), this,
                    new UserInformation(username));
            this.playerId = connection.getPlayerId();
            startPlaying();
        }
        catch (IOException e){
            throw new ServerConnectionException(e);
        }
    }

    @Override
    public void startPlaying() {
        sendMessage(new JoinRoomMessage(-1, playerId));
    }

    @Override
    public int getId() {
        return playerId;
    }

    @Override
    public void disconnect() {
        if (connection == null) {
            System.out.println("Connection is null");
            return;
        }
        System.out.println("Is going to send disconnect message");
        sendMessage(new DisconnectMessage(connection.getPlayerId()));
    }

    @Override
    public void notifyMessageReceived(Message<?> message) throws IOException {
        switch (message.getType()) {
           case JOIN_ROOM_ANSWER:
                System.out.println(message);
                break;
            case UPDATE_ROOM_INFO:
                System.out.println("Получаю сообщение об обновлении комнаты...");
                clientApp.updateRoomInfo(((RoomUpdateInfoMessage) message).getContent());
                clientApp.joinedRoom(true, ((RoomUpdateInfoMessage) message).getContent());
                clientApp.sendPreparingSceneTextWhileLoading();
                break;
            case STAGE_MUSIC:
                clientApp.startGame(((StageMusicMessage) message).getContent());
                break;
            case PREPARED_ROOM_MESSAGE:
                System.out.println("Получил сообщение о подготовленной сцене");
                clientApp.stopLoading();
                break;
            case UPDATE_USER_INFO:
                clientApp.updateRoomInfo((RoomInfo) message.getContent());
                break;
            case NOTE_SELECTED:
                System.out.println("Игрок с id "+ message.getSenderId() + " выбрал ноту");
                clientApp.startListenToMusic(((SelectedNoteMessage) message).getContent());
                break;
            case DISABLE_ANSWER_BUTTONS:
                System.out.println("Получил сообщение о блокировании кнопки");
                clientApp.disableAnswerButton();
                break;
            case DEFAULT_SONG:
                clientApp.playDefaultSong();
                break;
            case SHOW_RESULTS:
                clientApp.setScene(SceneTypes.RESULTS);
                break;
        }
    }

    @Override
    public void sendMessage(Message<?> message) {
        try {
            if (connection.isConnected()) {
                connection.send(message);
            }
        }
        catch (IOException e){
           throw new ServerConnectionException(e);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}

