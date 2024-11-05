package org.example;

import org.example.pieces.Piece;

public class BoardView {
    private Board board;

    public BoardView(Board board) {
        this.board = board;
    }

    public void show() {
        var squares = board.getSquares();
        final String pieceSeparator = " ";

        final String charSeparator = "     ";
        final String nullPiece = " ";

        System.out.print(" " + charSeparator);
        for (var c = 'A'; c <= 'H'; c++) {
            System.out.print(pieceSeparator + c + charSeparator);
        }
        var currentTurn = board.getCurrentTurn();
        System.out.println();
        for (var row = squares.length - 1; row >= 0; row--) {
            System.out.print(row + 1 + pieceSeparator);
            for (var col = 0; col < squares[row].length; col++) {
                var square = squares[row][col];
                var piece = square.getPiece();
                var isPiece = piece != null;
                var color = getBackgroundColor(row, col, square.isSelected(), piece, currentTurn);
                if (!isPiece) {
                    System.out.print(color + pieceSeparator + nullPiece + pieceSeparator + ConsoleColors.RESET);
                    continue;
                }
                System.out.print(color + pieceSeparator + piece.getSymbol() + pieceSeparator + ConsoleColors.RESET);
            }
            System.out.println();
        }
    }

    private String getBackgroundColor(int row, int col, boolean isSelected, Piece piece, Color currentColor) {
        if (piece != null && isSelected) {
            if (piece.getColor() == currentColor) {
                return (row + col) % 2 == 1 ? ConsoleColors.BLUE_BACKGROUND_BRIGHT : ConsoleColors.BLUE_BACKGROUND;
            }
            return (row + col) % 2 == 1 ? ConsoleColors.RED_BACKGROUND_BRIGHT : ConsoleColors.RED_BACKGROUND;
        }

        if (isSelected) {
            return (row + col) % 2 == 1 ? ConsoleColors.GREEN_BACKGROUND_BRIGHT : ConsoleColors.GREEN_BACKGROUND;
        }

        return (row + col) % 2 == 1 ? ConsoleColors.WHITE_BACKGROUND : ConsoleColors.BLACK_BACKGROUND_BRIGHT;
    }
}
