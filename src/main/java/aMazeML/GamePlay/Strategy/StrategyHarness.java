package aMazeML.GamePlay.Strategy;

import aMazeML.GamePlay.Preferences.*;
import aMazeML.MazeGenerator;
import aMazeML.MazeSolver;

import javafx.util.Pair;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.TTest;


/**
 * Example harness for repeated testing of a solution strategy
 */
public class StrategyHarness {

    public static void main(String[] args) throws InterruptedException {

        int n = 100000;

        // setup the generator preference
        Preference generatorPreference = new RightPreference();

        // set up a couple of different solvers, for comparison

        // use random weights
        Preference solverPreference_RANDOM = new RandomPreference();

        // weights learned by a single n=50 sample (see Analysis.FrequencyDistributions)
        Preference solverPreference_sample = new Preference(2, 9, 23, 16);

        // weights learned by averaging (least squares regression, performed out of band elsewhere)
        Preference solverPreference_model = new Preference(40.4, 110.2, 130.4, 210.0);

        // set up the descriptive statistics object (for performing t-tests)
        DescriptiveStatistics stats_random = new DescriptiveStatistics();
        DescriptiveStatistics stats_sample = new DescriptiveStatistics();
        DescriptiveStatistics stats_model = new DescriptiveStatistics();

        double[] treatment_sample = new double[n];
        double[] treatment_model = new double[n];
        double[] random = new double[n];

        // set up the maze generator with its preference
        MazeGenerator maze = new MazeGenerator(10, 10, generatorPreference);

        for(int k = 0; k < n; k++) {

            MazeSolver solver = new MazeSolver(solverPreference_sample);
            maze.GenerateMaze();

            System.out.println(k);
            Pair<Float, String[]> ret = solver.SolveMaze(maze);
            stats_sample.addValue(ret.getKey());
            treatment_sample[k] = ret.getKey();

            solver = new MazeSolver(solverPreference_model);
            ret = solver.SolveMaze(maze);
            stats_model.addValue(ret.getKey());
            treatment_model[k] = ret.getKey();

            solver = new MazeSolver(solverPreference_RANDOM);
            ret = solver.SolveMaze(maze);
            stats_random.addValue(ret.getKey());
            random[k] = ret.getKey();
        }

        TTest test = new TTest();
        double p_sampled = test.tTest(random, treatment_sample);
        double p_model = test.tTest(random, treatment_model);


        System.out.println("\ntreatment (sample):");
        System.out.println("median solve time: " +  (double)Math.round(stats_sample.getPercentile(50) * 100000d) / 100000d);
        System.out.println("variance: " +  (double)Math.round(stats_sample.getVariance() * 100000d) / 100000d);
        System.out.println("std: " +  (double)Math.round(stats_sample.getStandardDeviation() * 100000d) / 100000d);
        System.out.println("mean solve time: " +  (double)Math.round(stats_sample.getMean() * 100000d) / 100000d);
        System.out.println("p=" + (double)Math.round(p_sampled * 100000d) / 100000d);

        System.out.println("\ntreatment (model):");
        System.out.println("median solve time: " +  (double)Math.round(stats_model.getPercentile(50) * 100000d) / 100000d);
        System.out.println("variance: " +  (double)Math.round(stats_model.getVariance() * 100000d) / 100000d);
        System.out.println("std: " +  (double)Math.round(stats_model.getStandardDeviation() * 100000d) / 100000d);
        System.out.println("mean solve time: " +  (double)Math.round(stats_model.getMean() * 100000d) / 100000d);
        System.out.println("p=" + (double)Math.round(p_model * 100000d) / 100000d);


        System.out.println("\nnull hypothesis (random):");
        System.out.println("median solve time: " +  (double)Math.round(stats_random.getPercentile(50) * 100000d) / 100000d);
        System.out.println("variance: " +  (double)Math.round(stats_random.getVariance() * 100000d) / 100000d);
        System.out.println("std: " +  (double)Math.round(stats_random.getStandardDeviation() * 100000d) / 100000d);
        System.out.println("mean solve time: " +  (double)Math.round(stats_random.getMean() * 100000d) / 100000d);
    }
}
