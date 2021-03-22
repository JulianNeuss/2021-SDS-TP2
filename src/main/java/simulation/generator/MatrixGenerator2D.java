package simulation.generator;

import simulation.Cell;

import java.util.ArrayList;
import java.util.List;

public class MatrixGenerator2D {
    MatrixGenerator2D(){
    }

    public static List<List<Cell>> generate(int rows, int columns, double percentageRatio){
        if(percentageRatio < 0 || percentageRatio > 1)
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 1");

        List<List<Cell>> cells = new ArrayList<>(rows);
        for (int row = 0; row < rows; row++) {
            cells.add(new ArrayList<>(columns));
            for (int column = 0; column < columns; column++) {
                boolean isAlive = Math.random() < percentageRatio;
                cells.get(row).add(new Cell(isAlive));
            }
        }

        return cells;
    }
}
