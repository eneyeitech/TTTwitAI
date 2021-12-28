package com.eneyeitech;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
class Game {
    public static final char X = 'X';
    private static final char O = 'O';
    private static final Scanner scanner = new Scanner(System.in);
    private static String[] command;

    /**
     * The entire gameplay is here
     */
    public void start() {
        GameBoard gameBoard = new GameBoard();

        while (true) {

            getCommand();
            if (command[0].equals("exit")) {
                System.exit(0);
            }

            Player player1 = new Player(X, command[1]);
            Player player2 = new Player(O, command[2]);

            gameBoard.initField();
            gameBoard.printField();

            int result;
            while (true) {
                player1.makeTurn(gameBoard);
                gameBoard.printField();
                result = gameBoard.checkField();
                if (result != 0) {
                    break;
                }
                player2.makeTurn(gameBoard);
                gameBoard.printField();
                result = gameBoard.checkField();
                if (result != 0) {
                    break;
                }
            }
            printResult(result);
        }
    }

    /**
     * Getting the game initialization command
     */
    public static void getCommand() {
        System.out.print("Input command: ");

        while (true) {
            command = scanner.nextLine().split(" ");
            if (command[0].equals("exit")) {
                break;
            }
            if (command.length < 3) {
                System.out.println("Bad parameters!");
            } else {
                break;
            }
        }
    }

    /**
     * Output the game result to the console
     * @param result - int, result of the game
     */
    public static void printResult(int result) {
        switch (result) {
            case 1 :
                System.out.println("X wins");
                break;
            case 2 :
                System.out.println("O wins");
                break;
            case 3 :
                System.out.println("Draw");
                break;
        }
    }


}

class GameBoard {
    private static final int SIZE = 3;
    private static final char X = 'X';
    private static final char O = 'O';
    private static final char EMPTY = '_';

    public char[][] gameField;

    public static int getSIZE() {
        return SIZE;
    }

    /**
     * Make empty game field
     */
    public void initField() {
        gameField = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE * SIZE; i++) {
            gameField[i / SIZE][i % SIZE] = EMPTY;
        }
    }

    /**
     * Print game field
     */
    public void printField() {
        System.out.println("---------");
        for (int i = 0; i < SIZE; i++) {
            System.out.print("| ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(gameField[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    /**
     * Check possible WIN or DRAW or Game not finished
     * @return int 1 - if X win, 2 - if O win, 3 - if Draw, 0 - if not finished
     */
    public int checkField() {
        //check win & impossible win
        if (checkDiagWin(X) || checkRowColWin(X)) {
            return 1; // X wins
        } else if (checkDiagWin(O) || checkRowColWin(O)) {
            return 2; // O wins
        }

        //check draw or not finished
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameField[i][j] == ' ' || gameField[i][j] == '_') {
                    return 0; // Not finished
                }
            }
        }
        return 3; // Draw
    }

    /**
     * Diagonal win?
     * @param symbol - check this symbol
     * @return true if WIN
     */
    private boolean checkDiagWin(char symbol) {
        boolean leftRightDiag = true;
        boolean rightLeftDiag = true;

        for (int i = 0; i < SIZE; i++) {
            leftRightDiag &= (gameField[i][i] == symbol);
            rightLeftDiag &= (gameField[SIZE - i - 1][i] == symbol);
        }

        return leftRightDiag || rightLeftDiag;
    }

    /**
     * ROW or COLUMN WIN?
     * @param symbol - check this symbol
     * @return true if WIN
     */
    private boolean checkRowColWin(char symbol) {
        boolean cols, rows;

        for (int col = 0; col < SIZE; col++) {
            cols = true;
            rows = true;

            for (int row = 0; row < SIZE; row++) {
                cols &= (gameField[col][row] == symbol);
                rows &= (gameField[row][col] == symbol);
            }
            if (cols || rows) {
                return true;
            }
        }
        return false;
    }

}

class Player {

    public static Scanner scanner = new Scanner(System.in);
    private static final char X = 'X';
    private static final char O = 'O';
    private static final char EMPTY = '_';

    private final char symbol;
    private final String level;

