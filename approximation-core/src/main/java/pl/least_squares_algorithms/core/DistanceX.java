package pl.least_squares_algorithms.core;

import lombok.Getter;

@Getter
public enum DistanceX {
    EQUIDISTANT("equidistant"),
    NORMAL("normal"),
    CHEBYSHEV("chebyshev");

    private final String name;

    DistanceX(String name) {
        this.name = name;
    }
}
