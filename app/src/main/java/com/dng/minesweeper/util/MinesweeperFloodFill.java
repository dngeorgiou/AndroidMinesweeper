package com.dng.minesweeper.util;

public class MinesweeperFloodFill extends FloodFill {

    public void apply(boolean[][] shouldShow, int[][] grid, int row, int column) {
        int currentValue = getValueAt(grid, row, column);
        if (currentValue == 0) {
            shouldShow[row][column] = true;
            grid[row][column] = -1;
            showSurroundingBlocks(row, column, shouldShow);

            // Increment right
            apply(shouldShow, grid, row, column+1);

            // Increment below and right
            apply(shouldShow, grid, row+1, column+1);

            // Increment below
            apply(shouldShow, grid, row+1, column);

            // Increment below and left
            apply(shouldShow, grid, row+1, column-1);

            // Increment left
            apply(shouldShow, grid, row, column-1);

            // Increment above and left
            apply(shouldShow, grid, row-1, column-1);

            // Increment above
            apply(shouldShow, grid, row-1, column);

            // Increment above and right
            apply(shouldShow, grid, row-1, column+1);

        }
    }

    /**
     * Method created to fill the blocks surrounding the flood filled blocks
     */
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

}