    /**
     * Object Player constructor with her player symbol and difficulty level
     *
     * @param symbol - this player symbol (X or O)
     * @param level  - difficulty level (as String "easy", "medium" or "hard")
     */
    public Player(char symbol, String level) {
        this.symbol = symbol;
        this.level = level;
    }

    /**
     * Make a move on the playing field with the selected level of difficulty
     *
     * @param gameBoard - this game board
     */
    public void makeTurn(GameBoard gameBoard) {
        switch (level) {
            case "user":
                humanTurn(gameBoard);
                break;
            case "easy":
                easyAiTurn(gameBoard);
                break;
            case "medium":
                mediumAiTurn(gameBoard);
                break;
            case "hard":
                hardAiTurn(gameBoard);
                break;
            default:
                System.out.println("[PLAYER] Wrong level!");
        }
    }

    /**
     * Make a move on the playing field of user as human
     *
     * @param gameBoard - this game board
     */
    public void humanTurn(GameBoard gameBoard) {
        while (true) {
            int x;
            int y;
            System.out.print("Enter the coordinates: ");

            //check symbols without numbers
            while (true) {
                String turn = scanner.nextLine();
                if (!turn.matches("\\d\\s\\d")) {
                    System.out.println("You should enter numbers!");
                    System.out.print("Enter the coordinates: ");
                } else {
                    x = Integer.parseInt(String.valueOf(turn.charAt(0))) - 1;
                    y = Integer.parseInt(String.valueOf(turn.charAt(2))) - 1;
                    break;
                }
            }

            if (isPossibleTurn(gameBoard, x, y)) {
                gameBoard.gameField[x][y] = symbol;
                break;
            }
        }
    }

