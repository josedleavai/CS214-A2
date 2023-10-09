/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Taefalaula Brown - S11188253
 * @author David Palavipaongo - S11130156
 * @author Josed D Leavai - S11176243
 */
public class TSPGA {

    private static final int POPULATION_SIZE = 100;
    private static final double MUTATION_RATE = 0.01;
    private static final int MAX_GENERATIONS = 1000;

    private double[][] distanceMatrix; // Change to double[][]
    private int cityCount;
    private int functionCallCount = 0;
    private Random random = new Random();

    public TSPGA(double[][] distanceMatrix) { // Change parameter type to double[][]
        this.distanceMatrix = distanceMatrix;
        this.cityCount = distanceMatrix.length;
        this.functionCallCount = 0;
    }

    public List<Integer> solve() {
        functionCallCount++;
        List<Individual> population = new ArrayList<>(POPULATION_SIZE);
        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<Integer> tour = new ArrayList<>(cityCount);
            for (int j = 0; j < cityCount; j++) {
                tour.add(j);
            }
            Collections.shuffle(tour);
            population.add(new Individual(tour));
        }

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            Collections.sort(
                    population,
                    (a, b) -> Integer.compare(a.getFitness(), b.getFitness())
            );
            int cutoff = POPULATION_SIZE / 2;
            List<Individual> parents = population.subList(0, cutoff);

            List<Individual> newGeneration = new ArrayList<>(population);
            while (newGeneration.size() < POPULATION_SIZE) {
                Individual parent1 = parents.get(random.nextInt(cutoff));
                Individual parent2 = parents.get(random.nextInt(cutoff));
                Individual child = crossover(parent1, parent2);
                child = child.mutate();
                newGeneration.add(child);
            }

            population = newGeneration;
        }

        Collections.sort(
                population,
                (a, b) -> Integer.compare(a.getFitness(), b.getFitness())
        );
        return population.get(0).getTour();
    }

    public void resetFunctionCallCount() {
        functionCallCount = 0;
    }

    public int getFunctionCallCount() {
        return functionCallCount;
    }

    public int calculateTotalDistance(List<Integer> tour) {
        int totalDistance = 0;
        for (int i = 0; i < cityCount - 1; i++) {
            int fromCity = tour.get(i);
            int toCity = tour.get(i + 1);
            totalDistance += (int) distanceMatrix[fromCity][toCity]; // Cast to int
        }
        totalDistance += (int) distanceMatrix[tour.get(cityCount - 1)][tour.get(0)]; // Cast to int
        return totalDistance;
    }

    private Individual crossover(Individual parent1, Individual parent2) {
        int start = random.nextInt(cityCount);
        int end = random.nextInt(cityCount);
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        List<Integer> childTour = new ArrayList<>(cityCount);
        for (int i = 0; i < cityCount; i++) {
            childTour.add(null);
        }

        for (int i = start; i <= end; i++) {
            childTour.set(i, parent1.getTour().get(i));
        }

        int currentIndex = 0;
        for (int i = 0; i < cityCount; i++) {
            if (!childTour.contains(parent2.getTour().get(i))) {
                while (childTour.get(currentIndex) != null) {
                    currentIndex++;
                }
                childTour.set(currentIndex, parent2.getTour().get(i));
            }
        }

        return new Individual(childTour);
    }

    private class Individual {

        private List<Integer> tour;
        private int fitness;

        public Individual(List<Integer> tour) {
            this.tour = tour;
            calculateFitness();
        }

        private void calculateFitness() {
            int totalDistance = 0;
            for (int i = 0; i < cityCount - 1; i++) {
                int fromCity = tour.get(i);
                int toCity = tour.get(i + 1);
                totalDistance += (int) distanceMatrix[fromCity][toCity]; // Cast to int
            }
            totalDistance += (int) distanceMatrix[tour.get(cityCount - 1)][tour.get(0)]; // Cast to int
            fitness = totalDistance;
        }

        public List<Integer> getTour() {
            return tour;
        }

        public int getFitness() {
            return fitness;
        }

        private Individual mutate() {
            List<Integer> mutatedTour = new ArrayList<>(tour);
            if (random.nextDouble() < MUTATION_RATE) {
                int index1 = random.nextInt(cityCount);
                int index2 = random.nextInt(cityCount);
                Collections.swap(mutatedTour, index1, index2);
            }
            return new Individual(mutatedTour);
        }
    }
}
