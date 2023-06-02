package ru.kpfu.itis.network;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RoomInfo implements Serializable {
    private int roomId;
    private int currentSize;
    private final Map<Integer, UserInformation> members;

    private static final long serialVersionUID = 1L;

    protected static final int CAPACITY = 3;

    public RoomInfo(int roomId, int currentSize){
        this.roomId = roomId;
        this.currentSize = currentSize;
        members = new HashMap<>();
    }

    public RoomInfo(String roomName){
        this.currentSize = 0;
        members = new HashMap<>();
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public Collection<UserInformation> getMembers(){
        return members.values();
    }

    public void addMember(UserInformation playerInformation) {
        if (members.size() < CAPACITY) {
            members.put(playerInformation.getPlayerId(), playerInformation);
        }
    }

    public Map<Integer, UserInformation> getMapOfMembers(){
        return members;
    }

    public int getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "RoomInfo{" +
                "roomId=" + roomId +
                ", currentSize=" + currentSize +
                ", members=" + members +
                '}';
    }
}

