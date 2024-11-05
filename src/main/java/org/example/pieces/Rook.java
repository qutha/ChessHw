package org.example.pieces;

import org.example.Board;
import org.example.Color;
import org.example.Field;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    private static final int[][] directions = new int[][] {
            { -1, 0 },
            { 0, -1 }, { 0, 1 },
            { 1, 0 }
    };

    private boolean isFirstMove = true;
    public Rook(Color color) {
        super(color);
    }

    @Override
    public char getSymbol() {
        return switch (color) {
            case WHITE -> '♜';
            case BLACK -> '♖';
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
            int colDirections = directions[i][1];
            moves.addAll(getMovesByDirection(board, currentField, rowDirection, colDirections, isIncludedOurPiece));
        }

        return moves;
    }

    private List<Field> getMovesByDirection(Board board, Field currentField,
                                            int rowDirection, int colDirection, boolean isIncludedOurPiece) {

        var moves = new ArrayList<Field>();
        int row = currentField.row + rowDirection;
        int col = currentField.col + colDirection;
        var targetField = new Field(row, col);

        while (Board.isInRange(targetField)) {
            var pieceInTargetField = board.getSquare(targetField).getPiece();
            if (pieceInTargetField == null) {
                moves.add(targetField);
            } else {
                if (pieceInTargetField.getColor() != getColor()) {
                    moves.add(targetField);
                } else if (isIncludedOurPiece) {
                    moves.add(targetField);
                }
                break;
            }
            row += rowDirection;
            col += colDirection;
            targetField = new Field(row, col);
        }

        return moves;
    }

    public boolean isMoved() {
        return moved;
    }
}
