/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Taefalaula Brown - S11188253
 * @author David Palavipaongo - S11130156
 * @author Josed D Leavai - S11176243
 */
public class TSPDP {

    private static int functionCallCount = 0;

    public static int getFunctionCallCount() {
        return functionCallCount;
    }

    public static void resetFunctionCallCount() {
        functionCallCount = 0;
    }

    public static List<Integer> tspDynamicProgramming(double[][] distanceMatrix) { // Update parameter type to double[][]
        resetFunctionCallCount();

        int n = distanceMatrix.length;
        double[][] dp = new double[1 << n][n]; // Update to double
        int[][] parent = new int[1 << n][n];

        // Initialize the dp table with large values
        for (double[] d : dp) {
            Arrays.fill(d, Double.MAX_VALUE / 2); // Update to Double.MAX_VALUE / 2
        }

        dp[1][0] = 0.0; // Base case: Starting from city 0 with no other cities visited, cost is 0.0

        // Loop through different sets of cities
        for (int mask = 1; mask < (1 << n); mask += 2) {
            for (int u = 1; u < n; u++) {
                if ((mask & (1 << u)) != 0) {
                    for (int v = 0; v < n; v++) {
                        if ((mask & (1 << v)) != 0 && u != v) {
                            functionCallCount++; // Increment NFC
                            if (dp[mask ^ (1 << u)][v] + distanceMatrix[v][u] < dp[mask][u]) {
                                dp[mask][u] = dp[mask ^ (1 << u)][v] + distanceMatrix[v][u];
                                parent[mask][u] = v;
                            }
                        }
                    }
                }
            }
        }

        int mask = (1 << n) - 1;
        int u = -1;
        double minCost = Double.MAX_VALUE; // Update to double

        // Find the last city in the shortest path
        for (int v = 1; v < n; v++) {
            if (dp[mask][v] + distanceMatrix[v][0] < minCost) {
                minCost = dp[mask][v] + distanceMatrix[v][0];
                u = v;
            }
        }

        List<Integer> path = new ArrayList<>();

        if (u == -1) {
            return Collections.emptyList(); // If no valid path found
        }
        // Retrace steps to get the optimal path
        while (mask > 1) {
            path.add(u);
            int v = parent[mask][u];
            mask ^= (1 << u);
            u = v;
        }

        path.add(0); // Complete the tour by returning to the starting city
        Collections.reverse(path); // Reverse the list to get the correct order
        return path; // Return the optimal path
    }

    // Method to calculate the cost of a given path
    public static double calculatePathCost(double[][] distanceMatrix, List<Integer> path) { // Update parameter type to double[][]
        double cost = 0.0;
        int n = path.size();

        for (int i = 0; i < n - 1; i++) {
            int u = path.get(i);
            int v = path.get(i + 1);
            cost += distanceMatrix[u][v];
        }

        // Add the distance from the last node back to the starting node
        cost += distanceMatrix[path.get(n - 1)][path.get(0)];

        return cost;
    }
}
