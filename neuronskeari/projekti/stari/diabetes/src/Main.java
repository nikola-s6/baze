import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;

import java.util.ArrayList;
import java.util.List;

public class Main implements LearningEventListener {

    private final int numInput = 8;
    private final int numOutput = 1;
    private final double[] learningRates = {0.2, 0.4, 0.6};
    private List<Training> trainings = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    private void start() {
        DataSet dataSet = DataSet.createFromFile("diabetes_data.csv", numInput, numOutput, ",");
        Normalizer normalizer = new MaxNormalizer(dataSet);
        normalizer.normalize(dataSet);
        dataSet.shuffle();

        DataSet[] tt = dataSet.split(0.7, 0.3);
        DataSet trainSet = tt[0];
        DataSet testSet = tt[1];

        int sumOfIterations = 0, numOfTests = 0;
        for (double lr:
             learningRates) {
            MultiLayerPerceptron neauralNet = new MultiLayerPerceptron(numInput, 20, 16, numOutput);
            BackPropagation learningRule = neauralNet.getLearningRule();
            learningRule.addListener(this);
            learningRule.setMaxError(0.07);
            learningRule.setMomentum(0.5);
            learningRule.setLearningRate(lr);
            learningRule.setMaxIterations(1000);

            neauralNet.learn(trainSet);

            numOfTests++;
            sumOfIterations += learningRule.getCurrentIteration();

            double accuracy = evaluateAccuracy(neauralNet, testSet);

            Training training = new Training(neauralNet, accuracy);
            trainings.add(training);
        }
        System.out.println("Srednja vrednost broja iteracija je: " + (double) sumOfIterations/numOfTests);
        saveMaxAccuracy();
    }

    private void saveMaxAccuracy() {
        Training maxAccuracy = trainings.get(0);
        for (Training training:
             trainings) {
            if(training.getAccuracy() > maxAccuracy.getAccuracy()){
                maxAccuracy = training;
            }
        }
        maxAccuracy.getNeuralNet().save("nn.nnet");
    }

    private double evaluateAccuracy(MultiLayerPerceptron neauralNet, DataSet testSet) {
        MatricaKonfuzije mk = new MatricaKonfuzije(2);
        for (DataSetRow row:
             testSet) {
            neauralNet.setInput(row.getInput());
            neauralNet.calculate();

            int actual = (int) Math.round(row.getDesiredOutput()[0]);
            int predicted = (int) Math.round(neauralNet.getOutput()[0]);

            mk.incrementElement(actual, predicted);

        }
        mk.ispisi();
        System.out.println("Accuracy je: " + (double) (mk.getTruePositive(0) + mk.getTrueNegative(0)) / mk.getBrojObzervacija());
        return (double) (mk.getTruePositive(0) + mk.getTrueNegative(0)) / mk.getBrojObzervacija();
    }

    @Override
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        System.out.println("Iteration: " + bp.getCurrentIteration() +
                " Total network error: " + bp.getTotalNetworkError());
    }
}