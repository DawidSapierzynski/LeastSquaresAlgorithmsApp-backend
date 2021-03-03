package pl.edu.wat.wcy.isi.app.core;

import lombok.Getter;

@Getter
public enum DistanceX {
    EQUIDISTANT("equidistant"),
    NORMAL("normal");

    private final String name;

    DistanceX(String name) {
        this.name = name;
    }
}
