package org.example;

import org.example.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Color currentColor = Color.WHITE;
    private Square[][] squares;
    private Move lastMove;

    private List<Field> beatenFields = null;

    private Field whiteKingField = null;
    private Field blackKingField = null;

    public Board() {
        squares = new Square[8][8];
        initBoard();
    }

    public static boolean isInRange(Field field) {
        return field.row >= 0 && field.row < 8 && field.col >= 0 && field.col < 8;
    }

    public Square[][] getSquares() {
        return squares;
    }

    public Square getSquare(Field field) {
        if (!isInRange(field)) {
            return null;
        }
        return squares[field.row][field.col];
    }

    private void finishTurn() {
        currentColor = currentColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        beatenFields = getBeatenFields(currentColor);
    }

    public void selectAvailablePieces() {
        selectPieces(currentColor);
    }

    public Integer selectPiece(Field field) {
        var square = squares[field.row][field.col];
        var piece = square.getPiece();
        if (piece == null) {
            return 0;
        }

        ArrayList<Field> moves = new ArrayList<>();
        moves.add(field);
        if (piece.getColor() == getCurrentTurn()) {
            moves.addAll(piece.getMoves(this, field));
        }
        deselectSquares();
        selectSquares(moves);
        return moves.size();
    }

    private void deselectSquares() {
        for (var row = squares.length - 1; row >= 0; row--) {
            for (var col = squares[row].length - 1; col >= 0; col--) {
                var square = squares[row][col];
                square.deselect();
            }
        }
    }

    private void selectSquares(List<Field> coordinates) {
        for (var coordinate : coordinates) {
            squares[coordinate.row][coordinate.col].select();
        }
    }

    private void selectPieces(Color color) {
        for (var row = squares.length - 1; row >= 0; row--) {
            for (var col = squares[row].length - 1; col >= 0; col--) {
                var square = squares[row][col];
                var piece = square.getPiece();
                if (piece == null)
                    continue;
                var pieceColor = piece.getColor();
                if (pieceColor == color && !piece.getMoves(this, new Field(row, col)).isEmpty()) {
                    square.select();
                } else {
                    square.deselect();
                }
            }
        }
    }

    private void initBoard() {
        // Инициализация белых фигур
        squares[0][0] = new Square(new Rook(Color.WHITE));
        squares[0][1] = new Square(new Knight(Color.WHITE));
        squares[0][2] = new Square(new Bishop(Color.WHITE));
        squares[0][3] = new Square(new Queen(Color.WHITE));
        squares[0][4] = new Square(new King(Color.WHITE));
        squares[0][5] = new Square(new Bishop(Color.WHITE));
        squares[0][6] = new Square(new Knight(Color.WHITE));
        squares[0][7] = new Square(new Rook(Color.WHITE));

        // Инициализация белых пешек
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new Square(new Pawn(Color.WHITE));
        }

        // Инициализация чёрных фигур
        squares[7][0] = new Square(new Rook(Color.BLACK));
        squares[7][1] = new Square(new Knight(Color.BLACK));
        squares[7][2] = new Square(new Bishop(Color.BLACK));
        squares[7][3] = new Square(new Queen(Color.BLACK));
        squares[7][4] = new Square(new King(Color.BLACK));
        squares[7][5] = new Square(new Bishop(Color.BLACK));
        squares[7][6] = new Square(new Knight(Color.BLACK));
        squares[7][7] = new Square(new Rook(Color.BLACK));
        
        for (int i = 0; i < 8; i++) {
            squares[6][i] = new Square(new Pawn(Color.BLACK));
        }

        for (int row = 2; row <= 5; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new Square(null);
            }
        }
        whiteKingField = new Field(0, 4);
        blackKingField = new Field(7, 4);
        beatenFields = new ArrayList<>();
    }

    public boolean isCheck() {
        if (currentColor == Color.WHITE) {
            return beatenFields.contains(whiteKingField);
        } else {
            return beatenFields.contains(blackKingField);
        }
    }

    public boolean isFieldOccupied(Field field) {
        return squares[field.row][field.col].getPiece() != null;
    }

    public boolean move(Move move) {
        var from = move.getFrom();
        var square = squares[from.row][from.col];
        var piece = square.getPiece();
        if (currentColor != piece.getColor()) {
            deselectSquares();
            return false;
        }
        var to = move.getTo();

        if (!piece.canMove(this, from, to)) {
            System.out.println("Неверный ход");
            deselectSquares();
            return false;
        }
        if (piece instanceof Pawn pawn && pawn.isEnPassantMove(this, from, to)) {
            int capturedPawnRow = from.row;
            int capturedPawnCol = to.col;
            squares[capturedPawnRow][capturedPawnCol] = new Square(null);
        }

        if (piece instanceof King king) {
            if (currentColor == Color.WHITE) {
                whiteKingField.row = to.row;
                whiteKingField.col = to.col;
            } else {
                blackKingField.row = to.row;
                blackKingField.col = to.col;
            }
            Field rookField = null;
            Field rookMoveField = null;
            if (king.isShortCastlingMove(this, to)) {
                rookField = new Field(to.row, 7);
                rookMoveField = new Field(to.row, to.col - 1);
            } else if (king.isLongCastlingMove(this, to)) {
                rookField = new Field(to.row, 0);
                rookMoveField = new Field(to.row, to.col + 1);
            }
            if (rookField != null && rookMoveField != null) {
                var rook = getSquare(rookField).getPiece();
                rook.setMoved();
                squares[rookField.row][rookField.col] = new Square(null);
                squares[rookMoveField.row][rookMoveField.col] = new Square(rook);
            }
        }

        piece.setMoved();
        squares[to.row][to.col] = new Square(piece);
        squares[from.row][from.col] = new Square(null);
        deselectSquares();
        finishTurn();
        lastMove = move;
        return true;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public Color getCurrentTurn() {
        return currentColor;
    }

    private List<Field> getBeatenFields(Color currentColor) {
        var beatenFields = new ArrayList<Field>();

        for (int row = 0; row < squares.length; ++row) {
            for (int col = 0; col < squares[row].length; ++col) {
                squares[row][col].isBeaten = false;
                var piece = squares[row][col].getPiece();
                if (piece != null && piece.getColor() != currentColor) {
                    beatenFields.addAll(piece.getBeatenFields(this, new Field(row, col)));
                }
            }
        }
        for (int i = 0; i < beatenFields.size(); ++i) {
            var field = beatenFields.get(i);
            squares[field.row][field.col].isBeaten = true;
        }

        return beatenFields;
    }

    public boolean isBeatenField(Field field) {
        return beatenFields.contains(field);
    }
}
