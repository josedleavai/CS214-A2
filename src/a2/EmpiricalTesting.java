/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a2;

import static a2.TSPDP.calculatePathCost;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Taefalaula Brown - S11188253
 * @author David Palavipaongo - S11130156
 * @author Josed D Leavai - S11176243
 */
public class EmpiricalTesting {

    private double[][] distanceMatrix; // Update to double
    private final int NUM_OF_RUNS = 30;

    private double dpBestCost = Double.MAX_VALUE; // Update to double
    private double gaBestCost = Double.MAX_VALUE; // Update to double
    private double dpMaxCost = Double.MIN_VALUE; // Update to double
    private double gaMaxCost = Double.MIN_VALUE; // Update to double

    private List<Integer> dpMaxSolution;
    private List<Integer> gaMaxSolution;

    private List<List<Integer>> dpSolutions;
    private List<List<Integer>> gaSolutions;

    public EmpiricalTesting(double[][] distanceMatrix) { // Update parameter type to double[][]
        this.distanceMatrix = distanceMatrix;
    }

    public void runTests() {
        dpSolutions = new ArrayList<>();
        gaSolutions = new ArrayList<>();

        double dpSuccessCount = 0;
        double gaSuccessCount = 0;

        double totalDpCost = 0;
        double totalGaCost = 0;

        int dpBestNFC = Integer.MAX_VALUE;
        int gaBestNFC = Integer.MAX_VALUE;

        double gaBestCostRun = Double.MAX_VALUE;

        List<Integer> gaBestSolution = new ArrayList<>();

        // -----------------------------------------------------------------
        System.out.println("Running tests: \'" + NUM_OF_RUNS + "\' times...");
        System.out.println();

        for (int i = 0; i < NUM_OF_RUNS; i++) {
            // Run Dynamic Programming
            List<Integer> dpSolution = TSPDP.tspDynamicProgramming(distanceMatrix);
            int dpNFC = TSPDP.getFunctionCallCount();
            dpSolutions.add(dpSolution);

            double dpCost = calculatePathCost(distanceMatrix, dpSolution);
            totalDpCost += dpCost;

            if (dpNFC < dpBestNFC) {
                dpBestNFC = dpNFC;
                dpBestCost = dpCost;
                dpMaxSolution = dpSolution;
            }

            if (dpCost > dpMaxCost) {
                dpMaxCost = dpCost;
                dpMaxSolution = dpSolution;
            }

            if (dpCost != Double.MAX_VALUE) {
                dpSuccessCount++;
            }

            // Run Genetic Algorithm
            TSPGA gaSolver = new TSPGA(distanceMatrix);
            List<Integer> gaSolution = gaSolver.solve();
            int gaNFC = gaSolver.getFunctionCallCount();

            // Calculate GA cost
            double gaCost = gaSolver.calculateTotalDistance(gaSolution);
            totalGaCost += gaCost;
            gaSolutions.add(gaSolution);

            if (gaNFC < gaBestNFC) {
                gaBestNFC = gaNFC;
                gaBestCost = gaCost;
                gaMaxSolution = gaSolution;
            }

            if (gaCost > gaMaxCost) {
                gaMaxCost = gaCost;
                gaMaxSolution = gaSolution;
            }

            if (gaCost != Double.MAX_VALUE) {
                gaSuccessCount++;
            }

            // Check if this run had the lowest cost for GA
            if (gaCost < gaBestCostRun) {
                gaBestCostRun = gaCost;
                gaBestSolution = new ArrayList<>(gaSolution);
            }

            System.out.println("Iteration \tDP Cost \t\tGA Cost");
            System.out.println("------------------------------------------------------------------");

            // Print the execution times for each algorithm for each search term
            System.out.println((i + 1) + "\t\t" + dpCost + "\t\t\t" + gaCost);
            System.out.println();
        }

        // Calculate statistics
        double dpMeanCost = totalDpCost / NUM_OF_RUNS;
        double gaMeanCost = totalGaCost / NUM_OF_RUNS;
        double dpSuccessRate = (dpSuccessCount / NUM_OF_RUNS) * 100;
        double gaSuccessRate = (gaSuccessCount / NUM_OF_RUNS) * 100;

        // Print out the results
        System.out.println("// Overall : " + distanceMatrix.length + " cities");
        System.out.println("< ------------------------------ >");
        System.out.println("Dynamic Programming Statistics:");
        System.out.println("Best              : " + dpBestCost);
        System.out.println("Mean              : " + dpMeanCost);
        System.out.println("Max               : " + dpMaxCost);
        System.out.println("Success Rate (DP) : " + dpSuccessRate + "%");
        System.out.println("< ------------------------------ >");
        System.out.println("Genetic Algorithm Statistics:");
        System.out.println("Best              : " + gaBestCost);
        System.out.println("Mean              : " + gaMeanCost);
        System.out.println("Max               : " + gaMaxCost);
        System.out.println("Success Rate (GA) : " + gaSuccessRate + "%");
    }
}
