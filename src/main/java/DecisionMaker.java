public class DecisionMaker {

    public double[] comArrayValues;

    DecisionMaker(AHP ahp){
        comArrayValues = ahp.getPairwiseComparisonArray();
    }

    void setCriteriaImportance(double[] compArray){
        System.arraycopy(compArray, 0, comArrayValues, 0, compArray.length);

    }
}
