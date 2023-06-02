package ru.kpfu.itis.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.kpfu.itis.GuessTheMelodyApp;
import ru.kpfu.itis.exceptions.ThreadSleepException;
import ru.kpfu.itis.exceptions.UnknownIdException;
import ru.kpfu.itis.messages.ClickAnswerButtonMessage;
import ru.kpfu.itis.messages.DefaultSongMessage;
import ru.kpfu.itis.messages.UpdateUserInfoMessage;
import ru.kpfu.itis.models.Category;
import ru.kpfu.itis.models.Song;
import ru.kpfu.itis.network.UserInformation;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

public class MainController {
    protected final int SONG_COUNT = 3;
    protected final int TIMER = 30;
    protected final String DISABLE_IMAGE_PATH = "/images/note_disable.png";
    public AnchorPane mainScene;
    public ImageView note00;
    public ImageView note01;
    public ImageView note02;
    public ImageView note03;
    public ImageView note10;
    public ImageView note11;
    public ImageView note12;
    public ImageView note13;
    public ImageView note20;
    public ImageView note21;
    public ImageView note22;
    public ImageView note23;
    public ImageView note30;
    public ImageView note31;
    public ImageView note32;
    public ImageView note33;
    public Text category1;
    public Text category2;
    public Text category3;
    public Text category4;
    public Ellipse btnPlayer;
    public Text playerName1;
    public Text playerScore1;
    public Text playerName2;
    public Text playerScore2;
    public Text playerScore3;
    public Text playerName3;
    public Text points00;
    public Text points01;
    public Text points02;
    public Text points03;
    public Text points10;
    public Text points11;
    public Text points12;
    public Text points13;
    public Text points20;
    public Text points21;
    public Text points22;
    public Text points23;
    public Text points30;
    public Text points31;
    public Text points32;
    public Text points33;
    public BorderPane musicStagePane;
    ru.kpfu.itis.models.Stage musicStage;

    private GuessTheMelodyApp parent;
    private Map<String, Song> songs;
    private List<Category> categories;
    private Map<String, Boolean> songStatus;
    private MediaPlayer mediaPlayer;
    private List<UserInformation> members;
    private Map<UserInformation, Text> playersNames;
    private Map<UserInformation, Text> playersScores;
    private Song currentSong;
    private int count;

    @FXML
    private void initialize() {
        count = 0;
        playersNames = new HashMap<>();
        playersScores = new HashMap<>();
        playerScore1.setText("0");
        playerScore2.setText("0");
        playerScore3.setText("0");
        doMusicStageDisable();
    }

    public void setParent(GuessTheMelodyApp parent) {
        this.parent = parent;
    }

    public void doMusicStageDisable(){
        musicStagePane.setDisable(true);
    }

    public void doMusicStageAble(){
        musicStagePane.setDisable(false);
    }

    @FXML
    public void chooseNote(MouseEvent mouseEvent) throws IOException {
        String noteId = ((ImageView) mouseEvent.getSource()).getId();
        Song song = getSong(noteId);
        System.out.println("clientSong:" + song);
        parent.sendSelectedNote(song);
        startMusic(song);
        setNoteDisable(mouseEvent);
    }

    public void startListenToMusic(Song currSong){
        for (Map.Entry<String, Song> song : songs.entrySet()) {
            if(currSong.getId() == song.getValue().getId()){
                changeSongStatus(song.getKey());
                setImageDisable(getImageById(song.getKey()));
                break;
            }
        }
        startMusic(currSong);
    }

    // красная кнопка
    @FXML
    public void clickAnswerButton(MouseEvent mouseEvent) throws IOException {
        parent.sendMessage(new ClickAnswerButtonMessage(parent.getClient().getId()));
        openAnswerWindow(TIMER, currentSong);
        stopMedia();
    }

    public void setSceneWithMusic(ru.kpfu.itis.models.Stage stage){
        musicStage = stage;
        updateCategoriesList(musicStage.getCategories());
    }

    public void updateCategoriesList(List<Category> categories) {
        this.categories = categories;
        setData();
    }

    public void sendAnswer(int points, boolean correct) {
        UserInformation userInformation = parent.getClient().getConnection().getUserInformation();
        if (correct){
            userInformation.setPoints(userInformation.getPoints() + points);
            doMusicStageAble();
        }
        else {
            doMusicStageDisable();
            parent.sendMessage(new DefaultSongMessage(-1, parent.getClient().getId()));
        }
        parent.getRoomInfo().getMapOfMembers().put(parent.getClient().getId(), userInformation);
        parent.sendMessage(new UpdateUserInfoMessage(parent.getRoomInfo(), parent.getClient().getId()));
    }

    private void setNoteDisable(Event event) {
        ImageView source = (ImageView) event.getSource();
        currentSong = songs.get(source.getId());
        changeSongStatus(source.getId());
        setImageDisable(source);
    }

    private void setData() {
        setCategories();
        setSongs();
    }

    private void setCategories() {
        category1.setText(categories.get(0).getName());
        category2.setText(categories.get(1).getName());
        category3.setText(categories.get(2).getName());
        category4.setText(categories.get(3).getName());
    }

