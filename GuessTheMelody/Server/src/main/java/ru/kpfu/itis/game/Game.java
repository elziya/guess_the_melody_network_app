package ru.kpfu.itis.game;

import ru.kpfu.itis.Room;
import ru.kpfu.itis.db.services.CategoryService;
import ru.kpfu.itis.network.UserInformation;

import java.util.Map;

public interface Game {
    void start(Room room, Map<Integer, UserInformation> players, CategoryService categoryService);
    void playerDisconnected(int id);
}