    /**
     * We check whether it is possible to descend on the transmitted
     * coordinates and if necessary, output a message
     *
     * @param gameBoard - this game board
     * @param x         - coordinates to check
     * @param y         - coordinates to check
     * @return - boolean, passed onr not coordinates
     */
    public boolean isPossibleTurn(GameBoard gameBoard, int x, int y) {
        if (x < 0 || x > GameBoard.getSIZE() - 1 || y < 0 || y > GameBoard.getSIZE() - 1) {
            if (level.equals("user")) {
                System.out.println("Coordinates should be from 1 to 3!");
            }
            return false;
        } else if (gameBoard.gameField[x][y] == X || gameBoard.gameField[x][y] == O) {
            if (level.equals("user")) {
                System.out.println("This cell is occupied! Choose another one!");
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Make a move on the playing field of AI EASY
     * Use random coordinates
     *
     * @param gameBoard - this game board
     */
    public void easyAiTurn(GameBoard gameBoard) {
        System.out.println("Making move level \"easy\"");
        this.randomTurn(gameBoard);
    }

    /**
     * Getting random coordinates for turn AI
     *
     * @param gameBoard - this game board
     */
    public void randomTurn(GameBoard gameBoard) {
        while (true) {
            int randomX = (int) (Math.random() * GameBoard.getSIZE());
            int randomY = (int) (Math.random() * GameBoard.getSIZE());

            if (isPossibleTurn(gameBoard, randomX, randomY)) {
                gameBoard.gameField[randomX][randomY] = symbol;
                break;
            }
        }
    }

    /**
     * Make turn with medium AI.
     * Looking for the best turn and if it is not - turn randomly
     * @param gameBoard - this game board
     */
    public void mediumAiTurn(GameBoard gameBoard) {
        if (!findMediumTurn(gameBoard)) {
            randomTurn(gameBoard);
        }
    }

    /**
     * Check each cell for a possible win with check opponent's win.
     * @param gameBoard this game board
     * @return true if found best turn
     */
    public boolean findMediumTurn(GameBoard gameBoard) {
        int x = -1;
        int y = -1;
        boolean foundWin = false;

        System.out.println("Making move level \"medium\"");

        for (int i = 0; i < GameBoard.getSIZE(); i++) {
            for (int j = 0; j < GameBoard.getSIZE(); j++) {
                if (gameBoard.gameField[i][j] == EMPTY) {
                    gameBoard.gameField[i][j] = symbol;

                    if (gameBoard.checkField() != 0) {
                        x = i;
                        y = j;
                        foundWin = true;
                        break;
                    }
                    gameBoard.gameField[i][j] = (symbol == X) ? O : X;
                    if (gameBoard.checkField() != 0) {
                        x = i;
                        y = j;
                        foundWin = true;
                        break;
                    }
                    gameBoard.gameField[i][j] = EMPTY;
                }
            }
            if (foundWin) {
                break;
            }
        }
        if (foundWin) {
            gameBoard.gameField[x][y] = symbol;
            return true;
        }
        return false;
    }

    /**
     * Check all possible moves using the minimax algorithm
     * @param gameBoard - this game board
     */
    public void hardAiTurn(GameBoard gameBoard) {
        System.out.println("Making move level \"hard\"");

        Move bestMove = minimax(gameBoard.gameField, symbol, symbol);
        gameBoard.gameField[bestMove.index[0]][bestMove.index[1]] = symbol;
    }

    /**
     * Implementation of the minimax algorithm
     * @param gameField - the state of the playing field where we are looking for the best move
     * @param callingPlayer - character of the player being checked
     * @param currentPlayer - symbol of current player
     * @return move object - the best move in this state of the playing field
     */
    public Move minimax(char[][] gameField, char callingPlayer, char currentPlayer) {
        List<Move> moves = new ArrayList<>();
        char enemySymbol = (callingPlayer == X) ? O : X;
        char callingSymbol = (callingPlayer == X) ? X : O;
        char enemyPlayer = (currentPlayer == X) ? O : X;

        // Counting the score of this move
        if (isWin(gameField, enemySymbol)) {
            return new Move(-10);
        } else if (isWin(gameField, callingSymbol)) {
            return new Move(10);
        } else if (!isEmptyCellsLeft(gameField)) {
            return new Move(0);
        }

        for (int i = 0; i < GameBoard.getSIZE(); i++) {
            for (int j = 0; j < GameBoard.getSIZE(); j++) {
                if (gameField[i][j] == EMPTY) {
                    // let's make a possible move
                    Move move = new Move();
                    move.index = new int[]{i, j};
                    gameField[i][j] = currentPlayer;
                    Move result = minimax(gameField, callingPlayer, enemyPlayer);
                    // save the score for the minimax
                    move.score = result.score;
                    // then revert the occupied place back to empty, so next guesses can go on
                    gameField[i][j] = EMPTY;
                    moves.add(move);
                }
            }
        }

        // Choose the move with the highest score
        int bestMove = 0;

        if (currentPlayer == callingPlayer) {
            int bestScore = -10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score > bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        } else {
            int bestScore = 10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score < bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        }

        // minimax returns the best move to the latest function caller
        return moves.get(bestMove);
    }

    /**
     * Check is empty cells left on this game board
     * @param gameField - this game board
     * @return boolean true if got empty cell
     */
    private static boolean isEmptyCellsLeft(char[][] gameField) {
        boolean gotEmptiesCells = false;
        for (int i = 0; i < GameBoard.getSIZE(); i++) {
            for (int j = 0; j < GameBoard.getSIZE(); j++) {
                if (gameField[i][j] == EMPTY) {
                    gotEmptiesCells = true;
                    break;
                }
            }
        }
        return gotEmptiesCells;
    }


    /**
     * Check possible win on this game board for this player
     * @param gameField - this game board
     * @param playerSymbol - checked player symbol
     * @return boolean true if player win
     */
    private static boolean isWin(char[][] gameField, char playerSymbol) {
        boolean leftRightDiag = true;
        boolean rightLeftDiag = true;

        for (int i = 0; i < GameBoard.getSIZE(); i++) {
            leftRightDiag &= (gameField[i][i] == playerSymbol);
            rightLeftDiag &= (gameField[GameBoard.getSIZE() - i - 1][i] == playerSymbol);
        }

        boolean cols = false;
        boolean rows = false;

        for (int col = 0; col < GameBoard.getSIZE(); col++) {
            cols = true;
            rows = true;

            for (int row = 0; row < GameBoard.getSIZE(); row++) {
                cols &= (gameField[col][row] == playerSymbol);
                rows &= (gameField[row][col] == playerSymbol);
            }
            if (cols || rows) {
                break;
            }
        }

        return leftRightDiag || rightLeftDiag || cols || rows;
    }
}

class Move {

    int[] index;
    int score;

    Move() {
    }

    Move(int s) {
        score = s;
    }

}