    public void updatePlayerPoints(){
        this.members = new ArrayList<>(parent.getRoomInfo().getMembers());
        putUserInfoInMap();
        setPlayerPoints();
        checkNumberOfPlayedSongs();
    }

    private void checkNumberOfPlayedSongs() {
        System.out.println("количество проигранных" + count);
        if(count >= SONG_COUNT){
            stopMedia();
            parent.showResults();
        }
    }

    public void setPlayerPoints(){
        for (Map.Entry<UserInformation, Text> map : playersScores.entrySet()){
            System.out.println("UPDATE POINTS");
            map.getValue().setText(String.valueOf(map.getKey().getPoints()));
            System.out.println(map.getKey().getPoints());
        }
    }

    private void setSongs() {
        Map<String, Song> map = new HashMap<>();
        songStatus = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        String name;
        Point point;
        Song song;
        for (Field field : fields) {
            name = field.getName();
            if (name.startsWith("note")) {
                point = parseNoteId(name);
                song = categories.get(point.x).getSongs().get(point.y);
                setSongPoints(String.valueOf(point.x) + point.y, song.getPoints());
                map.put(name, song);
            }
        }
        songs = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> songs.put(x.getKey(), x.getValue()));
        setSongsStatus();
    }

    private void setSongPoints(String coordinates, int points) {
        getTextView("points" + coordinates).setText(String.valueOf(points));
    }

    private Text getTextView(String id) {
        try {
            return (Text) MainController.class.getDeclaredField(id).get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new UnknownIdException(e);
        }
    }

    private void setSongsStatus() {
        songStatus = new HashMap<>();
        songs.forEach((key, value) -> songStatus.put(key, false));
    }

    private void changeSongStatus(String id) {
        songStatus.put(id, true);
    }

    public void playDefaultSong(){
        startMusic(getDefaultSong());
    }

    private Song getDefaultSong() {
        for (Map.Entry<String, Song> song : songs.entrySet()) {
            if (!isSongAlreadyPlayed(song.getKey())) {
                changeSongStatus(song.getKey());
                setImageDisable(getImageById(song.getKey()));
                currentSong = song.getValue();
                return song.getValue();
            }
        }
        return null;
    }

    private void startMusic(Song song){
        mediaPlayer = new MediaPlayer(new Media(new File(song.getUrl()).toURI().toString()));
        playMedia();
    }

    private void playMedia() {
        try {
            count++;
            enableAnswerButton();
            Thread.sleep(3000);
            mediaPlayer.play();
            beginTimer(TIMER);
        } catch (InterruptedException e) {
            throw new ThreadSleepException(e);
        }
    }

    public void stopMedia(){
        mediaPlayer.stop();
    }

    private void setImageDisable(ImageView view) {
        view.setDisable(true);
        view.setImage(new Image(DISABLE_IMAGE_PATH));
    }

    private ImageView getImageById(String id) {
        try {
            if(id != null){
                Field field = MainController.class.getDeclaredField(id);
                return (ImageView) field.get(this);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new UnknownIdException(e);
        }
        return null;
    }

    private boolean isSongAlreadyPlayed(String song) {
        return songStatus.get(song);
    }

    private Song getSong(String noteId) {
        return songs.get(noteId);
    }

    //открывается модальное окно для ввода ответа после наатия на кнопку
    public void openAnswerWindow(int timer, Song song) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/answerScene.fxml"));
        Parent root = loader.load();
        AnswerController answerController = loader.getController();
        answerController.setData(this, timer, song);
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(mainScene.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    private Point parseNoteId(String id) {
        int length = id.length();
        int x = Integer.parseInt(String.valueOf(id.charAt(length - 2)));
        int y = Integer.parseInt(String.valueOf(id.charAt(length - 1)));
        return new Point(x, y);
    }

    public void setMembersInfo(Collection<UserInformation> membersInfo) {
        this.members = new ArrayList<>(membersInfo);
        putUserInfoInMap();
        setPlayerNames();
    }

    private void putUserInfoInMap(){
        playersNames.put(members.get(0), playerName1);
        playersNames.put(members.get(1), playerName2);
        playersNames.put(members.get(2), playerName3);

        playersScores.put(members.get(0), playerScore1);
        playersScores.put(members.get(1), playerScore2);
        playersScores.put(members.get(2), playerScore3);
    }

    private void setPlayerNames(){
        for (Map.Entry<UserInformation, Text> map : playersNames.entrySet()){
            map.getValue().setText(map.getKey().getUsername());
        }
    }

    public void disableAnswerButton() {
        btnPlayer.setDisable(true);
    }

    public void enableAnswerButton() {
        btnPlayer.setDisable(false);
    }

    public void beginTimer(int secondsDuration) {
        if (secondsDuration > 0) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    double current = mediaPlayer.getCurrentTime().toSeconds();

                    if (current / secondsDuration >= 1) {
                        stopMedia();
                        timer.cancel();
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 0, 1000);
        }
    }
}

