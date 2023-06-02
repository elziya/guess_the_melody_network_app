package ru.kpfu.itis;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.kpfu.itis.controllers.*;
import ru.kpfu.itis.exceptions.CantGetResourceException;
import ru.kpfu.itis.exceptions.FileSavingException;
import ru.kpfu.itis.exceptions.ServerConnectionException;
import ru.kpfu.itis.messages.Message;
import ru.kpfu.itis.messages.PermissionToGetResultsMessage;
import ru.kpfu.itis.messages.SelectedNoteMessage;
import ru.kpfu.itis.models.Category;
import ru.kpfu.itis.models.Song;
import ru.kpfu.itis.network.RoomInfo;
import ru.kpfu.itis.network.UserInformation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class GuessTheMelodyApp extends Application implements ClientApp{
    private StartController startController;
    private LoadController loadController;
    private RegisterController registerController;
    private ResultController resultController;
    private MainController mainController;
    private IClient client;
    private Stage stage;
    private ru.kpfu.itis.models.Stage musicStage;
    private RoomInfo roomInfo;
    private String loadMessage = "Ожидание соперников";
    private final String SONGS_REPOSITORY = "C:\\Users\\Public\\ITIS\\WORK_X\\semester_work2_2021_prog\\GuessTheMelody\\Client\\src\\main\\resources\\music\\";

    private Collection<RoomInfo> currentRooms = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {

        this.stage = primaryStage;

        this.stage.setTitle(WindowProperties.APP_NAME);
        this.stage.setWidth(WindowProperties.WINDOW_WIDTH);
        this.stage.setHeight(WindowProperties.WINDOW_HEIGHT);
        this.stage.setMinWidth(WindowProperties.WINDOW_WIDTH);
        this.stage.setMinHeight(WindowProperties.WINDOW_HEIGHT);
        this.stage.setOnCloseRequest(event -> exit());
        setScene(SceneTypes.START);
        stage.show();
    }

    @Override
    public void startGame(ru.kpfu.itis.models.Stage stage) {
        musicStage = stage;

        String uuid;
        String url ;
        ArrayList<Category> categories = new ArrayList<>();
        for (Category category : stage.getCategories()){
            ArrayList<Song> songs = new ArrayList<>();
            for(Song song: category.getSongs() ){
                uuid = UUID.randomUUID().toString();
                url = (SONGS_REPOSITORY + uuid + ".mp3").replace("-", "_");
                song.setUrl(url);
                songs.add(song);
                try(FileOutputStream fileOutputStream = new FileOutputStream(url)){

                    fileOutputStream.write(song.getMedia());
                    fileOutputStream.flush();
                } catch (IOException e) {
                    throw new FileSavingException(e);
                }
            }
            category.setSongs(songs);
            categories.add(category);

        }
        musicStage.setCategories(categories);

    }

    @Override
    public void updateRoomInfo(RoomInfo roomInfo) {
        System.out.println("!!!!!!Обновил room info до: " + roomInfo);
        setRoomInfo(roomInfo);
        if (mainController != null) {
            System.out.println("updated");
            mainController.updatePlayerPoints();
        }
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    @Override
    public void startSession(String username) {
        try {
            client = new MelodyClient(username, this);
            client.start();
            setScene(SceneTypes.LOAD);
        }
        catch (IllegalStateException e){
            throw new ServerConnectionException(e);
        }
    }

    @Override
    public void exit() {
        System.out.println("I want to exit");
        disconnect();
        System.exit(0);
    }

    @Override
    public void disconnect() {
        if (client == null) {
            return;
        }
        client.disconnect();
    }

    @Override
    public void setScene(SceneTypes name) {
        try{
            FXMLLoader loader = new  FXMLLoader(getClass().getResource(name.getTitle()));
            Parent root = loader.load();

            switch (name){
                case START:
                    startController = loader.getController();
                    startController.setParent(this);
                    break;
                case REGISTER:
                    registerController = loader.getController();
                    registerController.setParent(this);
                    break;
                case LOAD:
                    loadController = loader.getController();
                    loadController.setParent(this);
                    loadController.changeMessage(loadMessage);
                    break;
                case MAIN:
                    mainController = loader.getController();
                    mainController.setParent(this);
                    mainController.setMembersInfo(roomInfo.getMembers());
                    mainController.setSceneWithMusic(musicStage);
                    mainController.playDefaultSong();
                    break;
                case RESULTS:
                    resultController = loader.getController();
                    resultController.setParent(this);
                    resultController.setGameOverResults(roomInfo);
                    break;
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    stage.setScene(new Scene(root));
                }
            });

        }
        catch (IOException exception){
            throw new CantGetResourceException(exception);
        }
    }

    @Override
    public void joinedRoom(boolean isSuccessful, RoomInfo info) {
        if(isSuccessful){
            roomInfo = info;
        }
    }

    public void stopLoading() {
        if (loadController != null) {
            loadController.closeService();
        }
    }

    public void sendSelectedNote(Song song){
        client.sendMessage(new SelectedNoteMessage(song,client.getId()));
    }

    @Override
    public void sendPreparingSceneTextWhileLoading() {
        loadMessage ="Идет приготовление комнаты";
        if (loadController != null) {
            loadController.changeMessage(loadMessage);
        }
    }

    @Override
    public void startListenToMusic(Song song) {
        if (mainController !=null){
            mainController.startListenToMusic(song);
        }
    }

    //главный метод для отправки сообщений с клиента
    public void sendMessage(Message<?> message){
        client.sendMessage(message);
    }

    // тут передаём номер комнаты, как отправителя
    public void sendPermissionToAcceptResults(){
        client.sendMessage(new PermissionToGetResultsMessage(true, roomInfo.getRoomId()));
        setScene(SceneTypes.RESULTS);
    }

    private List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        for(UserInformation userInfo: roomInfo.getMembers()){
            names.add(userInfo.getUsername());
        }
        return names;
    }

    public IClient getClient() {
        return client;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    @Override
    public void disableAnswerButton() {
        mainController.disableAnswerButton();
        mainController.stopMedia();
    }

    @Override
    public void playDefaultSong() {
        mainController.playDefaultSong();
    }

    public void showResults() {
        setScene(SceneTypes.RESULTS);
    }
}
