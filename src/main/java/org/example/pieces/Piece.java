package org.example.pieces;

import org.example.Board;
import org.example.Color;
import org.example.Field;

import java.util.List;

public abstract class Piece {
    protected Color color;
    protected boolean moved;

    public Piece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setMoved() {
        moved = true;
    }

    public abstract char getSymbol();

    public abstract boolean canMove(Board board, Field from, Field to);

    public abstract List<Field> getMoves(Board board, Field currentField);

    public abstract List<Field> getBeatenFields(Board board, Field currentField);

    protected int getDirection() {
        return this.getColor() == Color.WHITE ? 1 : -1;
    }
}
