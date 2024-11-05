package org.example.pieces;

import org.example.Board;
import org.example.Color;
import org.example.Field;
import org.example.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
    }

    @Override
    public char getSymbol() {
        return switch (color) {
            case WHITE -> '♟';
            case BLACK -> '♙';
        };
    }

    @Override
    public boolean canMove(Board board, Field from, Field to) {
        List<Field> possibleMoves = getMoves(board, from);
        return possibleMoves.contains(to);
    }

    @Override
    public List<Field> getMoves(Board board, Field currentField) {
        var moves = new ArrayList<Field>();

        var oneForward = canMoveOneForward(board, currentField);
        if (oneForward != null) {
            moves.add(oneForward);
        }

        var twoForward = canMoveTwoForward(board, currentField);
        if (twoForward != null) {
            moves.add(twoForward);
        }

        var leftDiagonal = canMoveLeftDiagonal(board, currentField, true);
        if (leftDiagonal != null) {
            moves.add(leftDiagonal);
        }

        var rightDiagonal = canMoveRightDiagonal(board, currentField, true);
        if (rightDiagonal != null) {
            moves.add(rightDiagonal);
        }

        var enPassant = canEnPassant(board, currentField);
        if (enPassant != null) {
            moves.add(enPassant);
        }
        return moves;
    }

    @Override
    public List<Field> getBeatenFields(Board board, Field currentField) {
        var moves = new ArrayList<Field>();

        var leftDiagonal = canMoveLeftDiagonal(board, currentField, false);
        if (leftDiagonal != null) {
            moves.add(leftDiagonal);
        }

        var rightDiagonal = canMoveRightDiagonal(board, currentField, false);
        if (rightDiagonal != null) {
            moves.add(rightDiagonal);
        }

        return moves;
    }

    public boolean isEnPassantMove(Board board, Field from, Field to) {
        Move lastMove = board.getLastMove();

        if (lastMove == null) {
            return false;
        }

        Field lastFrom = lastMove.getFrom();
        Field lastTo = lastMove.getTo();
        Piece lastMovedPiece = board.getSquare(lastTo).getPiece();

        if (lastMovedPiece instanceof Pawn &&
                Math.abs(lastFrom.row - lastTo.row) == 2 &&
                lastFrom.col == lastTo.col) {

            return Math.abs(from.col - to.col) == 1
                    && from.row == lastTo.row
                    && to.col == lastTo.col
                    && board.getSquare(to).getPiece() == null;
        }

        return false;
    }

    private Field canEnPassant(Board board, Field currentField) {
        int direction = getDirection();
        Field targetFieldLeft = new Field(currentField.row + direction, currentField.col - 1);
        Field targetFieldRight = new Field(currentField.row + direction, currentField.col + 1);

        if (Board.isInRange(targetFieldLeft) && isEnPassantMove(board, currentField, targetFieldLeft)) {
            System.out.println(targetFieldLeft.row + " ! " + targetFieldLeft.col);
            return targetFieldLeft;
        }

        if (Board.isInRange(targetFieldRight) && isEnPassantMove(board, currentField, targetFieldRight)) {
            System.out.println(targetFieldRight.row + " ! " + targetFieldRight.col);
            return targetFieldRight;
        }

        return null;
    }

    private Field canMoveOneForward(Board board, Field currentField) {
        int direction = getDirection();

        var targetField = new Field(currentField.row + direction, currentField.col);
        if (!Board.isInRange(targetField)) {
            return null;
        }

        if (board.isFieldOccupied(targetField)) {
            return null;
        }
        return targetField;
    }

    private Field canMoveTwoForward(Board board, Field currentField) {
        int direction = getDirection();

        if (color == Color.WHITE && currentField.row != 1) {
            return null;
        }

        if (color == Color.BLACK && currentField.row != 6) {
            return null;
        }

        var targetField = new Field(currentField.row + direction * 2, currentField.col);
        if (!Board.isInRange(targetField)) {
            return null;
        }
        // Проверяем, что обе клетки пусты
        if (board.isFieldOccupied(targetField)) {
            return null;
        }

        if (board.isFieldOccupied(
                new Field(currentField.row + direction, currentField.col))
        ) {
            return null;
        }
        return targetField;
    }

    private Field canMoveLeftDiagonal(Board board, Field currentField, boolean isMoveField) {
        int direction = getDirection();
        int targetRow = currentField.row + direction;

        var targetField = new Field(targetRow, currentField.col - 1);

        return getDiagonalField(board, targetField, isMoveField);
    }

    private Field getDiagonalField(Board board, Field targetField, boolean isMoveField) {
        if (!Board.isInRange(targetField)) {
            return null;
        }
        var square = board.getSquare(targetField);
        var piece = square.getPiece();
        if (isMoveField) {
            if (piece == null || piece.getColor() == getColor()) {
                return null;
            } else {
                return targetField;
            }
        } else {
            if (piece != null && piece.getColor() == getColor()) {
                return null;
            } else {
                return targetField;
            }
        }
    }

    private Field canMoveRightDiagonal(Board board, Field currentField, boolean isMoveField) {
        int direction = getDirection();
        int targetRow = currentField.row + direction;

        var targetField = new Field(targetRow, currentField.col + 1);

        return getDiagonalField(board, targetField, isMoveField);
    }
}
