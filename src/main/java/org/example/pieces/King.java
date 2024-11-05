package org.example.pieces;

import org.example.Board;
import org.example.Color;
import org.example.Field;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private static final int[][] directions = new int[][] {
            { -1, -1 }, { -1, 0 }, { -1, 1 },
            { 0, -1 }, { 0, 1 },
            { 1, -1 }, { 1, 0 }, { 1, 1 }
    };

    public King(Color color) {
        super(color);
    }

    @Override
    public char getSymbol() {
        return switch (color) {
            case WHITE -> '♚';
            case BLACK -> '♔';
        };
    }

    @Override
    public boolean canMove(Board board, Field from, Field to) {
        List<Field> possibleMoves = getMoves(board, from);
        return possibleMoves.contains(to);
    }

    @Override
    public List<Field> getMoves(Board board, Field currentField) {
        var moves = getMoves(board, currentField, false);
        moves.addAll(getCastlingMoves(board));
        return moves;
    }

    @Override
    public List<Field> getBeatenFields(Board board, Field currentField) {
        return getMoves(board, currentField, true);
    }

    private List<Field> getMoves(Board board, Field currentField, boolean isIncludedOurPiece) {
        var moves = new ArrayList<Field>();

        for (int i = 0; i < directions.length; ++i) {
            int rowDirection = directions[i][0];
            int colDirection = directions[i][1];
            var field = getMoveByDirection(board, currentField, rowDirection, colDirection, isIncludedOurPiece);
            if (field != null) {
                moves.add(field);
            }
        }

        return moves;
    }

    private Field getMoveByDirection(Board board, Field currentField,
                                     int rowDirection, int colDirection, boolean isIncludedOurPiece) {

        int row = currentField.row + rowDirection;
        int col = currentField.col + colDirection;
        var targetField = new Field(row, col);
        if (!Board.isInRange(targetField)) {
            return null;
        }

        var pieceInTargetField = board.getSquare(targetField).getPiece();
        if (pieceInTargetField != null && pieceInTargetField.color == color) {
            if (isIncludedOurPiece) {
                return targetField;
            }
            return null;
        }
        if (board.getCurrentTurn() == color && board.isBeatenField(targetField)) {
            return null;
        }
        return targetField;
    }

    public List<Field> getCastlingMoves(Board board) {
        var castlingMoves = new ArrayList<Field>();

        var field = getShortCastlingMove(board);
        if (field != null) {
            castlingMoves.add(field);
        }
        field = getLongCastlingMove(board);
        if (field != null) {
            castlingMoves.add(field);
        }
        return castlingMoves;
    }

    public boolean isShortCastlingMove(Board board, Field to) {
        return to.equals(getShortCastlingMove(board));
    }

    public boolean isLongCastlingMove(Board board, Field to) {
        return to.equals(getLongCastlingMove(board));
    }

    public Field getShortCastlingMove(Board board) {
        if (isMoved() || board.isCheck()) {
            return null;
        }
        Field rookMoveField, kingMoveField, rookField;
        if (getColor() == Color.WHITE) {
            rookMoveField = new Field(0, 5);
            kingMoveField = new Field(0, 6);
            rookField = new Field(0, 7);
        } else {
            rookMoveField = new Field(7, 5);
            kingMoveField = new Field(7, 6);
            rookField = new Field(7, 7);
        }

        if (board.isFieldOccupied(rookMoveField) || board.isFieldOccupied(kingMoveField)) {
            return null;
        }
        if (board.isBeatenField(rookMoveField) || board.isBeatenField(kingMoveField)) {
            return null;
        }
        var piece = board.getSquare(rookField).getPiece();
        if (piece instanceof Rook rook && !rook.isMoved()) {
            return kingMoveField;
        } else {
            return null;
        }
    }

    public Field getLongCastlingMove(Board board) {
        if (isMoved() || board.isCheck()) {
            return null;
        }
        Field rookField, freeField, kingMoveField, rookMoveField;
        if (getColor() == Color.WHITE) {
            rookField = new Field(0, 0);
            freeField = new Field(0, 1);
            kingMoveField = new Field(0, 2);
            rookMoveField = new Field(0, 3);
        } else {
            rookField = new Field(7, 0);
            freeField = new Field(7, 1);
            kingMoveField = new Field(7, 2);
            rookMoveField = new Field(7, 3);
        }

        if (board.isFieldOccupied(rookMoveField) || board.isFieldOccupied(kingMoveField)
            || board.isFieldOccupied(freeField)) {

            return null;
        }
        if (board.isBeatenField(rookMoveField) || board.isBeatenField(kingMoveField)) {
            return null;
        }
        var piece = board.getSquare(rookField).getPiece();
        if (piece instanceof Rook rook && !rook.isMoved()) {
            return kingMoveField;
        } else {
            return null;
        }
    }

    public boolean isMoved() {
        return moved;
    }
}
