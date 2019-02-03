import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Test {

    private void calculateAHP(){

        AHP ahp = new AHP(Config.criteria);

        double compArray[] = ahp.getPairwiseComparisonArray();

        // Set the pairwise comparison values
        compArray[0] = Config.BANDWIDTH_SPEED;
        compArray[1] = Config.BANDWIDTH_AVAILABILITY;
        compArray[2] = Config.BANDWIDTH_SECURITY;
        compArray[3] = Config.BANDWIDTH_PRICE;
        compArray[4] = Config.SPEED_AVAILABILITY;
        compArray[5] = Config.SPEED_SECURITY;
        compArray[6] = Config.SPEED_PRICE;
        compArray[7] = Config.AVAILABLITY_SECURITY;
        compArray[8] = Config.AVAIALABILITY_PRICE;
        compArray[9] = Config.SECURITY_PRICE;

        ahp.setPairwiseComparisonArray(compArray);

        for (int i = 0; i < ahp.getNrOfPairwiseComparisons(); i++) {
            System.out.print("Importance of " + Config.criteria[ahp.getIndicesForPairwiseComparison(i)[0]] + " compared to ");
            System.out.print(Config.criteria[ahp.getIndicesForPairwiseComparison(i)[1]] + "= ");
            System.out.println(String.valueOf(ahp.getPairwiseComparisonArray()[i]));
        }

        System.out.println("Consistency Index: " + Config.df.format(ahp.getConsistencyIndex()));
        System.out.println("Consistency Ratio: " + Config.df.format(ahp.getConsistencyRatio()) + "%");
        System.out.println("Weights: ");
        for (int k=0; k<ahp.getWeights().length; k++) {
            Config.ahpWeights[k] = ahp.getWeights()[k];
            System.out.println(Config.criteria[k] + ": " + Config.df.format(ahp.getWeights()[k]));
        }
    }

    private void topsisMethod(){

        Topsis topsis = new Topsis();
        topsis.start();
    }

    public static void main(String[] args) {
        Test test = new Test();

        System.out.println("Calculating AHP Criteria weighting: ");
        test.calculateAHP();

        System.out.println("********************************");
        System.out.println("Calculating Fuzzy TOPSIS: ");

        test.topsisMethod();
    }
}
