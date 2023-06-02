package ru.kpfu.itis.network;

import lombok.*;
import ru.kpfu.itis.game.Role;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserInformation implements Serializable {
    private String username = "";
    private int roomId = -1;
    private int playerId;
    private Role role;
    private int points;

    public UserInformation(String username, int roomId, int playerId, int points) {
        this.username = username;
        this.roomId = roomId;
        this.playerId = playerId;
        this.points = points;
    }

    public UserInformation(int playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }

    public UserInformation(String username) {
        this.username = username;
    }
}

