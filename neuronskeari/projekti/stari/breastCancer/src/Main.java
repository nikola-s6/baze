import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class Main implements LearningEventListener {

    private final int numInput = 30;
    private final int numOutput = 1;
    private final double[] learningRates = {0.2, 0.4, 0.6};
    private final int[] numHiddenNeurons = {10, 20, 30};
    private List<Training> trainings = new ArrayList<>();


    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    private void start() {
        DataSet dataSet = DataSet.createFromFile("breast_cancer_data.csv", numInput, numOutput, ",");
        Normalizer normalizer = new MaxNormalizer(dataSet);
        normalizer.normalize(dataSet);
        dataSet.shuffle();

        DataSet[] tt = dataSet.split(0.65, 0.35);
        DataSet trainData = tt[0];
        DataSet testData = tt[1];

        int sumOfIterations = 0, numOfTrainings = 0;
        for (double lr:
             learningRates) {
            for (int hn:
                 numHiddenNeurons) {
                MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(numInput, hn, numOutput);
                MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();

                learningRule.setMomentum(0.7);
                learningRule.setLearningRate(lr);
                learningRule.setMaxIterations(1000);
                learningRule.addListener(this);

                neuralNet.learn(trainData);

                numOfTrainings++;
                sumOfIterations += learningRule.getCurrentIteration();

                double mse = calculateMse(neuralNet, testData);

                Training training = new Training(neuralNet, mse);
                trainings.add(training);
            }
        }
        System.out.println("Srednja vrednost broja iteracija je: " + (double) sumOfIterations/numOfTrainings);
        saveMinMSE();
    }

    private double calculateMse(MultiLayerPerceptron neuralNet, DataSet testData) {
        double sumError = 0;
        for (DataSetRow row:
             testData) {
            neuralNet.setInput(row.getInput());
            neuralNet.calculate();

            int actual = (int) Math.round(row.getDesiredOutput()[0]);
            int predicted = (int) Math.round(neuralNet.getOutput()[0]);

            sumError += Math.pow(actual - predicted, 2);
        }
        System.out.println("Mean square error: " + (double) sumError / (2 * testData.size()));
        return (double) sumError / (2 * testData.size());
    }

    private void saveMinMSE() {
        Training minMse = trainings.get(0);
        for (Training training:
             trainings) {
            if(training.getMse() < minMse.getMse()){
                minMse = training;
            }
        }
        minMse.getNeuralNet().save("nn.nnet");
    }

    @Override
    public void handleLearningEvent(LearningEvent event) {
        MomentumBackpropagation momentumbp = (MomentumBackpropagation) event.getSource();
        System.out.println("Iteration: " + momentumbp.getCurrentIteration() + " Total network error"
                + momentumbp.getTotalNetworkError());
    }
}