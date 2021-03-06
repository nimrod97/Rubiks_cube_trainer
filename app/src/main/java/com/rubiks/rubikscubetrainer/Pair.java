package com.rubiks.rubikscubetrainer;

// The class represents a pair of two objects.
public class Pair<F, S> {
    private final F first;
    private final S second;

    public Pair(F _f, S _s) {
        first = _f;
        second = _s;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
