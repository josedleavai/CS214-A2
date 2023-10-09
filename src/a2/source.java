/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package a2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Taefalaula Brown - S11188253
 * @author David Palavipaongo - S11130156
 * @author Josed D Leavai - S11176243
 */
public class source {

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        Scanner scanner = new Scanner(System.in);

        // Get the current working directory
        String currentDirectory = System.getProperty("user.dir");

        // Create a JFrame (window) for the file chooser dialog
        JFrame frame = new JFrame("Choose a .atsp File");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a file chooser dialog
        JFileChooser fileChooser = new JFileChooser(currentDirectory);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("ATSP Files", "atsp");
        fileChooser.setFileFilter(filter);

        // Show the file chooser dialog and capture the user's choice
        int result = fileChooser.showOpenDialog(frame);

        // Check if the user selected a file
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filename = selectedFile.getName();

            // Check if the file exists before attempting to read it
            if (!fileExists(filename)) {
                System.err.println("The specified file \'" + filename + "\' does not exist");
                System.exit(1);
            }

            double[][] distanceMatrix = readData(filename);

            if (distanceMatrix == null) {
                System.err.println("An error occurred while reading the contents of the file.");
                System.exit(1);
            }

            int cityCount = distanceMatrix.length;
            System.out.println("----------------------------");
            System.out.println("File name   : " + filename);
            System.out.println("# of cities : " + cityCount);
            System.out.println("----------------------------");
            System.out.println();

            boolean exit = false;

            do {
                System.out.println("Select an option:");
                System.out.println("-----------------------------------------------------");
                System.out.println("1. (\'Question 1a \') -> Dynamic Programming for TSP ");
                System.out.println("2. (\'Question 1b \') -> Genetic Algorithm for TSP ");
                System.out.println("3. (\'Question 3 \')  -> Empirical Testing ");
                System.out.println("4. (\'Question 4 \')  -> Race The Algorithms for TSP ");
                System.out.println("-----------------------------------------------------");
                System.out.println("5. Print the contents of the file");
                System.out.println("6. Load another file ");
                System.out.println("7. Exit ");
                System.out.println("-----------------------------------------------------");

                int choice = readIntInput(scanner, 1, 7);

                if (choice == 1) {
                    // Dynamic Programming for TSP
                    System.out.println();
                    System.out.println("Selected DP for TSP:");
                    System.out.println("--------------------");
                    System.out.println();
                    System.out.println("City count                          : " + cityCount);

                    List<Integer> shortestPath = TSPDP.tspDynamicProgramming(distanceMatrix);
                    long shortestPathCost = (long) TSPDP.calculatePathCost(distanceMatrix, shortestPath);

                    System.out.println("Dynamic Programming - Best Tour     : " + shortestPath);
                    System.out.println("Dynamic Programming - Best Distance : " + shortestPathCost);
                    System.out.println();

                } else if (choice == 2) {

                    // Genetic Algorithm for TSP
                    System.out.println();
                    System.out.println("Selected GA for TSP:");
                    System.out.println("--------------------");
                    System.out.println();
                    System.out.println("City count                        : " + cityCount);

                    TSPGA gaSolver = new TSPGA(distanceMatrix);
                    List<Integer> bestTourGA = gaSolver.solve();
                    int bestDistanceGA = gaSolver.calculateTotalDistance(bestTourGA);

                    System.out.println("Genetic Algorithm - Best Tour     : " + bestTourGA);
                    System.out.println("Genetic Algorithm - Best Distance : " + bestDistanceGA);
                    System.out.println();

                } else if (choice == 3) {
                    // Empirical testing for TSP
                    System.out.println();
                    System.out.println("Selected \'Empirical Testing\'");
                    System.out.println("------------------------------");
                    System.out.println();

                    EmpiricalTesting empiricalTesting = new EmpiricalTesting(distanceMatrix);
                    empiricalTesting.runTests();
                    System.out.println();

                } else if (choice == 4) {
                    System.out.println();
                    System.out.println("Selected \'Racing The Algorithms for TSP\'");
                    System.out.println("------------------------------------------");
                    System.out.println();

                    // Pass the distanceMatrix to the Performance class
                    // Performance performance = new Performance(distanceMatrix);
                    // Launch the JavaFX application
                    Performance.launch(Performance.class);

                    exit = true;

                } else if (choice == 5) {
                    // Display the distance matrix
                    System.out.println();
                    System.out.println("------------------------------");
                    System.out.println(cityCount + " x " + cityCount + " distance matrix");
                    System.out.println("------------------------------");
                    System.out.println();

                    for (int i = 0; i < cityCount; i++) {
                        for (int j = 0; j < cityCount; j++) {
                            System.out.print(distanceMatrix[i][j] + " ");
                        }
                        System.out.println();
                    }
                    System.out.println();

                } else if (choice == 6) {
                    System.out.println();
                    start();
                    break;

                } else {
                    // Terminate the program
                    System.out.println();
                    System.out.println("Exiting ...");
                    exit = true;
                }

            } while (!exit);

        } else {
            System.out.println();
            System.err.println("No file was selected!");
            System.err.println("Exiting ...");
        }

        frame.dispose(); // Close the JFrame
    }

    // File validation
    private static boolean fileExists(String filename) {
        File file = new File(filename);
        return file.exists() && file.isFile();
    }

    // Validation function 1
    private static int readIntInput(Scanner scanner, int min, int max) {
        int choice;
        do {
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println();
                System.out.println("Invalid input. Please enter a number between (" + min + " and " + max + ")");
                System.out.print("Enter your choice: ");
                scanner.next(); // Clear the invalid input
            }
            choice = scanner.nextInt();

            if (choice < min || choice > max) {
                System.out.println();
                System.out.println("Invalid input. Please enter a number between (" + min + " and " + max + ")");
            }
        } while (choice < min || choice > max);
        return choice;
    }

    private static double[][] readData(String filename) {
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
}
