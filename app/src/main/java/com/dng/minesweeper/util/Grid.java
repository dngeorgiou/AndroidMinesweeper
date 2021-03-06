package com.dng.minesweeper.util;

import java.io.Serializable;
import java.util.HashMap;

public class Grid implements Serializable {

    private static final String TAG = "Grid";

    /*
    KEY:
        0 - no mine at this location
        1 - mine at this location
     */
    public static final int NO_MINE_VALUE = 0;
    public static final int MINE_VALUE = 1;

    // Private member variables
    private final int[][] mineMap;
    private final int[][] surroundingMines;
    private boolean[][] shouldShow;
    private boolean[][] flagVisible;
    private int lastRowClicked;
    private int lastColumnClicked;
    private boolean gameOverWin;
    private boolean gameOverLoss;


    public Grid(int rows, int mines) {
        mineMap = initializeGrid(rows, mines);
        surroundingMines = surroundingMines(mineMap);
        shouldShow = new boolean[rows][rows];
        flagVisible = new boolean[rows][rows];
        gameOverWin = false;
        gameOverLoss = false;
    }

    protected Grid(int[][] testMineMap) {
        mineMap = testMineMap;
        surroundingMines = surroundingMines(mineMap);
        shouldShow = new boolean[mineMap.length][mineMap.length];
        flagVisible = new boolean[mineMap.length][mineMap.length];
    }

    // [START public getters]
    public final int[][] getMineMap() {
        return mineMap;
    }

    public final int[][] getSurroundingMines() {
        return surroundingMines;
    }

    public boolean[][] getShouldShow() {
        return shouldShow;
    }

    public boolean[][] getFlagVisible() {
        return flagVisible;
    }

    public int getLastRowClicked() {
        return  lastRowClicked;
    }

    public int getLastColumnClicked() {
        return lastColumnClicked;
    }

    public boolean getGameOverWin() {
        return gameOverWin;
    }

    public boolean getGameOverLoss() {
        return gameOverLoss;
    }
    // [END public getters]

    // [START public setters]
    public void setShouldShow(int rowClicked, int columnClicked) {
        shouldShow = updateShouldShow(rowClicked, columnClicked);
    }

    public void setFlagVisible(int rowLongClicked, int columnLongClicked) {
        flagVisible = updateFlagVisible(rowLongClicked, columnLongClicked);
    }

    public void setLastBlockClicked(int rowClicked, int columnClicked) {
        setLastRowClicked(rowClicked);
        setLastColumnClicked(columnClicked);
    }

    public void setGameOverWin() {
        gameOverWin = true;
    }

    public void setGameOverLoss() {
        gameOverLoss = true;
    }
    // [END public setters]

    // [START private setters for last block clicked]
    private void setLastRowClicked(int rowClicked) {
        lastRowClicked = rowClicked;
    }

    private void setLastColumnClicked(int columnClicked) {
        lastColumnClicked = columnClicked;
    }
    // [END private setters for last block clicked]


    /**
     * Method initializes grid with mines.
     */
    private int[][] initializeGrid(int rows, int mines) {
        int remainingCells = rows*rows;
        int remainingMines = mines;
        int[][] g = new int[rows][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                float chance = (float) remainingMines / remainingCells;
                // Math.random() returns a double between 0 (inclusive) and 1 (exclusive)
                if (Math.random() < chance) {
                    g[i][j] = MINE_VALUE;
                    remainingMines = remainingMines - 1;
                } else {
                    g[i][j] = NO_MINE_VALUE;
                }
                remainingCells = remainingCells - 1;

                System.out.print(g[i][j]);

            }
            System.out.println();
        }

