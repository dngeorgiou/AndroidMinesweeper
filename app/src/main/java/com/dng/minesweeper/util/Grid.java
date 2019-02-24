package com.dng.minesweeper.util;

import java.util.HashMap;

public class Grid {

    private static final String TAG = "Grid";

    /*
    KEY:
        0 - no mine at this location
        1 - mine at this location
     */
    public static final int NO_MINE_VALUE = 0;
    public static final int MINE_VALUE = 1;

    public int[][] grid;

    public Grid() {
        // Required empty public constructor
    }

    // [START initialization for Minesweeper grid]
    public int[][] initializeGrid(int rows, int mines) {
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
    // [END initialization for Minesweeper grid]

    // [START calculate mines surrounding cell]
    public int[][] surroundingMines(int[][] gridMap) {
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
    // [END calculate mines surrounding cell]

    // [START calc mines surrounding upper left corner block]
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
    // [END calc mines surrounding upper left corner block]

    // [START calc mines surrounding upper right corner block]
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
    // [END calc mines surrounding upper right corner block]

    // [START calc mines surrounding bottom left corner block]
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
    // [END calc mines surrounding bottom left corner block]

    // [START calc mines surrounding bottom right corner block]
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
    // [END calc mines surrounding bottom right corner block]

    // [START calc mines surrounding first row, not corner blocks]
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
    // [END calc mines surrounding first row, not corner blocks]

    // [START calc mines surrounding last row, not corner blocks]
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
    // [END calc mines surrounding last row, not corner blocks]

    // [START calc mines surrounding first column, not corner block]
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
    // [END calc mines surrounding first column, not corner block]

    // [START calc mines surrounding last column, not corner block]
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
    // [END calc mines surrounding last column, not corner block]

    // [START calc mines surrounding inner blocks]
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
    // [END calc mines surrounding inner blocks]



    // [START check mine at position]

    // [START check mine above and left]
    private int checkMineAboveAndLeft(int[][] gridMap, int row, int column) {
        if (gridMap[row-1][column-1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    private boolean checBlockAboveAndLeftForShow(int[][] surroundingMap, int row, int column) {
        if (surroundingMap[row-1][column-1] == 0) {
            return  true;
        } else {
            return false;
        }
    }
    // [END check mine above and left]

    // [START check mine left]
    private int checkMineLeft(int[][] gridMap, int row, int column) {
        if (gridMap[row][column-1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    private boolean checkBlockLeftForShow(int[][] surroundingMap, int row, int column) {
        if (surroundingMap[row][column-1] == 0) {
            return  true;
        } else {
            return false;
        }
    }
    // [END check mine left]

    // [START check mine below and left]
    private int checkMineBelowAndLeft(int[][] gridMap, int row, int column) {
        if (gridMap[row+1][column-1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    private boolean checkBlockBelowAndLeftForShow(int[][] surroundingMap, int row, int column) {
        if (surroundingMap[row+1][column-1] == 0) {
            return  true;
        } else {
            return false;
        }
    }
    // [END check mine below and left]

    // [START check mine below]
    private int checkMineBelow(int[][] gridMap, int row, int column) {
        if (gridMap[row+1][column] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    private boolean checkBlockBelowForShow(int[][] surroundingMap, int row, int column) {
        if (surroundingMap[row+1][column] == 0) {
            return  true;
        } else {
            return false;
        }
    }
    // [END check mine below]

    // [START check mine below and right]
    private int checkMineBelowAndRight(int[][] gridMap, int row, int column) {
        if (gridMap[row+1][column+1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    private boolean checkBlockBelowAndRightForShow(int[][] surroundingMap, int row, int column) {
        if (surroundingMap[row+1][column+1] == 0) {
            return  true;
        } else {
            return false;
        }
    }
    // [END check mine below and right]

    // [START check mine right]
    private int checkMineRight(int[][] gridMap, int row, int column) {
        if (gridMap[row][column+1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    private boolean checkBlockRightForShow(int[][] surroundingMap, int row, int column) {
        if (surroundingMap[row][column+1] == 0) {
            return  true;
        } else {
            return false;
        }
    }
    // [END check mine right]

    // [START check mine above and right]
    private int checkMineAboveAndRight(int[][] gridMap, int row, int column) {
        if (gridMap[row-1][column+1] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    private boolean checkBlockAboveAndRightForShow(int[][] surroundingMap, int row, int column) {
        if (surroundingMap[row-1][column+1] == 0) {
            return  true;
        } else {
            return false;
        }
    }
    // [END check mine above and right]

    // [START check mine above]
    private int checkMineAbove(int[][] gridMap, int row, int column) {
        if (gridMap[row-1][column] == MINE_VALUE) {
            return  1;
        } else {
            return 0;
        }
    }

    private boolean checkBlockAboveForShow(int[][] surroundingMap, int row, int column) {
        if (surroundingMap[row-1][column] == 0) {
            return  true;
        } else {
            return false;
        }
    }
    // [END check mine above]

    // [END check mine at position]



    // [START update should show map]
    public boolean[][] updateShouldShow(boolean[][] shouldShow, int[][] surroundingMap, int rowClicked, int columnClicked) {

        floodFill(shouldShow, surroundingMap, rowClicked, columnClicked);

        return shouldShow;
    }

    private void floodFill(boolean[][] shouldShow, int[][] surroundingMap, int row, int column) {
        int currentValue = getValueAt(surroundingMap, row, column);
        if (currentValue == 0) {
            shouldShow[row][column] = true;
            surroundingMap[row][column] = -1;
            showSurroundingBlocks(row, column, shouldShow);

            // Increment right
            floodFill(shouldShow, surroundingMap, row, column+1);

            // Increment below and right
            floodFill(shouldShow, surroundingMap, row+1, column+1);

            // Increment below
            floodFill(shouldShow, surroundingMap, row+1, column);

            // Increment below and left
            floodFill(shouldShow, surroundingMap, row+1, column-1);

            // Increment left
            floodFill(shouldShow, surroundingMap, row, column-1);

            // Increment above and left
            floodFill(shouldShow, surroundingMap, row-1, column-1);

            // Increment above
            floodFill(shouldShow, surroundingMap, row-1, column);

            // Increment above and right
            floodFill(shouldShow, surroundingMap, row-1, column+1);

        }
    }

    /**
     * Method created to avoid IndexOutOfBoundExceptions. This method returns -1 if you try to access
     * an invalid position.
     */
    private static int getValueAt(int[][] surroundingMap, int x, int y) {
        if (x < 0 || y < 0 || x > surroundingMap.length-1 || y > surroundingMap[x].length-1) {
            return -1;
        } else {
            return surroundingMap[x][y];
        }
    }

    private void showSurroundingBlocks(int i, int j, boolean shouldShow[][]) {
        // Show block above
        if (i > 0) {
            shouldShow[i-1][j] = true;
        }

        // Show block above and right
        if (i > 0 && j < shouldShow.length-1) {
            shouldShow[i-1][j+1] = true;
        }

        // Show block to right
        if (j < shouldShow.length-1) {
            shouldShow[i][j+1] = true;
        }

        // Show block below and right
        if (i < shouldShow.length-1 && j < shouldShow.length-1) {
            shouldShow[i+1][j+1] = true;
        }

        // Show block below
        if (i < shouldShow.length-1) {
            shouldShow[i+1][j] = true;
        }

        // Show block below and left
        if (i < shouldShow.length-1 && j > 0) {
            shouldShow[i+1][j-1] = true;
        }

        // Show block to left
        if (j > 0) {
            shouldShow[i][j-1] = true;
        }

        // Show block above left
        if (i > 0 && j > 0) {
            shouldShow[i-1][j-1] = true;
        }
    }
    // [END update should show map]

}
