package com.dng.minesweeper.util;

public class FloodFill {

    public void apply(boolean[][] shouldShow, int[][] grid, int row, int column) {
        int currentValue = getValueAt(grid, row, column);
        if (currentValue == 0) {
            shouldShow[row][column] = true;
            grid[row][column] = -1;

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
     * Method created to avoid IndexOutOfBoundExceptions. This method returns -1 if you try to access
     * an invalid position.
     */
    protected static int getValueAt(int[][] grid, int x, int y) {
        if (x < 0 || y < 0 || x > grid.length-1 || y > grid[x].length-1) {
            return -1;
        } else {
            return grid[x][y];
        }
    }

}
