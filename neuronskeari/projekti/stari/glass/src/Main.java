import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;

import java.util.ArrayList;
import java.util.List;

public class Main implements LearningEventListener {

    private final int brrojUlaznihVarijabli = 9;
    private final int brojIzlaznihVarijabli = 7;
    private final double[] learningRate = {0.2, 0.4, 0.6};
    private final int[] brojSkrivenihNeurona = {10, 20, 30};
    private List<Training> trainings = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        DataSet dataSet = DataSet.createFromFile("glass.csv", brrojUlaznihVarijabli, brojIzlaznihVarijabli, ",");
        Normalizer normalizer = new MaxNormalizer(dataSet);
        normalizer.normalize(dataSet);
        dataSet.shuffle();

        DataSet[] trainAndTest = dataSet.split(0.65, 0.35);
        DataSet trainSet = trainAndTest[0];
        DataSet testSet = trainAndTest[1];

        int brojTreninga = 0, ukupniBrojIteracija = 0;
        for (double lr:
             learningRate) {
            for (int skriveniNeuroni:
                 brojSkrivenihNeurona) {
                MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(brrojUlaznihVarijabli, skriveniNeuroni, brojIzlaznihVarijabli);
                MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
                learningRule.addListener(this);
                learningRule.setMomentum(0.6);
                learningRule.setLearningRate(lr);
                learningRule.setMaxIterations(1000);

                neuralNet.learn(trainSet);

                double accuracy = evaluateAccuracy(neuralNet, testSet);
                Training training = new Training(neuralNet, accuracy);
                trainings.add(training);

                brojTreninga++;
                ukupniBrojIteracija += learningRule.getCurrentIteration();
            }
        }
        System.out.println("Srednja vrednost broja iteracija: " + (double) ukupniBrojIteracija/brojTreninga);
        saveMaxAccuracyNet();
    }

    private void saveMaxAccuracyNet() {
        Training maxAccuracyTraining = trainings.get(0);
        for (Training training:
             trainings) {
            if(training.getAccuracy() > maxAccuracyTraining.getAccuracy()){
                maxAccuracyTraining = training;
            }
        }
        maxAccuracyTraining.getNeuralNet().save("nn.nnet");
    }

    private double evaluateAccuracy(MultiLayerPerceptron neuralNet, DataSet testSet) {
        double accuracy = 0;
        MatricaKonfuzije mk = new MatricaKonfuzije(brojIzlaznihVarijabli);
        for (DataSetRow row:
             testSet) {
            neuralNet.setInput(row.getInput());
            neuralNet.calculate();

            int actual = maxIndex(row.getDesiredOutput());
            int predicted = maxIndex(neuralNet.getOutput());

            mk.incrementElement(actual, predicted);
        }

        for (int i = 0; i < brojIzlaznihVarijabli; i++) {
            accuracy += (double) (mk.getTruePositive(i) + mk.getTrueNegative(i)) / mk.getTotal();
        }

        mk.ispisi();
        System.out.println("Accuracy je: " + (double) accuracy/brojIzlaznihVarijabli);
        return (double) accuracy/brojIzlaznihVarijabli;
    }

    private int maxIndex(double[] output) {
        int index = 0;
        for (int i = 0; i < output.length; i++) {
            if(output[index] < output[i]){
                index = i;
            }
        }
        return index;
    }

    @Override
    public void handleLearningEvent(LearningEvent event) {
        MomentumBackpropagation mbp = (MomentumBackpropagation) event.getSource();
        System.out.println("Iteracija: " + mbp.getCurrentIteration() + " Ukupna greska mreze: " + mbp.getTotalNetworkError() );
    }
}