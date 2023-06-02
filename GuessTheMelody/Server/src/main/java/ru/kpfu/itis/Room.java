package ru.kpfu.itis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.db.services.CategoryService;
import ru.kpfu.itis.exceptions.ServerConnectionException;
import ru.kpfu.itis.game.Game;
import ru.kpfu.itis.game.GuessMelodyGame;
import ru.kpfu.itis.messages.*;
import ru.kpfu.itis.models.Song;
import ru.kpfu.itis.network.Connection;
import ru.kpfu.itis.network.Notifiable;
import ru.kpfu.itis.network.RoomInfo;
import ru.kpfu.itis.network.UserInformation;

import java.io.IOException;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room implements Notifiable {
    private HashMap<Integer, Connection> connections;
    private MelodyServer server;
    private RoomInfo roomInfo;
    private Game game;
    private CategoryService categoryService;

    public Room(int id, int currentSize, MelodyServer server, CategoryService categoryService) {
        roomInfo = new RoomInfo(id, currentSize);
        connections = new HashMap<>();
        this.server = server;
        this.categoryService = categoryService;
    }

    public void addConnection(Connection connection){
        connection.setNotifier(this);

        connection.getPlayerInformation().setRoomId(roomInfo.getRoomId());
        connections.put(connection.getPlayerId(),connection);

        sendToPlayerConnection(connection.getPlayerId(), new JoinRoomAnswerMessage(new ActionResult(true), roomInfo, -1));
        roomInfo.addMember(connection.getPlayerInformation());
        roomInfo.setCurrentSize(roomInfo.getCurrentSize() + 1);

        server.sendCurrentRoomListToAllRoomUsers();
    }

    public void startGame(){
        game = new GuessMelodyGame();
        game.start(this, getPlayers(), categoryService);
    }

    private Map<Integer, UserInformation> getPlayers(){
        Map<Integer,UserInformation> map = new HashMap<>();

        Collection<Connection> connectionsCol = connections.values();
        for(Connection connection : connectionsCol){
            map.put(connection.getPlayerId(), connection.getPlayerInformation());
        }
        return map;
    }

    public void sendToPlayerConnection(int connectionId, Message<?> message){
        try {
            Connection connection = connections.get(connectionId);
            if (connection.isConnected()) {
                connection.send(message);
            }
        } catch (IOException e) {
            throw new ServerConnectionException(e);
        }
    }

    public void sendToAllRoomsUsers(Message<?> message)  {
        Collection<Connection> connectionCol = connections.values();
        for (Connection con : connectionCol) {
            try {
                if (con.isConnected()) con.send(message);
            } catch (IOException e) {
                throw new ServerConnectionException(e);
            }
        }
    }

    public Collection<Connection> getConnectionsValues() {
        return connections.values();
    }

    @Override
    public void notifyMessageReceived(Message<?> message) throws IOException {
        switch (message.getType()) {
            case DISCONNECTED:
                System.out.println("User with id" + message.getSenderId() + " decided to disconnect");
                removeConnection(message.getSenderId());
                if(game != null){
                    sendToAllRoomsUsers(new ShowResultsMessage(-1));
                }
                break;
            case NOTE_SELECTED:
                System.out.println("User with id " + message.getSenderId() + "chose song");
                sendToAllRoomsUsers(new SelectedNoteMessage((Song) message.getContent(),message.getSenderId()));
                break;
            case CLICK_ANSWER_BUTTON:
                System.out.println("Получил сообщение о клике от игрока " + message.getSenderId());
                sendToAllRoomsUsers(new DisableAnswerButtonsMessage( -1));
                break;
            case UPDATE_USER_INFO:
                sendToAllRoomsUsers(new UpdateUserInfoMessage((RoomInfo) message.getContent(), -1));
                setRoomInfo((RoomInfo) message.getContent());
                break;
            case DEFAULT_SONG:
                sendToAllRoomsUsers(new DefaultSongMessage((Integer) message.getContent(), -1));
                break;
        }
    }

    public List<UserInformation> getRivals(int playerId) {
        Collection<Connection> rivals = connections.values();
        List<UserInformation> rivalsInfo = new ArrayList<>();
        for (Connection c: rivals) {
            if (c.getPlayerInformation().getPlayerId() != playerId) {
                rivalsInfo.add(c.getPlayerInformation());
            }
        }
        return rivalsInfo;
    }

    public void removeConnection(int connectionId)  {
        Connection connection = connections.get(connectionId);
        if (connection == null) {
            return;
        }
        if (game != null) {
            game.playerDisconnected(connectionId);
        }

        connections.remove(connectionId);

        if (game == null) {
            roomInfo.setCurrentSize(roomInfo.getCurrentSize() - 1);
            roomInfo.removeMember(connectionId);
        }
    }
}

