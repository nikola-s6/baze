public class MatricaKonfuzije {
    private int[][] matrica;

    /**
     * Number of classes
     */
    private int dimenzija;

    private int brojObzervacija = 0;
    
    public MatricaKonfuzije(int dimenzije){
        this.dimenzija = dimenzije;
        this.matrica = new int[dimenzije][dimenzije];
    }

    public void incrementElement(int actual, int predicted) {
        matrica[actual][predicted]++;
        brojObzervacija++;
    }

    public int getTruePositive(int clsIdx) {
        return (int)matrica[clsIdx][clsIdx];
    }

    public int getTrueNegative(int clsIdx) {
        int trueNegative = 0;

        for(int i = 0; i < dimenzija; i++) {
            if (i == clsIdx) continue;
            for(int j = 0; j < dimenzija; j++) {
                if (j == clsIdx) continue;
                trueNegative += matrica[i][j];
            }
        }

        return trueNegative;
    }

    public int getFalsePositive(int clsIdx) {
        int falsePositive = 0;

        for(int i=0; i<dimenzija; i++) {
            if (i == clsIdx) continue;
            falsePositive += matrica[i][clsIdx];
        }

        return falsePositive;
    }

    public int getFalseNegative(int clsIdx) {
        int falseNegative = 0;

        for(int i=0; i<dimenzija; i++) {
            if (i == clsIdx) continue;
            falseNegative += matrica[clsIdx][i];
        }

        return falseNegative;
    }
    
    public void ispisi(){
        for (int i = 0; i < dimenzija; i++) {
            for (int j = 0; j < dimenzija; j++) {
                System.out.print(matrica[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int[][] getMatrica() {
        return matrica;
    }

    public void setMatrica(int[][] matrica) {
        this.matrica = matrica;
    }

    public int getDimenzija() {
        return dimenzija;
    }

    public void setDimenzija(int dimenzija) {
        this.dimenzija = dimenzija;
    }

    public int getBrojObzervacija() {
        return brojObzervacija;
    }

    public void setBrojObzervacija(int brojObzervacija) {
        this.brojObzervacija = brojObzervacija;
    }
}
