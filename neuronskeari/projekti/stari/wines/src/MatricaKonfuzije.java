public class MatricaKonfuzije {
    /**
     * matrica of confusion matrix
     */
    private int[][] matrica;

    /**
     * Number of classes
     */
    private int dimenzije;

    private int suma = 0;

    public MatricaKonfuzije(int brojIzlaznihVarijabli) {
        this.dimenzije = brojIzlaznihVarijabli;
        this.matrica = new int[dimenzije][brojIzlaznihVarijabli];
    }

    public void incrementElement(int actual, int predicted) {
        matrica[actual][predicted]++;
        suma++;
    }

    public int getTruePositive(int clsIdx) {
        return (int)matrica[clsIdx][clsIdx];
    }

    public int getTrueNegative(int clsIdx) {
        int trueNegative = 0;

        for(int i = 0; i < dimenzije; i++) {
            if (i == clsIdx) continue;
            for(int j = 0; j < dimenzije; j++) {
                if (j == clsIdx) continue;
                trueNegative += matrica[i][j];
            }
        }

        return trueNegative;
    }

    public int getFalsePositive(int clsIdx) {
        int falsePositive = 0;

        for(int i=0; i<dimenzije; i++) {
            if (i == clsIdx) continue;
            falsePositive += matrica[i][clsIdx];
        }

        return falsePositive;
    }

    public int getFalseNegative(int clsIdx) {
        int falseNegative = 0;

        for(int i=0; i<dimenzije; i++) {
            if (i == clsIdx) continue;
            falseNegative += matrica[clsIdx][i];
        }

        return falseNegative;
    }

    public int[][] getMatrica() {
        return matrica;
    }

    public void setMatrica(int[][] matrica) {
        this.matrica = matrica;
    }

    public int getDimenzije() {
        return dimenzije;
    }

    public void setDimenzije(int dimenzije) {
        this.dimenzije = dimenzije;
    }

    public int getSuma() {
        return suma;
    }

    public void setSuma(int suma) {
        this.suma = suma;
    }

    public void ispisiMatricu(){
        for (int i = 0; i < dimenzije; i++) {
            for (int j = 0; j < dimenzije; j++) {
                System.out.print(matrica[i][j] + " ");
            }
            System.out.println();
        }
    }
}
