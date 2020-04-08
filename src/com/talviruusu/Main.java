package com.talviruusu;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();

        while(board.checkWon() == Board.CellState.BLANK) {
            board.printBoard();

            Board.CellState[][] currentBoard = board.getAvailablePositions();

            int playerRowPos;
            int playerColPos;

            do {
                Scanner scan = new Scanner(System.in);

                System.out.println("Enter your placement row (1-9)");

                playerRowPos = scan.nextInt();

                System.out.println("Enter your placement col (1-9)");

                playerColPos = scan.nextInt();

            } while(currentBoard[playerRowPos][playerColPos] == Board.CellState.X);

            placeMark(board, playerRowPos, playerColPos, Board.CellState.X);

            Random rand = new Random();

            int cpuRowPos;
            int cpuColPos;

            do {
                cpuRowPos = rand.nextInt(9);
                cpuColPos = rand.nextInt(9);
            } while (currentBoard[cpuRowPos][cpuColPos] == Board.CellState.X);

            placeMark(board, cpuRowPos, cpuColPos, Board.CellState.O);
        }

        System.out.println(board.checkWon() + " won! Please try again!");
    }

    public static void placeMark(Board board, int row, int col, Board.CellState player) {
        Integer[] coords = {row, col};
        board.place(coords, player);
    }
}
