package org.example;

public class Move {
    private final Field from;
    private final Field to;

    public Move(Field from, Field to) {
        this.from = from;
        this.to = to;
    }

    public Field getFrom() {
        return from;
    }

    public Field getTo() {
        return to;
    }
}
