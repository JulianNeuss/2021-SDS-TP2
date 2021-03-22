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
                List<List<Cell>> cells = parserResult.getCells2D();
                cells = GameOfLife2D.nextRound(cells);

                str.append(Dimension.TWO_D.toString()).append('\n');
                str.append(parserResult.getRows()).append(' ').append(parserResult.getColumns()).append('\n');
                str.append('\n');
                for (List<Cell> rows : cells) {
                    for (Cell cell : rows){
                        str.append(cell.isAlive()?'1':'0').append(' ');
                    }
                    str.append('\n');
                }
            } else {
                List<List<List<Cell>>> cells = parserResult.getCells3D();
                cells = GameOfLife3D.nextRound(cells);

                str.append(Dimension.THREE_D.toString()).append('\n');
                str.append(parserResult.getRows()).append(' ').append(parserResult.getColumns()).append(' ').append(parserResult.getDepth()).append('\n');
                str.append('\n');
                for (List<List<Cell>> rows : cells) {
                    for (List<Cell> columns : rows){
                        for (Cell cell : columns) {
                            str.append(cell.isAlive() ? '1' : '0').append(' ');
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
}
