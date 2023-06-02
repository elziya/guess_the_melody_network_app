package ru.kpfu.itis.models;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "media")
public class Song implements Serializable {

    private static final long serialVersionUID = 2L;
    private int id;
    private byte[] media;
    private String name;
    private int points;
    private List<Category> categories;
    private String url;
}

