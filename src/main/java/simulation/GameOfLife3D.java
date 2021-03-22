package simulation;

import java.util.ArrayList;
import java.util.List;

public class GameOfLife3D {
    private GameOfLife3D(){
    }

    public static List<List<List<Cell>>> nextRound(List<List<List<Cell>>> oldCellMatrix){
        if(oldCellMatrix.isEmpty() || oldCellMatrix.get(0).isEmpty())
            throw new IllegalArgumentException("La matriz no tiene celdas");

        List<List<List<Cell>>> newCellMatrix = initializeMatrix(oldCellMatrix.size(),
                oldCellMatrix.get(0).size(),
                oldCellMatrix.get(0).get(0).size());

        for (int row = 0; row < oldCellMatrix.size(); row++) {
            for (int column = 0; column < oldCellMatrix.get(0).size(); column++) {
                for (int depth = 0; depth < oldCellMatrix.get(0).get(0).size(); depth++) {

                    int aliveAroundQty = aliveAroundQty(oldCellMatrix, row, column, depth);
                    boolean wasAlive = oldCellMatrix.get(row).get(column).get(depth).isAlive();

                    newCellMatrix.get(row).get(column).add(new Cell(shouldBeAlive(wasAlive, aliveAroundQty)));
                }
            }
        }

        return newCellMatrix;
    }

    private static List<List<List<Cell>>> initializeMatrix(int rows, int columns, int depth) {
        List<List<List<Cell>>> matrix = new ArrayList<>(rows);
        for (int row = 0; row < rows; row++) {
            matrix.add(new ArrayList<>(columns));
            for (int column = 0; column < columns; column++) {
                matrix.get(row).add(new ArrayList<>(depth));
            }
        }
        return matrix;
    }

    private static boolean shouldBeAlive(boolean wasAlive, int aliveAroundQty) {
        if(wasAlive){
            return aliveAroundQty == 2 || aliveAroundQty == 3;
        } else {
            return aliveAroundQty == 3;
        }
    }

    private static int aliveAroundQty(List<List<List<Cell>>> cellMatrix, int row, int column, int depth) {
        int aliveAroundQty = 0;

        boolean canCheckUp = row > 0;
        boolean canCheckDown = row + 1 < cellMatrix.size();

        // UP
        if(canCheckUp){
            List<List<Cell>> upRow = cellMatrix.get(row - 1);
            aliveAroundQty += aliveInRow(upRow, column, depth, false);
        }

        // MID
        List<List<Cell>> midRow = cellMatrix.get(row);
        aliveAroundQty += aliveInRow(midRow, column, depth, true);


        // DOWN
        if(canCheckDown){
            List<List<Cell>> downRoad = cellMatrix.get(row + 1);
            aliveAroundQty += aliveInRow(downRoad, column, depth, false);
        }

        return aliveAroundQty;
    }

    private static int aliveInRow(List<List<Cell>> row, int column, int depth, boolean isMidRow) {
        int aliveAroundQty = 0;

        boolean canCheckLeft = column > 0;
        boolean canCheckRight = column + 1 < row.size();

        // LEFT
        if(canCheckLeft) {
            List<Cell> leftColumn = row.get(column - 1);
            aliveAroundQty += aliveInColumn(leftColumn, depth, false);
        }

        // MID
        List<Cell> midColumn = row.get(column);
        aliveAroundQty += aliveInColumn(midColumn, depth, isMidRow);

        // RIGHT
        if(canCheckRight) {
            List<Cell> rightColumn = row.get(column + 1);
            aliveAroundQty += aliveInColumn(rightColumn, depth, false);
        }

        return aliveAroundQty;
    }

    private static int aliveInColumn(List<Cell> column, int depth, boolean isCenter) {
        int aliveAroundQty = 0;

        boolean canCheckBefore = depth > 0;
        boolean canCheckAfter = depth + 1 < column.size();

        // BEFORE
        if (canCheckBefore && column.get(depth - 1).isAlive())
            aliveAroundQty++;

        // MID
        if(!isCenter && column.get(depth).isAlive())
            aliveAroundQty++;

        // AFTER
        if(canCheckAfter && column.get(depth+1).isAlive())
            aliveAroundQty++;

        return aliveAroundQty;
    }
}
