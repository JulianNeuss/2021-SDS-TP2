package simulation.parser;

import simulation.Cell;
import simulation.Dimension;

import java.util.List;

public class ParserResult {
    private final Dimension dimension;
    private final int rows;
    private final int columns;
    private final int depth;
    private final int aliveQty;
    private final double maxDistance;
    private final List<List<Cell>> cells2D;
    private final List<List<List<Cell>>> cells3D;

    public ParserResult(Dimension dimension, int rows, int columns, int depth, int aliveQty, double maxDistance, List<List<Cell>> cells2D, List<List<List<Cell>>> cells3D) {
        this.dimension = dimension;
        this.rows = rows;
        this.columns = columns;
        this.depth = depth;
        this.aliveQty = aliveQty;
        this.maxDistance = maxDistance;
        this.cells2D = cells2D;
        this.cells3D = cells3D;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getDepth() {
        if(dimension != Dimension.THREE_D)
            throw new IllegalArgumentException("This are not parse results for 3D");
        return depth;
    }

    public int getAliveQty() {
        return aliveQty;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public List<List<Cell>> getCells2D() {
        if(dimension != Dimension.TWO_D)
            throw new IllegalArgumentException("This are not parse results for 2D");
        return cells2D;
    }

    public List<List<List<Cell>>> getCells3D() {
        if (dimension != Dimension.THREE_D)
            throw new IllegalArgumentException("This are not parse results for 3D");
        return cells3D;
    }
}
