package simulation.parser;

import simulation.Cell;
import simulation.Dimension;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    private Parser(){
    }

    public static ParserResult parse(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));

        Dimension dimension = Dimension.fromString(scanner.nextLine().trim());
        if(dimension == Dimension.TWO_D) {

            String[] matrixSizes = scanner.nextLine().trim().split(" ", 2);
            int rows = Integer.parseInt(matrixSizes[0]);
            int columns = Integer.parseInt(matrixSizes[1]);

            List<List<Cell>> cells = new ArrayList<>(rows);
            scanner.nextLine(); // Skip line
            for (int row = 0; row < rows; row++) {
                cells.add(new ArrayList<>(columns));
                String[] cellsStates = scanner.nextLine().trim().split(" ", 10);
                for (int column = 0; column < columns; column++) {
                    cells.get(row).add(new Cell(cellsStates[column].equals("1")));
                }
            }

            return new ParserResult(dimension, rows, columns, -1, cells, null);
        } else {

            String[] matrixSizes = scanner.nextLine().trim().split(" ", 3);
            int rows = Integer.parseInt(matrixSizes[0]);
            int columns = Integer.parseInt(matrixSizes[1]);
            int depths = Integer.parseInt(matrixSizes[2]);

            List<List<List<Cell>>> cells = new ArrayList<>(rows);
            for (int row = 0; row < rows; row++) {
                scanner.nextLine(); // Skip line
                cells.add(new ArrayList<>(columns));
                for (int column = 0; column < columns; column++) {
                    cells.get(row).add(new ArrayList<>(depths));
                    String[] cellsStates = scanner.nextLine().trim().split(" ", 10);
                    for (int depth = 0; depth < depths; depth++) {
                        cells.get(row).get(column).add(new Cell(cellsStates[depth].equals("1")));

                    }
                }
            }

            return new ParserResult(dimension, rows, columns, depths, null, cells);
        }
    }
}
