package simulation;

import java.util.List;

public class MatrixOperations {
    private MatrixOperations(){
    }

    public static boolean touchBorder2D(List<List<Cell>> cells) {
        // ROW = 0
        for (Cell cell : cells.get(0)){
            if (cell.isAlive())
                return true;
        }

        // ROW = SIZE - 1
        for (Cell cell : cells.get(cells.size() - 1)){
            if (cell.isAlive())
                return true;
        }

        // COLUMN = 0
        for (List<Cell> row : cells){
            if(row.get(0).isAlive())
                return true;
        }

        // COLUMN = SIZE - 1
        for (List<Cell> row : cells){
            if(row.get(cells.get(0).size() - 1).isAlive())
                return true;
        }

        return false;
    }

    public static boolean touchBorder3D(List<List<List<Cell>>> cells) {
        // ROW = 0
        for (List<Cell> column : cells.get(0)){
            for(Cell cell : column){
                if(cell.isAlive())
                    return true;
            }
        }

        // ROW = SIZE - 1
        for (List<Cell> column : cells.get(cells.size() - 1)){
            for(Cell cell : column){
                if(cell.isAlive())
                    return true;
            }
        }

        // COLUMN = 0
        for (List<List<Cell>> row : cells) {
            for(Cell cell : row.get(0)){
                if (cell.isAlive())
                    return true;
            }
        }

        // COLUMN = SIZE - 1
        for (List<List<Cell>> row : cells) {
            for(Cell cell : row.get(cells.get(0).size() - 1)){
                if (cell.isAlive())
                    return true;
            }
        }

        // DEPTH = 0
        for (List<List<Cell>> row : cells){
            for (List<Cell> column : row){
                if(column.get(0).isAlive())
                    return true;
            }
        }

        // DEPTH = SIZE - 1
        for (List<List<Cell>> row : cells){
            for (List<Cell> column : row){
                if(column.get(cells.get(0).get(0).size() - 1).isAlive())
                    return true;
            }
        }

        return false;
    }

    public static int aliveQty2D(List<List<Cell>> cells) {
        int aliveQty = 0;
        for (List<Cell> row : cells){
            for (Cell cell : row){
                if(cell.isAlive())
                    aliveQty++;
            }
        }
        return aliveQty;
    }

    public static int aliveQty3D(List<List<List<Cell>>> cells) {
        int aliveQty = 0;
        for (List<List<Cell>> row : cells){
            for (List<Cell> column : row){
                for (Cell cell : column){
                    if(cell.isAlive())
                        aliveQty++;
                }
            }
        }
        return aliveQty;
    }

    public static double maxDistance2D(List<List<Cell>> cells) {
        double maxDistance = 0;
        for (int row = 0; row < cells.size(); row++) {
            for (int column = 0; column < cells.get(0).size(); column++) {
                if (cells.get(row).get(column).isAlive()){
                    double relativeRows = Math.abs(row - cells.size()/2.0);
                    double relativeColumns = Math.abs(column - cells.get(0).size()/2.0);
                    double distance = Math.sqrt(relativeRows * relativeRows + relativeColumns * relativeColumns);
                    maxDistance = Math.max(maxDistance, distance);
                }
            }
        }
        return maxDistance;
    }

    public static double maxDistance3D(List<List<List<Cell>>> cells) {
        double maxDistance = 0;
        for (int row = 0; row < cells.size(); row++) {
            for (int column = 0; column < cells.get(0).size(); column++) {
                for (int depth = 0; depth < cells.get(0).get(0).size(); depth++) {
                    if (cells.get(row).get(column).get(depth).isAlive()){
                        double relativeRows = Math.abs(row - cells.size()/2.0);
                        double relativeColumns = Math.abs(column - cells.get(0).size()/2.0);
                        double relativeDepth = Math.abs(depth - cells.get(0).get(0).size()/2.0);
                        double distance = Math.sqrt(relativeRows * relativeRows + relativeColumns * relativeColumns + relativeDepth * relativeDepth);
                        maxDistance = Math.max(maxDistance, distance);
                    }
                }
            }
        }
        return maxDistance;
    }
}
