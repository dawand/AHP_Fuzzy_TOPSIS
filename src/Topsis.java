import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Topsis {

    private HashMap<String, ArrayList<Double>> sitesMatrix;
    private HashMap<String, ArrayList<Fuzzy>> availableSites;
    private ArrayList<Fuzzy> criteriaImportance;

    public Topsis(){
        criteriaImportance = new ArrayList<>();
        sitesMatrix =new HashMap<>();
        availableSites = new HashMap<>();
    }

    public void start(){
        for (String alternative : Config.alternatives) {
            criteriaImportance = profileNode(alternative);
            availableSites.put(alternative, criteriaImportance);
        }

        sitesMatrix = calculateFuzzyTopsis(availableSites);

        HashMap<String, Double> idealDistances = calculateDistance(sitesMatrix, true);
        HashMap<String, Double> antiIdealDistances = calculateDistance(sitesMatrix, false);

        TreeMap<String, Double> ccStar = calculateRelativeCloseness(idealDistances, antiIdealDistances);

        System.out.println("**********************************");
        System.out.println("Final Ranking:");

        for (Map.Entry<String, Double> entry: ccStar.entrySet()){
            System.out.println(entry.getKey() + ": " + Config.df.format(entry.getValue()));
        }
    }

    private HashMap<String, ArrayList<Double>> calculateFuzzyTopsis(HashMap<String, ArrayList<Fuzzy>> availableSites) {

//        System.out.println(availableSites.keySet());
//        System.out.println(availableSites.values());

        for (Map.Entry<String,ArrayList<Fuzzy>> entry: availableSites.entrySet()) {

            System.out.println(entry);

            ArrayList<Double> weightedMatrix = new ArrayList<>();

            for (int k = 0; k < entry.getValue().size(); k++) {
                for (double fuzzyValue:entry.getValue().get(k).getValue()) {
                    weightedMatrix.add(fuzzyValue);
                }
            }

            sitesMatrix.put(entry.getKey(), weightedMatrix);
        }

        System.out.println("unweighted fuzzy values: " + sitesMatrix);


        for (Map.Entry<String,ArrayList<Double>> entry: sitesMatrix.entrySet()) {

            ArrayList<Double> weightedMatrix = new ArrayList<>();

            int j = 0;
            for (int i = 0; i < entry.getValue().size(); i++) {
                weightedMatrix.add(entry.getValue().get(i) * Config.ahpWeights[j]);
                if (j%3 == 0)
                    j++;
            }
            entry.setValue(weightedMatrix);
        }

        System.out.println("weighted fuzzy values: " + sitesMatrix);

        return sitesMatrix;
    }

    private ArrayList<Fuzzy> profileNode(String node){
        ArrayList<Fuzzy> siteCriteria = new ArrayList<>();

        // Mobile node
        if (node.equalsIgnoreCase(Config.alternatives[0])){
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.GOOD);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_LOW);
        }
        else if (node.equalsIgnoreCase(Config.alternatives[1])) { // Edge
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.LOW);
        }
        // Public cloud instance
        else {
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.GOOD);
            siteCriteria.add(Fuzzy.VERY_HIGH);
        }

        return siteCriteria;
    }

    private HashMap<String, Double> calculateDistance(HashMap<String, ArrayList<Double>> sitesMatrix, boolean ideal) {
        EuclideanDistance distance = new EuclideanDistance();
        // The normalized values for the ideal solution and negative ideal solution on criteria are always (1,1,1) and (0,0,0) respectively
        Double dValue = 0.0;
        HashMap<String, Double> results = new HashMap<>();

        for (Map.Entry<String,ArrayList<Double>> entry: sitesMatrix.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i = i + 3) {
                double[] fuzzyValues = {entry.getValue().get(i), entry.getValue().get(i+1), entry.getValue().get(i+2)};
                if (ideal) { // For D+
                    if (Config.costCriteria[i/3]) { // cost value for price criterion
                        dValue += distance.compute(fuzzyValues, Config.antiIdealSolution) * (1.0/3.0);
                    } else {
                        dValue += distance.compute(fuzzyValues, Config.idealSolution) * (1.0/3.0);
                    }
                } else { // For D-
                    if (Config.costCriteria[i/3]) { // cost value for price criterion
                        dValue += distance.compute(fuzzyValues, Config.idealSolution) * (1.0/3.0);
                    } else {
                        dValue += distance.compute(fuzzyValues, Config.antiIdealSolution) * (1.0/3.0);
                    }
                }
            }

            results.put(entry.getKey(), dValue);
            System.out.print(ideal? "D+": "D-");
            System.out.println(" for " + entry.getKey() + " is: " + Config.df.format(dValue));
        }

        return results;
    }

    private TreeMap<String, Double> calculateRelativeCloseness(HashMap<String, Double> dPlusList, HashMap<String, Double> dMinusList) {

        TreeMap<String, Double> cStar = new TreeMap<>();

        for (Map.Entry<String,Double> entry: dPlusList.entrySet()) {
            // c* = d- / (d- + d+)
            cStar.put(entry.getKey(), dMinusList.get(entry.getKey()) / ( dMinusList.get(entry.getKey()) + entry.getValue()));
        }

        System.out.println("closeness coefficient set is: " + cStar);

        return cStar;
    }
}
