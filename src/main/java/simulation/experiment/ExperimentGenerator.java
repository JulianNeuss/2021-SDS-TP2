package simulation.experiment;

import simulation.*;
import simulation.generator.MatrixGenerator2D;
import simulation.generator.MatrixGenerator3D;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ExperimentGenerator {
    private static final String DEFAULT_OUTPUT_FILENAME = "./data/test2d.txt";
    private static final int DEFAULT_ROWS = 20;
    private static final int DEFAULT_COLUMNS = 20;
    private static final int DEFAULT_DEPTHS = 20;
    private static final int DEFAULT_INITIAL_ROWS = 10;
    private static final int DEFAULT_INITIAL_COLUMNS = 10;
    private static final int DEFAULT_INITIAL_DEPTH = 10;
    private static final double DEFAULT_MIN_PERCENTAGE_ALIVE = 0.01;
    private static final double DEFAULT_MAX_PERCENTAGE_ALIVE = 1;
    private static final int DEFAULT_PERCENTAGES_QTY = 6;
    private static final Dimension DEFAULT_DIMENSION = Dimension.TWO_D;
    private static final int DEFAULT_ITERATIONS = 100;
    private static final boolean END_ON_TOUCH_BORDER = true;
    private static final int DEFAULT_NUMBER_OF_RUNS = 10;


    public static void main(String[] args) {
        Properties properties = System.getProperties();
        String outputFilename = DEFAULT_OUTPUT_FILENAME;
        if( properties.getProperty("outputFilename") != null ){
            outputFilename = properties.getProperty("outputFilename");
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
        int initialRows = DEFAULT_INITIAL_ROWS;
        if( properties.getProperty("initialRows")!= null ) {
            initialRows = Integer.parseInt(properties.getProperty("initialRows"));
        }
        int initialColumns = DEFAULT_INITIAL_COLUMNS;
        if( properties.getProperty("initialColumns")!= null ){
            initialColumns = Integer.parseInt(properties.getProperty("initialColumns"));
        }
        int initialDepths = DEFAULT_INITIAL_DEPTH;
        if( properties.getProperty("initialDepths")!= null ){
            initialDepths = Integer.parseInt(properties.getProperty("initialDepths"));
        }
        double minPercentageAlive = DEFAULT_MIN_PERCENTAGE_ALIVE;
        if( properties.getProperty("minPercentageAlive")!= null ){
            minPercentageAlive = Double.parseDouble(properties.getProperty("minPercentageAlive"));
        }
        double maxPercentageAlive = DEFAULT_MAX_PERCENTAGE_ALIVE;
        if( properties.getProperty("maxPercentageAlive")!= null ){
            maxPercentageAlive = Double.parseDouble(properties.getProperty("maxPercentageAlive"));
        }
        int percentageQty = DEFAULT_PERCENTAGES_QTY;
        if( properties.getProperty("percentageQty")!= null ){
            percentageQty = Integer.parseInt(properties.getProperty("percentageQty"));
        }


        Dimension dimension = DEFAULT_DIMENSION;
        if( properties.getProperty("dimension")!= null ){
            dimension = Dimension.fromString(properties.getProperty("dimension"));
        }

        int maxIterations = DEFAULT_ITERATIONS;
        if( properties.getProperty("maxIterations")!= null ){
            outputFilename = properties.getProperty("maxIterations");
        }

        boolean endOnTouchBorder = END_ON_TOUCH_BORDER;
        if( properties.getProperty("endOnTouchBorder")!= null ){
            endOnTouchBorder = Boolean.parseBoolean(properties.getProperty("endOnTouchBorder"));
        }

        int numberOfRuns = DEFAULT_NUMBER_OF_RUNS;
        if( properties.getProperty("numberOfRuns")!= null ){
            numberOfRuns = Integer.parseInt(properties.getProperty("numberOfRuns"));
        }



        Map<Double, Map<Integer, List<ExperimentResult>>> results = new HashMap<>();
        Map<Double, Map<Integer, ExperimentResult>> percentageToAverageResults = new HashMap<>();
        double percentageStep = (maxPercentageAlive - minPercentageAlive)/percentageQty;
        double percentageAlive = minPercentageAlive;
        if (dimension == Dimension.TWO_D) {
            for (int percentageIteration = 0; percentageIteration < percentageQty; percentageAlive = Math.min(1, percentageAlive + percentageStep), percentageIteration++) {
                for (int run = 0; run < numberOfRuns; run++) {
                    boolean touchBorder = false;
                    List<List<Cell>> cells = MatrixGenerator2D.generate(rows, columns, initialRows, initialColumns, percentageAlive);
                    for (int iteration = 0; (endOnTouchBorder && !touchBorder && (iteration < maxIterations)) || (!endOnTouchBorder && iteration < maxIterations); iteration++) {
                        cells = GameOfLife2D.nextRound(cells);
                        touchBorder = MatrixOperations.touchBorder2D(cells);
                        if(!results.containsKey(percentageAlive))
                            results.put(percentageAlive, new HashMap<>());
                        if(!results.get(percentageAlive).containsKey(iteration))
                            results.get(percentageAlive).put(iteration, new ArrayList<>());
                        results.get(percentageAlive).get(iteration).add(new ExperimentResult(MatrixOperations.aliveQty2D(cells), MatrixOperations.maxDistance2D(cells)));
                    }
                }
                for (Integer iteration : results.get(percentageAlive).keySet()) {
                    if(!percentageToAverageResults.containsKey(percentageAlive))
                        percentageToAverageResults.put(percentageAlive, new HashMap<>());
                    percentageToAverageResults.get(percentageAlive).put(iteration, averageResult(results.get(percentageAlive).get(iteration)));
                }
            }
        } else {
            for (int percentageIteration = 0; percentageIteration < percentageQty; percentageAlive = Math.min(1, percentageAlive + percentageStep), percentageIteration++) {
                for (int run = 0; run < numberOfRuns; run++) {
                    boolean touchBorder = false;
                    List<List<List<Cell>>> cells = MatrixGenerator3D.generate(rows, columns, depths, initialRows, initialColumns, initialDepths, percentageAlive);
                    for (int iteration = 0; (endOnTouchBorder && !touchBorder && (iteration < maxIterations)) || (!endOnTouchBorder && iteration < maxIterations); iteration++) {
                        cells = GameOfLife3D.nextRound(cells);
                        touchBorder = MatrixOperations.touchBorder3D(cells);
                        if(!results.containsKey(percentageAlive))
                            results.put(percentageAlive, new HashMap<>());
                        if(!results.get(percentageAlive).containsKey(iteration))
                            results.get(percentageAlive).put(iteration, new ArrayList<>());
                        results.get(percentageAlive).get(iteration).add(new ExperimentResult(MatrixOperations.aliveQty3D(cells), MatrixOperations.maxDistance3D(cells)));
                    }
                }

                for (Integer iteration : results.get(percentageAlive).keySet()) {
                    if(!percentageToAverageResults.containsKey(percentageAlive))
                        percentageToAverageResults.put(percentageAlive, new HashMap<>());
                    percentageToAverageResults.get(percentageAlive).put(iteration, averageResult(results.get(percentageAlive).get(iteration)));
                }
            }
        }

        StringBuilder str = new StringBuilder();
        List<Double> sortedPercentages = new ArrayList<>(percentageToAverageResults.keySet());
        Collections.sort(sortedPercentages);
        for (Double percentage : sortedPercentages){
            str.append(percentage).append('\n');
            List<Integer> sortedIterations = new ArrayList<>(percentageToAverageResults.get(percentage).keySet());
            Collections.sort(sortedIterations);
            for (Integer iteration : sortedIterations) {
                str.append(iteration).append(' ').append(percentageToAverageResults.get(percentage).get(iteration).aliveQty).append(' ').append(percentageToAverageResults.get(percentage).get(iteration).maxDistance).append('\n');
            }
            str.append('\n');
        }

        // check file
        File inputFile = new File(Paths.get(outputFilename).toAbsolutePath().toString());
        if(!inputFile.getParentFile().exists()){
            if(!inputFile.getParentFile().mkdirs()){
                System.err.println("Output's folder does not exist and could not be created");
                System.exit(1);
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(outputFilename).toAbsolutePath().toString(), false));
            writer.write(str.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static ExperimentResult averageResult(List<ExperimentResult> experimentResults) {
        double aliveSum = 0;
        double distanceSum = 0;
        for (ExperimentResult result : experimentResults){
            aliveSum += result.aliveQty;
            distanceSum += result.maxDistance;
        }
        return new ExperimentResult(aliveSum/experimentResults.size(), distanceSum/experimentResults.size());
    }

    private static class ExperimentResult {
        private final double aliveQty;
        private final double maxDistance;

        public ExperimentResult(double aliveQty, double maxDistance) {
            this.aliveQty = aliveQty;
            this.maxDistance = maxDistance;
        }
    }
}
