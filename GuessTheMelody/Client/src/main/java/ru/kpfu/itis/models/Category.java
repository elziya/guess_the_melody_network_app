package ru.kpfu.itis.models;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category implements Serializable {
    private int id;
    private String name;
    private List<Song> songs;
}

