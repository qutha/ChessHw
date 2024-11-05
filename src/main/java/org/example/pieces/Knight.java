package org.example.pieces;

import org.example.Board;
import org.example.Color;
import org.example.Field;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    private static final int[][] directions = new int[][] {
            { 1, -2 }, { 1, 2 },
            { -1, -2 }, { -1, 2 },
            { 2, -1 }, { 2, 1 },
            { -2, -1 }, { -2 , 1}
    };
    public Knight(Color color) {
        super(color);
    }

    @Override
    public char getSymbol() {
        return switch (color) {
            case WHITE -> '♞';
            case BLACK -> '♘';
        };
    }

    @Override
    public boolean canMove(Board board, Field from, Field to) {
        List<Field> possibleMoves = getMoves(board, from);
        return possibleMoves.contains(to);
    }

    @Override
    public List<Field> getMoves(Board board, Field currentField) {

        return getMoves(board, currentField, false);
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

        var targetField = new Field(currentField.row + rowDirection, currentField.col + colDirection);
        if (!Board.isInRange(targetField)) {
            return null;
        }
        if (board.isFieldOccupied(targetField)) {
            var pieceInTargetField = board.getSquare(targetField);
            if (pieceInTargetField.getPiece().color == color) {
                if (isIncludedOurPiece) {
                    return targetField;
                }
                return null;
            } else {
                return targetField;
            }
        }
        return targetField;
    }
}
