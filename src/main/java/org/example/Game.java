package org.example;

import java.util.Scanner;

public class Game {
    private Board board;
    private BoardView boardView;

    public Game() {
        board = new Board();
        boardView = new BoardView(board);
    }

    public void start() {
        var scanner = new Scanner(System.in);

        while (!isGameOver()) {
            board.selectAvailablePieces();
            boardView.show();
            System.out.println("Выберите фигуру (например, A2): ");
            String fromNotation = scanner.nextLine();
            Field from = Field.getField(fromNotation);

            if (from == null) {
                System.out.println("Неправильная фигура. Попробуйте снова.");
                continue;
            }
            Integer selectedSquares = board.selectPiece(from);
            if (selectedSquares == 0) {
                boardView.show();
                System.out.println("Неправильная фигура. Попробуйте снова.");
                continue;
            } else if (selectedSquares == 1) {
                boardView.show();
                System.out.println("У выбранной фигуры нет доступных ходов. Попробуйте снова.");
                continue;
            }
            boardView.show();

            System.out.println("Введите позицию для хода (например, A4): ");
            String toNotation = scanner.nextLine();
            Field to = Field.getField(toNotation);

            Move move = new Move(from, to);
            if (board.move(move)) {
                boardView.show();
            } else {
                System.out.println("Недопустимый ход. Попробуйте снова.");
            }
        }

        System.out.println("Игра окончена!");
        scanner.close();
    }

    private boolean isGameOver() {
        return false;
    }
}
