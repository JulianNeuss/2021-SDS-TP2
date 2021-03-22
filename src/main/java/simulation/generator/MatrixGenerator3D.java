package simulation.generator;

import simulation.Cell;

import java.util.ArrayList;
import java.util.List;

public class MatrixGenerator3D {
    MatrixGenerator3D(){
    }

    public static List<List<List<Cell>>> generate(int rows, int columns, int depths, int initialRows, int initialColumns, int initialDepths,double aliveRatio){
        if(aliveRatio < 0 || aliveRatio > 1)
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 1");

        int centerRow = rows / 2;
        int centerColumn = columns / 2;
        int centerDepth = depths / 2;

        List<List<List<Cell>>> cells = new ArrayList<>(rows);
        for (int row = 0; row < rows; row++) {
            cells.add(new ArrayList<>(columns));
            for (int column = 0; column < columns; column++) {
                cells.get(row).add(new ArrayList<>(depths));
                for (int depth = 0; depth < depths; depth++) {
                    if(row < centerRow - Math.floor(initialRows/2.0) || row > centerRow + Math.ceil(initialRows/2.0) ||
                            column < centerColumn - Math.floor(initialColumns/2.0) || column > centerColumn + Math.ceil(initialColumns/2.0) ||
                            depth < centerDepth - Math.floor(initialDepths/2.0) || depth > centerDepth + Math.ceil(initialDepths/2.0)){
                        cells.get(row).get(column).add(new Cell(false));
                    } else {
                        boolean isAlive = Math.random() < aliveRatio;
                        cells.get(row).get(column).add(new Cell(isAlive));
                    }
                }
            }
        }

        return cells;
    }
}
