public class DecisionMaker {

    private double[] comArrayValues;

    DecisionMaker(AHP ahp){
        comArrayValues = ahp.getPairwiseComparisonArray();
    }

    void setCriteriaImportance(double[] compArray){
        System.arraycopy(compArray, 0, comArrayValues, 0, compArray.length);
    }

    public double[] getComArrayValues() {
        return comArrayValues;
    }
}
