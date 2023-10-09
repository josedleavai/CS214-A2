/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package a2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 * Performance class for TSP algorithm race visualization.
 */
public class Performance extends Application {

    private double[][] distanceMatrix;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TSP Algorithm Race Performance");

        // Create a file chooser dialog to select the ATSP file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ATSP Files", "*.atsp"));

        // Set the initial directory to the current working directory
        String currentDirectory = System.getProperty("user.dir");
        fileChooser.setInitialDirectory(new File(currentDirectory));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            String filename = selectedFile.getAbsolutePath();
            distanceMatrix = readData(filename);
            if (distanceMatrix != null) {
                // Extract the filename without the path
                String chartTitle = selectedFile.getName();

                // Continue with algorithm race and chart creation
                runAlgorithmRace(primaryStage, chartTitle);
            }
        } else {
            System.out.println();
            System.err.println("No file was selected!");
            System.err.println("Exiting ...");
            System.exit(0); // Terminate the program if no file is selected
        }
    }

    // Modify the readData method to load the distanceMatrix from the file
    private double[][] readData(String filename) {
        double[][] distanceMatrix = null;
        int cityCount = 0;
        int currentRow = 0;
        int currentCol = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+");

                if (cityCount == 0) {
                    // Read the number of cities from the first line
                    cityCount = Integer.parseInt(values[0]);
                    distanceMatrix = new double[cityCount][cityCount];
                } else {
                    for (String value : values) {
                        distanceMatrix[currentRow][currentCol] = Double.parseDouble(value);
                        currentCol++;

                        if (currentCol == cityCount) {
                            currentCol = 0;
                            currentRow++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return distanceMatrix;
    }

    // Implement the algorithm race and chart creation here
    private void runAlgorithmRace(Stage primaryStage, String chartTitle) {
        // Create a line chart with Number axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        xAxis.setLabel("Number of Function Calls (NFC)");
        yAxis.setLabel("Fitness Value (Total Distance)");

        // Set the chart title to include the filename
        lineChart.setTitle("TSP Algorithm Race - " + chartTitle);

        // Create a series for DP
        XYChart.Series<Number, Number> dpSeries = new XYChart.Series<>();
        dpSeries.setName("Dynamic Programming");

        // Create a series for GA
        XYChart.Series<Number, Number> gaSeries = new XYChart.Series<>();
        gaSeries.setName("Genetic Algorithm");

        // Run the algorithms and collect data for the chart
        int numRuns = 100; // Adjust the number of runs as needed

        for (int run = 0; run < numRuns; run++) {
            // Run Dynamic Programming
            List<Integer> dpSolution = TSPDP.tspDynamicProgramming(distanceMatrix);
            int dpNFC = TSPDP.getFunctionCallCount();
            double dpCost = TSPDP.calculatePathCost(distanceMatrix, dpSolution);
            dpSeries.getData().add(new XYChart.Data<>(run, dpCost));

            // Run Genetic Algorithm
            TSPGA gaSolver = new TSPGA(distanceMatrix);
            List<Integer> gaSolution = gaSolver.solve();
            int gaNFC = gaSolver.getFunctionCallCount();
            int gaCost = gaSolver.calculateTotalDistance(gaSolution);
            gaSeries.getData().add(new XYChart.Data<>(run, gaCost));
        }

        // Add series to the line chart
        lineChart.getData().addAll(dpSeries, gaSeries);

        // Create a scene and display the chart
        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
