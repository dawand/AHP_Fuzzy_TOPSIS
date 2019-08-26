import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealVector;

public class AHP {

    // Singleton instance
    private static AHP instance;

    // Random Consistency Index
    private static double[] RI = {0.0, 0.0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49};

    // The matrix
    private Array2DRowRealMatrix mtx;

    // Pairwise Comparison Array of criteria
    private double[] pairwiseComparisonArray;

    // Number of alternatives
    private int nrAlternatives;

    // The resulting weights/priorities
    private double[] weights;

    // Corresponds to the weights
    private String[] labels = null;

    private EigenDecomposition evd;

    /**
     * Convenience array, i.e. comparisonIndices[length=NumberOfPairwiseComparisons][2]
     * Contains minimum number of comparisons.
     */
    private int[][] comparisonIndices;

    /**
     * Index of the greatest Eigenvalue/ -vector
     */
    private int evIdx = 0; // index of actual eigenvalue/-vector

    static AHP getInstance(String[] labels) {
        if(instance != null){
            return instance;
        } else {
            instance = new AHP(labels);
        }
        return instance;
    }

    private AHP(String[] labels) {
        this(labels.length);
        this.labels = labels;
    }

    private AHP(int nrAlternatives) {
        this.nrAlternatives = nrAlternatives;
        mtx = new Array2DRowRealMatrix(nrAlternatives, nrAlternatives);
        weights = new double[nrAlternatives];

        pairwiseComparisonArray = new double[getNrOfPairwiseComparisons()];
        comparisonIndices = new int[getNrOfPairwiseComparisons()][];
        for (int i = 0; i < getNrOfPairwiseComparisons(); i++) {
            comparisonIndices[i] = new int[2];
        }

        // only need diagonal 1, but set everything to 1.0
        for (int row = 0; row < nrAlternatives; row++) {
            for (int col = 0; col < nrAlternatives; col++) {
                getMtx().setEntry(row, col, 1.0);
            }
        }
    }

    /**
     *
     * @return the number of pairwise comparisons which have to be done by the user
     */
    int getNrOfPairwiseComparisons() {
        return ((nrAlternatives - 1) * nrAlternatives) / 2;
    }

    /**
     *
     * @return the user input of the pairwise comparisons
     */
    double[] getPairwiseComparisonArray() {
        return pairwiseComparisonArray;
    }

    /**
     * Set the pairwise comparison scores and calculate all relevant numbers
     * @param a
     */
    void setPairwiseComparisonArray(double[] a) {
        int i = 0;
        for (int row = 0; row < nrAlternatives; row++) {
            for (int col = row + 1; col < nrAlternatives; col++) {
                //System.out.println(row + "/" + col + "=" + a[i]);
                getMtx().setEntry(row, col, a[i]);
                getMtx().setEntry(col, row, 1.0 / getMtx().getEntry(row, col));
                comparisonIndices[i][0] = row;
                comparisonIndices[i][1] = col;
                i++;
            }
        }
    }

    /**
     * Sets the EigenDecomposition
     */
    void setEvd(){
        evd = new EigenDecomposition(getMtx());

        evIdx = 0;
        for (int k = 0; k < evd.getRealEigenvalues().length; k++) {
            //System.out.println(evd.getRealEigenvalues()[k]);
            evIdx = (evd.getRealEigenvalue(k) > evd.getRealEigenvalue(evIdx)) ? k : evIdx;
        }
        //System.out.println("evIdx=" + evIdx);
        //System.out.println("EigenValue=" + evd.getRealEigenvalue(evIdx));

        double sum = 0.0;
        RealVector v = evd.getEigenvector(evIdx);
        for (double d : v.toArray()) {
            sum += d;
        }
        //System.out.println(sum);
        for (int k = 0; k < v.getDimension(); k++) {
            weights[k] = v.getEntry(k) / sum;
        }
    }

    /**
     *
     * @param arrayIdx
     * @return
     */
    int[] getIndicesForPairwiseComparison(int arrayIdx) {
        return comparisonIndices[arrayIdx];
    }

    /**
     *
     * @return resulting weights for alternatives
     */
    double[] getWeights() {
        return weights;
    }

    /**
     *
     * @return the consistency index
     */
    double getConsistencyIndex() {
        return (evd.getRealEigenvalue(evIdx) - (double) nrAlternatives) / (double) (nrAlternatives - 1);
    }

    /**
     *
     * @return the consistency ratio. Should be less than 10%
     */
    double getConsistencyRatio() {
        return getConsistencyIndex() / RI[nrAlternatives] * 100.0;
    }

    /**
     * Calculates the GCI(A) According to
     * "The geometric consistency index: Approximated thresholds" by Aguaron et al.
     * @return GCI(A)
     */
    double getGeometricConsistencyIndex(){

        Array2DRowRealMatrix e_ij = new Array2DRowRealMatrix(nrAlternatives, nrAlternatives);

        for (int row = 0; row < nrAlternatives; row++){
            for (int col = row + 1; col < nrAlternatives; col++) {
                //System.out.println(row + "/" + col + "=" + a[i]);
                double e = getMtx().getEntry(row,col) * (weights[col] / weights[row]);
                e_ij.setEntry(row, col, e);
            }
        }

        double gci = 0;

        for (int row = 0; row < nrAlternatives; row++) {
            for (int col = row + 1; col < nrAlternatives; col++) {
                gci += Math.log(e_ij.getEntry(row,col)) * Math.log(e_ij.getEntry(row,col));
            }
        }

        return gci * (2.0 / ((nrAlternatives - 1) * (nrAlternatives - 2)));
    }

    /**
     *
     * Calculates the GCCI(A) According to
     * "Consensus models for AHP group decision making under row geometric mean prioritization method" by Dong et al
     * @return GCCI(A)
     */
    double getGeometricCardinalConsistencyIndex(){

        double gcci = 0;

        for (int row = 0; row < nrAlternatives; row++){
            for (int col = row + 1; col < nrAlternatives; col++) {
                gcci += Math.pow((Math.log(getMtx().getEntry(row,col)) - Math.log(weights[row]) + Math.log(weights[col])),2);
            }
        }

        return gcci * (2.0 / ((nrAlternatives - 1) * (nrAlternatives - 2)));
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<nrAlternatives; i++)
            sb.append(getMtx().getRowVector(i)).append("\n");
        return sb.toString();
    }

    /**
     * Created for the GDM to assign A_i to mtx
     * @return mtx
     */
    public Array2DRowRealMatrix getMtx() {
        return mtx;
    }

    /**
     * Created for setting Ac to mtx
     * @param mtx
     */
    public void setMtx(Array2DRowRealMatrix mtx) {
        this.mtx = mtx;
    }
}
