package ru.kpfu.itis.models;

import lombok.*;
import ru.kpfu.itis.Room;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    private int id;
    private String name;
    private Status status;
    private Room room;
}

