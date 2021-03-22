package simulation;

import simulation.parser.Parser;
import simulation.parser.ParserResult;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class SimulationApp {
    private static final String DEFAULT_INPUT_FILENAME = "./data/initialSetup.txt";
    private static final String DEFAULT_OUTPUT_FILENAME = "./data/output.txt";
    private static final int DEFAULT_ITERATIONS = 10;

    public static void main(String[] args) {
        Properties properties = System.getProperties();

        String inputFilename = Paths.get(DEFAULT_INPUT_FILENAME).toAbsolutePath().toString();
        if( properties.getProperty("inputFilename")!= null ){
            inputFilename = properties.getProperty("inputFilename");
        }

        String outputFilename = Paths.get(DEFAULT_OUTPUT_FILENAME).toAbsolutePath().toString();
        if( properties.getProperty("outputFilename")!= null ){
            outputFilename = properties.getProperty("outputFilename");
        }

        int iterations = DEFAULT_ITERATIONS;
        if( properties.getProperty("iterations")!= null ){
            outputFilename = properties.getProperty("iterations");
        }

        // check input file
        File inputFile = new File(Paths.get(inputFilename).toAbsolutePath().toString());
        if(!inputFile.getParentFile().exists()){
            if(!inputFile.getParentFile().mkdirs()){
                System.err.println("Input's folder does not exist and could not be created");
                System.exit(1);
            }
        }

        // check input file
        File outputFile = new File(Paths.get(inputFilename).toAbsolutePath().toString());
        if(!outputFile.getParentFile().exists()){
            if(!outputFile.getParentFile().mkdirs()){
                System.err.println("Output's folder does not exist and could not be created");
                System.exit(1);
            }
        }

        try {
            ParserResult parserResult = Parser.parse(Paths.get(inputFilename).toAbsolutePath().toString());
            StringBuilder str = new StringBuilder();
            if(parserResult.getDimension() == Dimension.TWO_D){
                str.append(Dimension.TWO_D.toString()).append('\n');
                str.append(parserResult.getRows()).append(' ').append(parserResult.getColumns()).append('\n');
                str.append('\n');
                List<List<Cell>> cells = parserResult.getCells2D();
                for (int iteration = 0; iteration < iterations; iteration++) {
                    cells = GameOfLife2D.nextRound(cells);
                    str.append(aliveQty2D(cells)).append('\n');
                    str.append('\n');
                    for (List<Cell> rows : cells) {
                        for (Cell cell : rows) {
                            str.append(cell.isAlive() ? '1' : '0').append(' ');
                        }
                        str.append('\n');
                    }
                    str.append('\n');
                }
            } else {
                str.append(Dimension.THREE_D.toString()).append('\n');
                str.append(parserResult.getRows()).append(' ').append(parserResult.getColumns()).append(' ').append(parserResult.getDepth()).append('\n');
                str.append('\n');
                List<List<List<Cell>>> cells = parserResult.getCells3D();
                for (int iteration = 0; iteration < iterations; iteration++) {
                    cells = GameOfLife3D.nextRound(cells);
                    str.append(aliveQty3D(cells)).append('\n');
                    str.append('\n');
                    for (List<List<Cell>> rows : cells) {
                        for (List<Cell> columns : rows) {
                            for (Cell cell : columns) {
                                str.append(cell.isAlive() ? '1' : '0').append(' ');
                            }
                            str.append('\n');
                        }
                        str.append('\n');
                    }
                    str.append('\n');
                }
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(outputFilename).toAbsolutePath().toString(), false));
                writer.write(str.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int aliveQty2D(List<List<Cell>> cells) {
        int aliveQty = 0;
        for (List<Cell> row : cells){
            for (Cell cell : row){
                if(cell.isAlive())
                    aliveQty++;
            }
        }
        return aliveQty;
    }

    private static int aliveQty3D(List<List<List<Cell>>> cells) {
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
}
