package simulation.generator;

import simulation.Cell;
import simulation.Dimension;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class GenerateInitialFile {
    private static final String DEFAULT_INPUT_FILENAME = "./data/initialSetup.txt";
    private static final int DEFAULT_ROWS = 10;
    private static final int DEFAULT_COLUMNS = 10;
    private static final int DEFAULT_DEPTHS = 10;
    private static final double DEFAULT_PERCENTAGE_ALIVE = 0.25;
    private static final Dimension DEFAULT_DIMENSION = Dimension.TWO_D;

    public static void main(String[] args) {
        Properties properties = System.getProperties();
        String inputFilename = DEFAULT_INPUT_FILENAME;
        if( properties.getProperty("inputFilename") != null ){
            inputFilename = properties.getProperty("inputFilename");
        }
        int rows = DEFAULT_ROWS;
        if( properties.getProperty("rows")!= null ) {
            rows = Integer.parseInt(properties.getProperty("rows"));
        }
        int columns = DEFAULT_COLUMNS;
        if( properties.getProperty("columns")!= null ){
            columns = Integer.parseInt(properties.getProperty("columns"));
        }
        int depths = DEFAULT_DEPTHS;
        if( properties.getProperty("depths")!= null ){
            depths = Integer.parseInt(properties.getProperty("depths"));
        }
        double percentageAlive = DEFAULT_PERCENTAGE_ALIVE;
        if( properties.getProperty("percentageAlive")!= null ){
            percentageAlive = Double.parseDouble(properties.getProperty("percentageAlive"));
        }

        Dimension dimension = DEFAULT_DIMENSION;
        if( properties.getProperty("dimension")!= null ){
            dimension = Dimension.fromString(properties.getProperty("dimension"));
        }

        StringBuilder str = new StringBuilder();

        if(dimension == Dimension.TWO_D){
            List<List<Cell>> cells = MatrixGenerator2D.generate(rows, columns, percentageAlive);

            str.append(dimension.toString()).append('\n');
            str.append(rows).append(' ').append(columns).append('\n');
            str.append('\n');
            for (List<Cell> rowsList : cells) {
                for (Cell cell : rowsList){
                    str.append(cell.isAlive()?'1':'0').append(' ');
                }
                str.append('\n');
            }
        } else {
            List<List<List<Cell>>> cells = MatrixGenerator3D.generate(rows, columns, depths, percentageAlive);

            str.append(Dimension.THREE_D.toString()).append('\n');
            str.append(rows).append(' ').append(columns).append(' ').append(depths).append('\n');
            str.append('\n');
            for (List<List<Cell>> rowsList : cells) {
                for (List<Cell> columnsList : rowsList){
                    for (Cell cell : columnsList) {
                        str.append(cell.isAlive() ? '1' : '0').append(' ');
                    }
                    str.append('\n');
                }
                str.append('\n');
            }
        }

        // check file
        File inputFile = new File(Paths.get(inputFilename).toAbsolutePath().toString());
        if(!inputFile.getParentFile().exists()){
            if(!inputFile.getParentFile().mkdirs()){
                System.err.println("Input's folder does not exist and could not be created");
                System.exit(1);
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(inputFilename).toAbsolutePath().toString(), false));
            writer.write(str.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
