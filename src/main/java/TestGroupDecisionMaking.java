import java.util.Arrays;

public class TestGroupDecisionMaking {

    public static void main(String[] args) {
        TestGroupDecisionMaking test = new TestGroupDecisionMaking();

        AHP ahp = AHP.getInstance(Config.criteria);

        System.out.println("Calculating AHP Criteria weighting: ");

        /* We need to set 10 relative criteria importance for each decision maker
        {BANDWIDTH_SPEED, BANDWIDTH_AVAILABILITY, BANDWIDTH_SECURITY, BANDWIDTH_PRICE,
        SPEED_AVAILABILITY, SPEED_SECURITY, SPEED_PRICE,
        AVAILABLITY_SECURITY, AVAIALABILITY_PRICE,
        SECURITY_PRICE}
        */

        // energy efficient and fast response (normal execution)
        DecisionMaker dm1 = new DecisionMaker(ahp);
        double[] criteriaImportance1 = {1.0, 5.0, 7.0, 9.0, 5.0, 6.0, 8.0, 3.0, 3.0, 2.0};

        // Reduce energy consumption to extend battery lifetime (e.g. when battery level is < 20)
        DecisionMaker dm2 = new DecisionMaker(ahp);
        double[] criteriaImportance2 = {3.0, 7.0, 9.0, 9.0, 3.0, 7.0, 9.0, 5.0, 7.0, 2.0};

        // Reduce response time no matter the impact on energy consumption (e.g. when battery level > 80 or device is currently charging)
        DecisionMaker dm3 = new DecisionMaker(ahp);
        double[] criteriaImportance3 = {1.0/3.0, 5.0, 7.0, 9.0, 5.0, 9.0, 9.0, 5.0, 7.0, 2.0};

        // Increase security level (Only offload if the offloading server is trusted and authenticated)
        DecisionMaker dm4 = new DecisionMaker(ahp);
        double[] criteriaImportance4 = {1.0, 5.0, 1.0/5.0, 9.0, 5.0, 1.0/5.0, 8.0, 1.0/7.0, 3.0, 9.0};

        // Reduce the financial cost
        DecisionMaker dm5 = new DecisionMaker(ahp);
        double[] criteriaImportance5 = {1.0, 5.0, 7.0, 1.0/5.0, 5.0, 6.0, 1.0/5.0, 3.0, 1.0/7.0, 1.0/9.0};

        // Start calculating pairwise comparison matrix for each DM
        dm1.setCriteriaImportance(criteriaImportance1);
        double[] w1 = test.calculateAHP(ahp, dm1.comArrayValues);
        System.out.println("A1: " + ahp.toString());
        System.out.println("W1: " + Arrays.toString(w1) + "\n");

        dm2.setCriteriaImportance(criteriaImportance2);
        double[] w2 = test.calculateAHP(ahp, dm2.comArrayValues);
        System.out.println("A2: " + ahp.toString());
        System.out.println("W2: " + Arrays.toString(w2) + "\n");

        dm3.setCriteriaImportance(criteriaImportance3);
        double[] w3 = test.calculateAHP(ahp, dm3.comArrayValues);
        System.out.println("A3: " + ahp.toString());
        System.out.println("W3: " + Arrays.toString(w3) + "\n");

        dm4.setCriteriaImportance(criteriaImportance4);
        double[] w4 = test.calculateAHP(ahp, dm4.comArrayValues);
        System.out.println("A4: " + ahp.toString());
        System.out.println("W4: " + Arrays.toString(w4) + "\n");

        dm5.setCriteriaImportance(criteriaImportance5);
        double[] w5 = test.calculateAHP(ahp, dm5.comArrayValues);
        System.out.println("A5: " + ahp.toString());
        System.out.println("W5: " + Arrays.toString(w5) + "\n");

        // This can be set according to the application requirements
        Double[] dmWeights = {2.5/5.0, 2.0/5.0, 0.2/5.0, 0.2/5.0, 0.1/5.0};
        Double[] collectivePriorityWeightVector = new Double[w1.length];

        double sum = 0;
        for (int i=0; i<w1.length; i++) {
            Double wk = Math.pow(w1[i], dmWeights[0]) * Math.pow(w2[i], dmWeights[1]) * Math.pow(w3[i], dmWeights[2])
                    * Math.pow(w4[i], dmWeights[3]) * Math.pow(w5[i], dmWeights[4]);
            collectivePriorityWeightVector[i] = wk;
            sum += wk;
        }

        System.out.println("Wc: " + Arrays.toString(collectivePriorityWeightVector) + "\n");

        Config.ahpWeights = collectivePriorityWeightVector;

        System.out.println("End of AHP");
        System.out.println("********************************");
        System.out.println("Calculating Fuzzy TOPSIS: ");

        test.topsisMethod();
    }

    private double[] calculateAHP(AHP ahp, double[] compArray){

        ahp.setPairwiseComparisonArray(compArray);

        double[] ahpWeights = new double[Config.criteria.length];

        for (int i = 0; i < ahp.getNrOfPairwiseComparisons(); i++) {
            System.out.print("Importance of " + Config.criteria[ahp.getIndicesForPairwiseComparison(i)[0]] + " compared to ");
            System.out.print(Config.criteria[ahp.getIndicesForPairwiseComparison(i)[1]] + "= ");
            System.out.println(String.valueOf(ahp.getPairwiseComparisonArray()[i]));
        }

        System.out.println("Consistency Index: " + Config.df.format(ahp.getConsistencyIndex()));
        System.out.println("Consistency Ratio: " + Config.df.format(ahp.getConsistencyRatio()) + "%");
        System.out.println("Weights: ");
        for (int k=0; k<ahp.getWeights().length; k++) {
            ahpWeights[k] = ahp.getWeights()[k];
            System.out.println(Config.criteria[k] + ": " + Config.df.format(ahp.getWeights()[k]));
        }

        return ahpWeights;
    }

    private void topsisMethod(){

        Topsis topsis = new Topsis();
        topsis.start();
    }
}