        return g;
    }

    // [START calculate mines surrounding cell]
    /**
     * Main method for calculating the number of mines surrounding each block.
     */
    private int[][] surroundingMines(int[][] gridMap) {
        int[][] surroundingMap = new int[gridMap.length][gridMap.length];

        for (int i = 0; i < gridMap.length; i++) {
            for (int j = 0; j < gridMap.length; j++) {
                // [START calc first row]
                if (i == 0) {
                    if (j == 0) {
                        // Upper left corner
                        surroundingMap[i][j] = upperLeftMineNearbyCount(gridMap, i, j);
                        System.out.print(surroundingMap[i][j]);
                        continue;
                    } else if (j == gridMap.length-1) {
                        // Upper right corner
                        surroundingMap[i][j] = upperRightMineNearbyCount(gridMap, i, j);
                        System.out.print(surroundingMap[i][j]);
                        continue;
                    } else {
                        // First row, inner blocks
                        surroundingMap[i][j] = firstRowNotCornerMineNearbyCount(gridMap, i, j);
                        System.out.print(surroundingMap[i][j]);
                        continue;
                    }
                }
                // [END calc first row]

                // [START calc last row]
                if (i == gridMap.length-1) {
                    if (j == 0) {
                        // Bottom left corner
                        surroundingMap[i][j] = bottomLeftMineNearbyCount(gridMap, i, j);
                        System.out.print(surroundingMap[i][j]);
                        continue;
                    } else if (j == gridMap.length-1) {
                        // Bottom right corner
                        surroundingMap[i][j] = bottomRightMineNearbyCount(gridMap, i, j);
                        System.out.print(surroundingMap[i][j]);
                        continue;
                    } else {
                        // Last row, inner blocks
                        surroundingMap[i][j] = lastRowNotCornerMineNearbyCount(gridMap, i, j);
                        System.out.print(surroundingMap[i][j]);
                        continue;
                    }
                }
                // [END calc last row]

                // [START calc first column]
                if (j == 0) {
                    // First column, inner blocks
                    surroundingMap[i][j] = firstColumnNotCornerMineNearbyCount(gridMap, i, j);
                    System.out.print(surroundingMap[i][j]);
                    continue;
                }
                // [END calc first column]

                // [START calc last column]
                if (j == gridMap.length-1) {
                    surroundingMap[i][j] = lastColumnNotCornerMineNearbyCount(gridMap, i, j);
                    System.out.print(surroundingMap[i][j]);
                    continue;
                }
                // [END calc last column]

                // [START calc inner blocks]
                surroundingMap[i][j] = innerMineNearbyCount(gridMap, i, j);
                // [END calc inner blocks]
                System.out.print(surroundingMap[i][j]);
            }

            System.out.println();
        }

        return surroundingMap;
    }

    /**
     * Sub surrounding mine calculation method, which calculates number of mines surrounding
     * upper left block.
     */
    private int upperLeftMineNearbyCount(int[][] gridMap, int row, int column) {
        /*
        3 blocks surrounding
         */
        int mines = 0;

        // Block to right
        mines = mines + checkMineRight(gridMap, row, column);

        // Block below and right
        mines = mines + checkMineBelowAndRight(gridMap, row, column);

        // Block below
        mines = mines + checkMineBelow(gridMap, row, column);

        return mines;
    }

    /**
     * Sub surrounding mine calculation method, which calculates number of mines surrounding
     * upper right block.
     */
    private int upperRightMineNearbyCount(int[][] gridMap, int row, int column) {
        /*
        3 blocks surrounding
         */
        int mines = 0;

        // Block to left
        mines = mines + checkMineLeft(gridMap, row, column);

        // Block below and left
        mines = mines + checkMineBelowAndLeft(gridMap, row, column);

        // Block below
        mines = mines + checkMineBelow(gridMap, row, column);

        return mines;
    }

    /**
     * Sub surrounding mine calculation method, which calculates number of mines surrounding
     * bottom left block.
     */
    private int bottomLeftMineNearbyCount(int[][] gridMap, int row, int column) {
        /*
        3 blocks surrounding
         */
        int mines = 0;

        // Block above
        mines = mines + checkMineAbove(gridMap, row, column);

        // Block above and right
        mines = mines + checkMineAboveAndRight(gridMap, row, column);

        // Block to right
        mines = mines + checkMineRight(gridMap, row, column);

        return mines;
    }

    /**
     * Sub surrounding mine calculation method, which calculates number of mines surrounding
     * bottom right block.
     */
    private int bottomRightMineNearbyCount(int[][] gridMap, int row, int column) {
        /*
        3 blocks surrounding
         */
        int mines = 0;

        // Block above
        mines = mines + checkMineAbove(gridMap, row, column);

        // Block above and left
        mines = mines + checkMineAboveAndLeft(gridMap, row, column);

        // Block to left
        mines = mines + checkMineLeft(gridMap, row, column);

        return mines;
    }

    /**
     * Sub surrounding mine calculation method, which calculates number of mines surrounding
     * first row, not corner blocks.
     */
    private int firstRowNotCornerMineNearbyCount(int[][] gridMap, int row, int column) {
        /*
        5 blocks surrounding
         */
        int mines = 0;

        // Block to left
        mines = mines + checkMineLeft(gridMap, row, column);

        // Block below and left
        mines = mines + checkMineBelowAndLeft(gridMap, row, column);

        // Block below
        mines = mines + checkMineBelow(gridMap, row, column);

        // Block below and right
        mines = mines + checkMineBelowAndRight(gridMap, row, column);

        // Block to right
        mines = mines + checkMineRight(gridMap, row, column);

        return mines;
    }

    /**
     * Sub surrounding mine calculation method, which calculates number of mines surrounding
     * last row, not corner blocks.
     */
    private int lastRowNotCornerMineNearbyCount(int[][] gridMap, int row, int column) {
        /*
        5 blocks surrounding
         */
        int mines = 0;

        // Block to left
        mines = mines + checkMineLeft(gridMap, row, column);

        // Block above and left
        mines = mines + checkMineAboveAndLeft(gridMap, row, column);

        // Block above
        mines = mines + checkMineAbove(gridMap, row, column);

        // Block above and right
        mines = mines + checkMineAboveAndRight(gridMap, row, column);

        // Block to right
        mines = mines + checkMineRight(gridMap, row, column);

        return mines;
    }

    /**
     * Sub surrounding mine calculation method, which calculates number of mines surrounding
     * first column, not corner blocks.
     */
    private int firstColumnNotCornerMineNearbyCount(int[][] gridMap, int row, int column) {
        /*
        5 blocks surrounding
         */
        int mines = 0;

        // Block above
        mines = mines + checkMineAbove(gridMap, row, column);

        // Block above and right
        mines = mines + checkMineAboveAndRight(gridMap, row, column);

        // Block to right
        mines = mines + checkMineRight(gridMap, row, column);

        // Block below and right
        mines = mines + checkMineBelowAndRight(gridMap, row, column);

        // Block below
        mines = mines + checkMineBelow(gridMap, row, column);

        return mines;
    }

    /**
     * Sub surrounding mine calculation method, which calculates number of mines surrounding
     * last column, not corner blocks.
     */
    private int lastColumnNotCornerMineNearbyCount(int[][] gridMap, int row, int column) {
        /*
        5 blocks surrounding
         */
        int mines = 0;

        // Block above
        mines = mines + checkMineAbove(gridMap, row, column);

        // Block above and left
        mines = mines + checkMineAboveAndLeft(gridMap, row, column);

        // Block to left
        mines = mines + checkMineLeft(gridMap, row, column);

        // Block below and left
        mines = mines + checkMineBelowAndLeft(gridMap, row, column);

        // Block below
        mines = mines + checkMineBelow(gridMap, row, column);

        return mines;
    }

    /**
     * Sub surrounding mine calculation method, which calculates number of mines surrounding
     * inner blocks (i.e. blocks not in first row, last row, first column, or last column).
     */
    private int innerMineNearbyCount(int[][] gridMap, int row, int column) {
        /*
        8 blocks surrounding
         */
        int mines = 0;

        // Block to above and left
        mines = mines + checkMineAboveAndLeft(gridMap, row, column);

        // Block to left
        mines = mines + checkMineLeft(gridMap, row, column);

        // Block below and left
        mines = mines + checkMineBelowAndLeft(gridMap, row, column);

        // Block below
        mines = mines + checkMineBelow(gridMap, row, column);

        // Block below and right
        mines = mines + checkMineBelowAndRight(gridMap, row, column);

        // Block to right
        mines = mines + checkMineRight(gridMap, row, column);

        // Block above and right
        mines = mines + checkMineAboveAndRight(gridMap, row, column);

        // Block above
        mines = mines + checkMineAbove(gridMap, row, column);

        return mines;
    }


    // [START check mine at specific surrounding position]
    /**
     * Sub surrounding mine calculation method, which checks if mine in block above and left current block.
     */
    private int checkMineAboveAndLeft(int[][] gridMap, int row, int column) {
        if (gridMap[row-1][column-1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    /**
     * Sub surrounding mine calculation method, which checks if mine in block to left of current block.
     */
    private int checkMineLeft(int[][] gridMap, int row, int column) {
        if (gridMap[row][column-1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    /**
     * Sub surrounding mine calculation method, which checks if mine in block below and left current block.
     */
    private int checkMineBelowAndLeft(int[][] gridMap, int row, int column) {
        if (gridMap[row+1][column-1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    /**
     * Sub surrounding mine calculation method, which checks if mine in block below current block.
     */
    private int checkMineBelow(int[][] gridMap, int row, int column) {
        if (gridMap[row+1][column] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    /**
     * Sub surrounding mine calculation method, which checks if mine in block below and right current block.
     */
    private int checkMineBelowAndRight(int[][] gridMap, int row, int column) {
        if (gridMap[row+1][column+1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    /**
     * Sub surrounding mine calculation method, which checks if mine in block to right of current block.
     */
    private int checkMineRight(int[][] gridMap, int row, int column) {
        if (gridMap[row][column+1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    /**
     * Sub surrounding mine calculation method, which checks if mine in block above and right current block.
     */
    private int checkMineAboveAndRight(int[][] gridMap, int row, int column) {
        if (gridMap[row-1][column+1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    /**
     * Sub surrounding mine calculation method, which checks if mine in block above current block.
     */
    private int checkMineAbove(int[][] gridMap, int row, int column) {
        if (gridMap[row-1][column] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }
    // [END check mine at specific surrounding position]
    // [END calculate mines surrounding cell]


    /**
     * Method uses Flood Fill recursive algorithm to determine blocks not containing a mine surrounding
     * the block the user clicked on and the blocks immediately surrounding those.
     */
    private boolean[][] updateShouldShow(int rowClicked, int columnClicked) {

        // Show last pressed block
        shouldShow[rowClicked][columnClicked] = true;

        // Update shouldShow and surroundingMap using flood fill algorithm
        MinesweeperFloodFill minesweeperFloodFill = new MinesweeperFloodFill();
        minesweeperFloodFill.apply(shouldShow, surroundingMines, flagVisible, rowClicked, columnClicked);

        // [START print result]
        for (int i = 0; i < shouldShow.length; i++) {
            for (int j = 0; j < shouldShow.length; j++) {
                System.out.print(shouldShow[i][j]);
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println();
        // [END print result]

        return shouldShow;
    }

    /**
     * Method updates the map containing which blocks have been flagged by user.
     */
    private boolean[][] updateFlagVisible(int rowLongClicked, int columnLongClicked) {
        if (flagVisible[rowLongClicked][columnLongClicked]) {
            // Set flagVisible of block to false
            flagVisible[rowLongClicked][columnLongClicked] = false;
        } else {
            // Set flagVisible of block to true
            flagVisible[rowLongClicked][columnLongClicked] = true;
        }

        return flagVisible;
    }

}
