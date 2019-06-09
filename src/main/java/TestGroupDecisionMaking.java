public class TestGroupDecisionMaking {

    public static void main(String[] args) {
        TestGroupDecisionMaking test = new TestGroupDecisionMaking();

        AHP ahp1 = new AHP(Config.criteria);
        AHP ahp2 = new AHP(Config.criteria);
        AHP ahp3 = new AHP(Config.criteria);
        AHP ahp4 = new AHP(Config.criteria);
        AHP ahp5 = new AHP(Config.criteria);

        System.out.println("Calculating AHP Criteria weighting: ");

        /* We need to set 10 relative criteria importance for each decision maker
        {BANDWIDTH_SPEED, BANDWIDTH_AVAILABILITY, BANDWIDTH_SECURITY, BANDWIDTH_PRICE,
        SPEED_AVAILABILITY, SPEED_SECURITY, SPEED_PRICE,
        AVAILABLITY_SECURITY, AVAIALABILITY_PRICE,
        SECURITY_PRICE}
        */

        // energy efficient and fast response (normal execution)
        DecisionMaker dm1 = new DecisionMaker(ahp1);
        double[] criteriaImportance1 = {1.0, 5.0, 7.0, 9.0, 5.0, 6.0, 8.0, 3.0, 3.0, 2.0};

        // Reduce energy consumption to extend battery lifetime (e.g. when battery level is < 20)
        DecisionMaker dm2 = new DecisionMaker(ahp2);
        double[] criteriaImportance2 = {3.0, 7.0, 9.0, 9.0, 3.0, 7.0, 9.0, 5.0, 7.0, 2.0};

        // Reduce response time no matter the impact on energy consumption (e.g. when battery level > 80 or device is currently charging)
        DecisionMaker dm3 = new DecisionMaker(ahp3);
        double[] criteriaImportance3 = {1.0/3.0, 5.0, 7.0, 9.0, 5.0, 9.0, 9.0, 5.0, 7.0, 2.0};

        // Increase security level (Only offload if the offloading server is trusted and authenticated)
        DecisionMaker dm4 = new DecisionMaker(ahp4);
        double[] criteriaImportance4 = {1.0, 5.0, 1.0/5.0, 9.0, 5.0, 1.0/5.0, 8.0, 1.0/7.0, 3.0, 9.0};

        // Reduce the financial cost
        DecisionMaker dm5 = new DecisionMaker(ahp5);
        double[] criteriaImportance5 = {1.0, 5.0, 7.0, 1.0/5.0, 5.0, 6.0, 1.0/5.0, 3.0, 1.0/7.0, 1.0/9.0};

        // Start calculating pairwise comparison matrix for each DM
        dm1.setCriteriaImportance(criteriaImportance1);
        test.calculateAHP(ahp1, dm1.comArrayValues);
        double[] w1 = ahp1.getWeights();
        System.out.println("ahp1: " + ahp1.toString());

        dm2.setCriteriaImportance(criteriaImportance2);
        test.calculateAHP(ahp2, dm2.comArrayValues);
        double[] w2 = ahp2.getWeights();
        System.out.println("ahp2: " + ahp2.toString());

        dm3.setCriteriaImportance(criteriaImportance3);
        test.calculateAHP(ahp3, dm3.comArrayValues);
        double[] w3 = ahp3.getWeights();
        System.out.println("ahp3: " + ahp3.toString());

        dm4.setCriteriaImportance(criteriaImportance4);
        test.calculateAHP(ahp4, dm4.comArrayValues);
        double[] w4 = ahp4.getWeights();
        System.out.println("ahp4: " + ahp4.toString());

        dm5.setCriteriaImportance(criteriaImportance5);
        test.calculateAHP(ahp5, dm5.comArrayValues);
        double[] w5 = ahp5.getWeights();
        System.out.println("ahp5: " + ahp5.toString());

        // This can be set according to the application requirements
        Double[] dmWeights = {2.5/5.0, 2.0/5.0, 0.2/5.0, 0.2/5.0, 0.1/5.0};
        Double[] collectivePriorityWeightVector = new Double[w1.length];

        double sum = 0;
        for (int i=0; i<w1.length; i++) {
            Double wk = Math.pow(w1[i], dmWeights[0]) * Math.pow(w2[i], dmWeights[1]) * Math.pow(w3[i], dmWeights[2])
                    * Math.pow(w4[i], dmWeights[3]) * Math.pow(w5[i], dmWeights[4]);
            collectivePriorityWeightVector[i] = wk;
            sum += wk;
            System.out.println("wc" + i + ": " + collectivePriorityWeightVector[i]);
        }

        Config.ahpWeights = collectivePriorityWeightVector;

        System.out.println(sum);
        System.out.println("********************************");
        System.out.println("Calculating Fuzzy TOPSIS: ");

        test.topsisMethod();
    }

    private void calculateAHP(AHP ahp, double[] compArray){

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
}
