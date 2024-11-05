package org.example;

import org.example.pieces.Piece;

public class Square {
    private Piece piece;
    private boolean isSelected;

    public boolean isBeaten = false;

    public Square(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void deselect() {
        isSelected = false;
    }

    public void select() {
        isSelected = true;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
