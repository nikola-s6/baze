import org.neuroph.eval.classification.ConfusionMatrix;

public class MatricaKonfuzije {
    private int[][] values;

    private int classCount;

    private int total = 0;

    public MatricaKonfuzije(int dimenzije){
        this.classCount = dimenzije;
        this.values = new int[dimenzije][dimenzije];
    }

    public void incrementElement(int actual, int predicted) {
        values[actual][predicted]++;
        total++;
    }
    public int getTruePositive(int clsIdx) {
        return (int)values[clsIdx][clsIdx];
    }

    public int getTrueNegative(int clsIdx) {
        int trueNegative = 0;

        for(int i = 0; i < classCount; i++) {
            if (i == clsIdx) continue;
            for(int j = 0; j < classCount; j++) {
                if (j == clsIdx) continue;
                trueNegative += values[i][j];
            }
        }

        return trueNegative;
    }

    public int getFalsePositive(int clsIdx) {
        int falsePositive = 0;

        for(int i=0; i<classCount; i++) {
            if (i == clsIdx) continue;
            falsePositive += values[i][clsIdx];
        }

        return falsePositive;
    }

    public int getFalseNegative(int clsIdx) {
        int falseNegative = 0;

        for(int i=0; i<classCount; i++) {
            if (i == clsIdx) continue;
            falseNegative += values[clsIdx][i];
        }

        return falseNegative;
    }

    public void ispisi(){
        for (int i = 0; i < classCount; i++) {
            for (int j = 0; j < classCount; j++) {
                System.out.print(values[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int[][] getValues() {
        return values;
    }

    public void setValues(int[][] values) {
        this.values = values;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
