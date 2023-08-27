import org.neuroph.core.NeuralNetwork;

public class Training {
    private NeuralNetwork neuralNet;
    private double accuracy;

    public Training(NeuralNetwork neuralNet, double accuracy) {
        this.neuralNet = neuralNet;
        this.accuracy = accuracy;
    }

    public NeuralNetwork getNeuralNet() {
        return neuralNet;
    }

    public void setNeuralNet(NeuralNetwork neuralNet) {
        this.neuralNet = neuralNet;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}
