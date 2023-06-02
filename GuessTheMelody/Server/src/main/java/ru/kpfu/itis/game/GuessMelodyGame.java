package ru.kpfu.itis.game;

import ru.kpfu.itis.Room;
import ru.kpfu.itis.db.services.CategoryService;
import ru.kpfu.itis.exceptions.ThreadSleepException;
import ru.kpfu.itis.messages.PreparedRoomMessage;
import ru.kpfu.itis.messages.StageMusicMessage;
import ru.kpfu.itis.models.Stage;
import ru.kpfu.itis.network.UserInformation;

import java.util.Map;

public class GuessMelodyGame implements Game, Runnable {
    private Room room;
    private Map<Integer, UserInformation> players;
    private CategoryService categoryService;
    protected final String FIRST_STAGE = "1 ЭТАП";
    protected final int SLEEP_TIME = 1000;
    private int currentStage;

    @Override
    public void start(Room room, Map<Integer, UserInformation> players, CategoryService categoryService) {
        this.room = room;
        this.players = players;
        this.categoryService = categoryService;
        this.currentStage = 1;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Stage stage = Stage.builder()
                    .id(1)
                    .name(FIRST_STAGE)
                    .categories(categoryService.findCategoriesForFirstStage())
                    .build();

            StageMusicMessage stageMusicMessage = new StageMusicMessage(stage, -1);

            room.sendToAllRoomsUsers(stageMusicMessage);
            Thread.sleep(SLEEP_TIME);

            PreparedRoomMessage preparedRoomMessage = new PreparedRoomMessage(-1);
            room.sendToAllRoomsUsers(preparedRoomMessage);
        } catch (InterruptedException exception) {
            throw new ThreadSleepException(exception);
        }
    }

    @Override
    public void playerDisconnected(int id) {
        if (players.get(id) == null) {
            return;
        }
        players.remove(id);
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }
}
