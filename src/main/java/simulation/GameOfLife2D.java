package simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameOfLife2D {
    private GameOfLife2D(){
    }

    public static List<List<Cell>> nextRound(List<List<Cell>> oldCellMatrix, GameOfLifeRules rules){
        if(oldCellMatrix.isEmpty())
            throw new IllegalArgumentException("La matriz no tiene celdas");

        List<List<Cell>> newCellMatrix = initializeMatrix(oldCellMatrix.size(), oldCellMatrix.get(0).size());

        for (int row = 0; row < oldCellMatrix.size(); row++) {
            for (int column = 0; column < oldCellMatrix.get(0).size(); column++) {

                int aliveAroundQty = aliveAroundQty(oldCellMatrix, row, column);
                boolean wasAlive = oldCellMatrix.get(row).get(column).isAlive();

                newCellMatrix.get(row).add(new Cell(shouldBeAlive(wasAlive, aliveAroundQty, rules)));
            }
        }

        return newCellMatrix;
    }

    private static List<List<Cell>> initializeMatrix(int rows, int columns) {
        List<List<Cell>> matrix = new ArrayList<>(rows);
        for (int row = 0; row < rows; row++) {
            matrix.add(new ArrayList<>(columns));
        }
        return matrix;
    }

    private static boolean shouldBeAlive(boolean wasAlive, int aliveAroundQty, GameOfLifeRules rules) {
        if(wasAlive){
            return rules.getAroundAliveSet().contains(aliveAroundQty);
        } else {
            return rules.getAroundDeadSet().contains(aliveAroundQty);
        }
    }

    private static int aliveAroundQty(List<List<Cell>> cellMatrix, int row, int column) {
        int aliveAroundQty = 0;

        boolean canCheckUp = row > 0;
        boolean canCheckDown = row + 1 < cellMatrix.size();

        // UP
        if(canCheckUp){
            List<Cell> upRow = cellMatrix.get(row - 1);
            aliveAroundQty += aliveInRow(upRow, column, true);
        }

        // CENTER
        List<Cell> centerRow = cellMatrix.get(row);
        aliveAroundQty += aliveInRow(centerRow, column, false);


        // DOWN
        if(canCheckDown){
            List<Cell> downRoad = cellMatrix.get(row + 1);
            aliveAroundQty += aliveInRow(downRoad, column, true);
        }

        return aliveAroundQty;
    }

    private static int aliveInRow(List<Cell> row, int column, boolean sumCenter) {
        int aliveAroundQty = 0;

        boolean canCheckLeft = column > 0;
        boolean canCheckRight = column + 1 < row.size();

        // LEFT
        if(canCheckLeft && row.get(column-1).isAlive())
            aliveAroundQty++;

        // MID
        if(sumCenter && row.get(column).isAlive())
            aliveAroundQty++;

        // RIGHT
        if(canCheckRight && row.get(column + 1).isAlive())
            aliveAroundQty++;

        return aliveAroundQty;
    }
}