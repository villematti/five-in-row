package com.talviruusu;

public class Board {
    private CellState[] [] board;

    Board() {
        board = initBoard();
    }

    public CellState[][] getBoard() {
        return board;
    }

    public CellState[][] initBoard() {
        return new CellState[][] {
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK}
        };
    }

    public void place(Integer[] location, CellState player) {
        board[location[0]][location[1]] = player;
    }

    public void printBoard() {
        System.out.println("  0 1 2 3 4 5 6 7 8");
        for (int i = 0; i < board.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != CellState.BLANK) {
                    System.out.print(board[i][j] + (j + 1 == board[i].length ? "" : "|"));
                } else {
                    System.out.print(" " + (j + 1 == board[i].length ? "" : "|"));
                }
            }
            System.out.print(i + 1 == board.length ? "" : "\n  ------------------\n");
        }
        System.out.println();
    }

    public CellState checkWon() {
        CellState checkingPlayer = CellState.BLANK;

        CellState mainBoardRowCondition = getCellCondition(board);
        CellState leftDiagonalCondition = getCellCondition(createLeftDiagonalGrid());
        CellState rightDiagonalCondition = getCellCondition(createRightDiagonalGrid());

        // Check if row has winning condition fulfilled
        if (mainBoardRowCondition != CellState.BLANK) {
            checkingPlayer = mainBoardRowCondition;
        } else if (leftDiagonalCondition != CellState.BLANK) {
            checkingPlayer = leftDiagonalCondition;
        } else if (rightDiagonalCondition != CellState.BLANK) {
            checkingPlayer = rightDiagonalCondition;
        }

        // Check if column has winning condition
        for(CellState[] cellStates : board) {
            int counter = 0;
            CellState lastCell = CellState.BLANK;
            for (int ci = 0;ci < cellStates.length;ci++) {

                // Check if current column fills a winning condition
                for (CellState[] states : board) {
                    if (counter == 5) {
                        checkingPlayer = lastCell;
                    } else if (states[ci] == CellState.BLANK) {
                        // Reset counter if cell is BLANK
                        counter = 0;
                        lastCell = CellState.BLANK;
                    } else if (states[ci] == lastCell) {
                        counter++;
                    } else {
                        counter = 0;
                        lastCell = states[ci];
                    }
                }
                counter = 0;
                lastCell = CellState.BLANK;
            }
        }

        // Check if game is tie
        int cellCount = 0;
        for (CellState[] cellStates : board) {
            for (CellState cellState : cellStates) {
                if (cellState == CellState.BLANK) {
                    cellCount++;
                }
            }
        }

        if (cellCount == 0 && checkingPlayer == CellState.BLANK) {
            return null;
        } else {
            return checkingPlayer;
        }
    }
    public CellState[][] getAvailablePositions() {
        CellState[][] pos = initBoard();
        for (int ri = 0;ri < board.length;ri++) {
            CellState[] row = getBoardRow(board, ri);
            for(int ci = 0;ci < row.length;ci++) {
                if (board[ri][ci] == CellState.BLANK) {
                    pos[ri][ci] = CellState.BLANK;
                } else {
                    pos[ri][ci] = CellState.X;
                }
            }
        }
        return pos;
    }

    private CellState[] getBoardRow(CellState[][] selectedBoard, int index) {
        return selectedBoard[index];
    }

    private CellState checkRowCondition(CellState[] row) {
        int counter = 1;
        CellState lastState = CellState.BLANK;
        CellState winnerState = CellState.BLANK;

        for (CellState cell : row) {
            if (counter == 5) {
                winnerState = lastState;
                break;
            } else if (cell == CellState.BLANK) {
                counter = 1;
                lastState = CellState.BLANK;
            } else if (cell == lastState) {
                counter++;
            } else {
                lastState = cell;
                counter = 1;
            }
        }

        return winnerState;
    }

    private CellState getCellCondition(CellState[][] selectedBoard) {
        CellState cellWinner = CellState.BLANK;
        for (int x = 0; x < selectedBoard.length; x++) {
            CellState currentCell = checkRowCondition(getBoardRow(selectedBoard, x));

            if (currentCell != CellState.BLANK && cellWinner == CellState.BLANK) {
                cellWinner = currentCell;
            }
        }
        return cellWinner;
    }

    private CellState[][] createRightDiagonalGrid() {
        CellState[][] diagonalGridRight = initDiagonalGrid();

        // Line 1
        diagonalGridRight[0][0] = board[0][4];
        diagonalGridRight[0][1] = board[1][5];
        diagonalGridRight[0][2] = board[2][6];
        diagonalGridRight[0][3] = board[3][7];
        diagonalGridRight[0][4] = board[4][8];

        // Line 2
        diagonalGridRight[1][0] = board[0][3];
        diagonalGridRight[1][1] = board[1][4];
        diagonalGridRight[1][2] = board[2][5];
        diagonalGridRight[1][3] = board[3][6];
        diagonalGridRight[1][4] = board[4][7];
        diagonalGridRight[1][5] = board[5][8];

        // Line 3
        diagonalGridRight[2][0] = board[0][2];
        diagonalGridRight[2][1] = board[1][3];
        diagonalGridRight[2][2] = board[2][4];
        diagonalGridRight[2][3] = board[3][5];
        diagonalGridRight[2][4] = board[4][6];
        diagonalGridRight[2][5] = board[5][7];
        diagonalGridRight[2][6] = board[6][8];

        // Line 4
        diagonalGridRight[3][0] = board[0][1];
        diagonalGridRight[3][1] = board[1][2];
        diagonalGridRight[3][2] = board[2][3];
        diagonalGridRight[3][3] = board[3][4];
        diagonalGridRight[3][4] = board[4][5];
        diagonalGridRight[3][5] = board[5][6];
        diagonalGridRight[3][6] = board[6][7];
        diagonalGridRight[3][7] = board[7][8];

        // Line 5
        diagonalGridRight[4][0] = board[0][0];
        diagonalGridRight[4][1] = board[1][1];
        diagonalGridRight[4][2] = board[2][2];
        diagonalGridRight[4][3] = board[3][3];
        diagonalGridRight[4][4] = board[4][4];
        diagonalGridRight[4][5] = board[5][5];
        diagonalGridRight[4][6] = board[6][6];
        diagonalGridRight[4][7] = board[7][7];
        diagonalGridRight[4][8] = board[8][8];

        // Line 6
        diagonalGridRight[5][0] = board[1][0];
        diagonalGridRight[5][1] = board[2][1];
        diagonalGridRight[5][2] = board[3][2];
        diagonalGridRight[5][3] = board[4][3];
        diagonalGridRight[5][4] = board[5][4];
        diagonalGridRight[5][5] = board[6][5];
        diagonalGridRight[5][6] = board[7][6];
        diagonalGridRight[5][7] = board[8][7];

        // Line 7
        diagonalGridRight[6][0] = board[2][0];
        diagonalGridRight[6][1] = board[3][1];
        diagonalGridRight[6][2] = board[4][2];
        diagonalGridRight[6][3] = board[5][3];
        diagonalGridRight[6][4] = board[6][4];
        diagonalGridRight[6][5] = board[7][5];
        diagonalGridRight[6][6] = board[8][6];

        // Line 8
        diagonalGridRight[7][0] = board[3][0];
        diagonalGridRight[7][1] = board[4][1];
        diagonalGridRight[7][2] = board[5][2];
        diagonalGridRight[7][3] = board[6][3];
        diagonalGridRight[7][4] = board[7][4];
        diagonalGridRight[7][5] = board[8][5];

        // Line 9
        diagonalGridRight[8][0] = board[4][0];
        diagonalGridRight[8][1] = board[5][1];
        diagonalGridRight[8][2] = board[6][2];
        diagonalGridRight[8][3] = board[7][3];
        diagonalGridRight[8][4] = board[8][4];

        return diagonalGridRight;
    }

    private CellState[][] createLeftDiagonalGrid() {

        CellState[][] diagonalGridLeft = initDiagonalGrid();

        // Line 1
        diagonalGridLeft[0][0] = board[4][0];
        diagonalGridLeft[0][1] = board[3][1];
        diagonalGridLeft[0][2] = board[2][2];
        diagonalGridLeft[0][3] = board[1][3];
        diagonalGridLeft[0][4] = board[0][4];

        // Line 2
        diagonalGridLeft[1][0] = board[5][0];
        diagonalGridLeft[1][1] = board[4][1];
        diagonalGridLeft[1][2] = board[3][2];
        diagonalGridLeft[1][3] = board[2][3];
        diagonalGridLeft[1][4] = board[1][4];
        diagonalGridLeft[1][5] = board[0][5];

        // Line 3
        diagonalGridLeft[2][0] = board[6][0];
        diagonalGridLeft[2][1] = board[5][1];
        diagonalGridLeft[2][2] = board[4][2];
        diagonalGridLeft[2][3] = board[3][3];
        diagonalGridLeft[2][4] = board[2][4];
        diagonalGridLeft[2][5] = board[1][5];
        diagonalGridLeft[2][6] = board[0][6];

        // Line 4
        diagonalGridLeft[3][0] = board[7][0];
        diagonalGridLeft[3][1] = board[6][1];
        diagonalGridLeft[3][2] = board[5][2];
        diagonalGridLeft[3][3] = board[4][3];
        diagonalGridLeft[3][4] = board[3][4];
        diagonalGridLeft[3][5] = board[2][5];
        diagonalGridLeft[3][6] = board[1][6];
        diagonalGridLeft[3][7] = board[0][7];

        // Line 5
        diagonalGridLeft[4][0] = board[8][0];
        diagonalGridLeft[4][1] = board[7][1];
        diagonalGridLeft[4][2] = board[6][2];
        diagonalGridLeft[4][3] = board[5][3];
        diagonalGridLeft[4][4] = board[4][4];
        diagonalGridLeft[4][5] = board[3][5];
        diagonalGridLeft[4][6] = board[2][6];
        diagonalGridLeft[4][7] = board[1][7];
        diagonalGridLeft[4][8] = board[0][8];

        // Line 6
        diagonalGridLeft[5][0] = board[8][1];
        diagonalGridLeft[5][1] = board[7][2];
        diagonalGridLeft[5][2] = board[6][3];
        diagonalGridLeft[5][3] = board[5][4];
        diagonalGridLeft[5][4] = board[4][5];
        diagonalGridLeft[5][5] = board[3][6];
        diagonalGridLeft[5][6] = board[2][7];
        diagonalGridLeft[5][7] = board[1][8];

        // Line 7
        diagonalGridLeft[6][0] = board[8][2];
        diagonalGridLeft[6][1] = board[7][3];
        diagonalGridLeft[6][2] = board[6][4];
        diagonalGridLeft[6][3] = board[5][5];
        diagonalGridLeft[6][4] = board[4][6];
        diagonalGridLeft[6][5] = board[3][7];
        diagonalGridLeft[6][6] = board[2][8];

        // Line 8
        diagonalGridLeft[7][0] = board[8][3];
        diagonalGridLeft[7][1] = board[7][4];
        diagonalGridLeft[7][2] = board[6][5];
        diagonalGridLeft[7][3] = board[5][6];
        diagonalGridLeft[7][4] = board[4][7];

        return diagonalGridLeft;
    }

    public CellState[][] initDiagonalGrid() {

        return new CellState[][]{
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK,},
                {CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK, CellState.BLANK,}
        };
    }

    public enum CellState {
        X, O, BLANK
    }
}
