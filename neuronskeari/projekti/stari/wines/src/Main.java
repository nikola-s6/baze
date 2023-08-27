import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;

import java.util.ArrayList;
import java.util.List;

public class Main implements LearningEventListener {

    private final int brojUlaznihVarijabli = 13;
    private final int brojIzlaznihVarijabli = 3;
    private final double[] learningrates = {0.2, 0.4, 0.6};

    private List<Train> trainings = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        DataSet dataSet = DataSet.createFromFile("wines.csv", brojUlaznihVarijabli, brojIzlaznihVarijabli, ",");
        Normalizer normalizer = new MaxNormalizer(dataSet);
        normalizer.normalize(dataSet);
        dataSet.shuffle();

        DataSet[] treningITestSetovi = dataSet.split(0.7, 0.3);
        DataSet trainSet = treningITestSetovi[0];
        DataSet testSet = treningITestSetovi[1];

        int ukupanBrojIteracija = 0, brojTestova = 0;
        for (double learningRate:
             learningrates) {
            MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(brojUlaznihVarijabli, 22, brojIzlaznihVarijabli);

            BackPropagation learningRule = neuralNet.getLearningRule();

            learningRule.addListener(this);
            learningRule.setMaxError(0.02);
            learningRule.setLearningRate(learningRate);

            neuralNet.learn(trainSet);
            brojTestova++;
            ukupanBrojIteracija += learningRule.getCurrentIteration();

            double accuracy = evaluateAccuracy(neuralNet, testSet);

            Train train = new Train(neuralNet, accuracy);
            trainings.add(train);
        }
        System.out.println("Srednja vrednost broja iteracija je: " + (double) ukupanBrojIteracija/brojTestova);
        saveNetWithMaxAccuracy();

    }

    private void saveNetWithMaxAccuracy() {
        Train maxAccuracyTrain = trainings.get(0);
        for (Train train:
             trainings) {
            if(train.getAccuracy() > maxAccuracyTrain.getAccuracy()){
                maxAccuracyTrain = train;
            }
        }
        maxAccuracyTrain.getNeuralNet().save("nn.nnet");
    }

    private double evaluateAccuracy(MultiLayerPerceptron neuralNet, DataSet testSet) {
        double accuracy = 0;
        MatricaKonfuzije mk = new MatricaKonfuzije(brojIzlaznihVarijabli);
        for (DataSetRow red :
                testSet) {
            neuralNet.setInput(red.getInput());
            neuralNet.calculate();

            int actual = getMaxIndex(red.getDesiredOutput());
            int predicted = getMaxIndex(neuralNet.getOutput());
//            int actual = Math.round(red.getDesiredOutput()[0]);

            mk.incrementElement(actual, predicted);
        }
        mk.ispisiMatricu();

        for (int i = 0; i < mk.getDimenzije(); i++) {
            accuracy += (double) (mk.getTruePositive(i) + mk.getTrueNegative(i)) / mk.getSuma();
        }

//        sumError = (double) Math.pow(actual - predicted, 2);
//        mse = (double) sumError / (2 * testSet.size());

        System.out.println("Accuracy: " + (double) accuracy / mk.getDimenzije());
        return (double) accuracy / mk.getDimenzije();
    }

    private int getMaxIndex(double[] desiredOutput) {
        int maxIndex = 0;
        for (int i = 1; i < desiredOutput.length; i++) {
            if(desiredOutput[maxIndex] < desiredOutput[i]){
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    @Override
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        System.out.println("Trenutna iteracija " + bp.getCurrentIteration() +
                " Totalna greska mreze " + bp.getTotalNetworkError());
    }
}