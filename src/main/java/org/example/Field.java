package org.example;

public class Field {
    public int row;
    public int col;

    public Field(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Field getField(String notation) {
        if (notation == null || notation.length() != 2) {
            return null;
        }

        char column = Character.toUpperCase(notation.charAt(0));
        char row = notation.charAt(1);

        if (column < 'A' || column > 'H' || row < '1' || row > '8') {
            return null;
        }
        int col = column - 'A';
        int rowIndex = Character.getNumericValue(row) - 1;

        return new Field(rowIndex, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        var field = (Field) obj;

        return row == field.row && col == field.col;
    }
}
