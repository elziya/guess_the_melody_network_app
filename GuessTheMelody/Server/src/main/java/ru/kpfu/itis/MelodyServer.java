package ru.kpfu.itis;

import ru.kpfu.itis.db.DatabaseConnection;
import ru.kpfu.itis.db.repositories.CategoryRepository;
import ru.kpfu.itis.db.repositories.CategoryRepositoryJdbcTemplateImpl;
import ru.kpfu.itis.db.services.CategoryService;
import ru.kpfu.itis.db.services.CategoryServiceImpl;
import ru.kpfu.itis.messages.ListRoomMessage;
import ru.kpfu.itis.messages.JoinRoomMessage;
import ru.kpfu.itis.messages.Message;
import ru.kpfu.itis.messages.RoomUpdateInfoMessage;
import ru.kpfu.itis.network.Connection;
import ru.kpfu.itis.network.Notifiable;
import ru.kpfu.itis.network.UserConnection;
import ru.kpfu.itis.network.RoomInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class MelodyServer implements Notifiable, IServer {
    private final HashMap<Integer, Room> rooms;
    private final HashMap<Integer, Connection> connections;
    private final ServerSocket serverSocket;
    private final int SERVER_PORT = Protocol.port;
    private int lastUserId = 1;
    private int lastRoomId = 1;
    private final CategoryService categoryService;

    public MelodyServer() throws IllegalStateException{
        connections = new HashMap<>();
        rooms = new HashMap<>();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        CategoryRepository categoryRepository = new CategoryRepositoryJdbcTemplateImpl(databaseConnection.getDataSource());
        categoryService = new CategoryServiceImpl(categoryRepository);
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        }
        catch (IOException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public void start(){
        System.out.println("Server started");
        while (true){
            try {
                System.out.println("Waiting");
                Socket socket = serverSocket.accept();
                System.out.println("Connected");
                addConnection(socket);
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
    }

    @Override
    public void addConnection(Socket socket){
        System.out.println("Is going to add connection");
        UserConnection connection = new UserConnection(socket, this, lastUserId++);
        System.out.println("Created player connection");
        connections.put(connection.getPlayerId(), connection);
        System.out.println("Add: " + connection.getPlayerId() + " " + connection);
        ListRoomMessage listRoomMessage = new ListRoomMessage(getAllRoomsInfo(), connection.getPlayerId());
        sendToConnection(connection.getPlayerId(), listRoomMessage);
        System.out.println("Connected player Id: " + (connection.getPlayerId() + " PlayerName: " + connection.getPlayerInformation().getUsername()));
    }

    // connectionId = playerId
    private void sendToConnection(int connectionId, Message<?> message){
        try {
            Connection con = connections.get(connectionId);
            if (con.isConnected()) con.send(message);
        } catch (IOException e) {
            throw new IllegalStateException("Can't send to connection", e);
        }
    }

    public Collection<RoomInfo> getAllRoomsInfo() {
        Collection<RoomInfo> res = new ArrayList<>();
        for (Room r: rooms.values()) {
            res.add(r.getRoomInfo());
        }
        return res;
    }

    @Override
    public void notifyMessageReceived(Message<?> message)  {
        System.out.println("Got message with type  "+ message.getType().name());
        switch (message.getType()){
            case JOIN_ROOM:
                System.out.println("Going to add user to the room..." );
                joinRoom((JoinRoomMessage)message);
                break;
            case DISCONNECTED:
                removeConnection(message.getSenderId());
                break;

        }
    }

    public void removeRoom(int roomId) {
        rooms.remove(roomId);
        sendCurrentRoomListToAllRoomUsers();
    }

    @Override
    public void removeConnection(int id){
        Connection connection = connections.get(id);
        if (connection == null) {
            return;
        }
        Room room = rooms.get(connection.getPlayerInformation().getRoomId());
        if (room != null) {
            room.removeConnection(id);
        }
        System.out.println("User with id:" + connection.getPlayerInformation().getPlayerId() + " name: "
                + connection.getPlayerInformation().getUsername() + " Room : "+  connection.getPlayerInformation().getRoomId() + "disconnected");
        connections.remove(id);
    }

    public void joinRoom(JoinRoomMessage message){

        int senderId = message.getSenderId();
        Connection connection = connections.get(senderId);
        if (rooms.size() == 0) {
            createNewRoom();
        }
        if (rooms.get(lastRoomId).getRoomInfo().getCurrentSize() < 3) {
            rooms.get(lastRoomId).addConnection(connection);
            if (rooms.get(lastRoomId).getRoomInfo().getCurrentSize() == 3) {
                rooms.get(lastRoomId).sendToAllRoomsUsers(new RoomUpdateInfoMessage( rooms.get(lastRoomId).getRoomInfo(), -1));
                rooms.get(lastRoomId).startGame();
            }
        } else {
            lastRoomId++;
            createNewRoom();
            rooms.get(lastRoomId).addConnection(connection);
        }
        printRoomsStatistics();
    }

    private void createNewRoom() {
        Room room = new Room(lastRoomId, 0, this, categoryService);
        rooms.put(lastRoomId, room);
    }

    public void sendCurrentRoomListToAllRoomUsers() {
        sendToAllRoomUsers(new ListRoomMessage(getRoomInfoList(),0));
    }

    public Collection<RoomInfo> getRoomInfoList() {
        List<RoomInfo> res = new ArrayList<>();
        for (Room r: rooms.values()) {
            res.add(r.getRoomInfo());
        }
        return rooms.values().stream().map(Room::getRoomInfo).collect(Collectors.toList());
    }

    public void sendToAllRoomUsers(Message<?> message) {
        Collection<Connection> connectionCollection = connections.values();
        for(Connection con : connectionCollection){
            try {
                if (con.isConnected()) con.send(message);
            }
            catch (IOException e){
                throw new IllegalStateException(e);
            }
        }
    }

    private void printRoomsStatistics() {
        for(Room room : rooms.values()){
            RoomInfo info = room.getRoomInfo();
            System.out.println("Id: " + info.getRoomId() + " capacity: " + info.getCapacity() + " currentSize: " + info.getCurrentSize());
            System.out.println("Current users: ");
            for (Connection connection : room.getConnectionsValues()){
                System.out.println("id: " + connection.getPlayerId() + " username: " + connection.getPlayerInformation().getUsername());
            }
        }
    }
}

