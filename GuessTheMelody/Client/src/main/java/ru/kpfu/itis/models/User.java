package ru.kpfu.itis.models;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    private int id;
    private String name;
    private Status status;
}

