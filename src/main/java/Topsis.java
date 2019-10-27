import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.*;

class Topsis {

    private TreeMap<String, ArrayList<Double>> sitesMatrix;
    private TreeMap<String, ArrayList<Fuzzy>> availableSites;

    Topsis(){
        sitesMatrix = new TreeMap<>();
        availableSites = new TreeMap<>();
    }

    void start(){

        Profiler p = new Profiler();
        availableSites = p.start();

        sitesMatrix = transformToFuzzyValues(availableSites);
//        TreeMap<String, ArrayList<Double>> normalisedSitesMatrix = calculateNormalisedFuzzyMatrix(sitesMatrix);
        TreeMap<String, ArrayList<Double>> normalisedWeightedFuzzyMatrix = calculateWeightedFuzzyMatrix(sitesMatrix);

        TreeMap<String, Double> idealDistances = calculateDistance(normalisedWeightedFuzzyMatrix, true);
        TreeMap<String, Double> antiIdealDistances = calculateDistance(normalisedWeightedFuzzyMatrix, false);

        TreeMap<Double, String> ccStar = calculateRelativeCloseness(idealDistances, antiIdealDistances);

        System.out.println("**********************************");
        System.out.println("Final Ranking:");

        for (Map.Entry<Double, String> entry: ccStar.entrySet()){
            System.out.println(entry.getValue() + ": " + Config.df.format(entry.getKey()));
        }
    }

    private TreeMap<String, ArrayList<Double>> transformToFuzzyValues(TreeMap<String, ArrayList<Fuzzy>> availableSites) {

//        System.out.println(availableSites.keySet());
//        System.out.println(availableSites.values());

        for (Map.Entry<String,ArrayList<Fuzzy>> entry: availableSites.entrySet()) {

            System.out.println(entry);

            ArrayList<Double> fuzzyMatrix = new ArrayList<>();

            for (int k = 0; k < entry.getValue().size(); k++) {
                for (double fuzzyValue:entry.getValue().get(k).getValue()) {
                    fuzzyMatrix.add(fuzzyValue);
                }
            }

            sitesMatrix.put(entry.getKey(), fuzzyMatrix);
        }

        System.out.println("unweighted fuzzy values: " + sitesMatrix);


        return sitesMatrix;
    }

    private TreeMap<String, ArrayList<Double>> calculateNormalisedFuzzyMatrix(TreeMap<String, ArrayList<Double>> sitesMatrix) {

        TreeMap<String, ArrayList<Double>> normalisedMatrix = new TreeMap<>();

        double cj = 0;
        double aj = 0;

        for (Map.Entry<String,ArrayList<Double>> entry: sitesMatrix.entrySet()) {
            for (int i = 2; i < entry.getValue().size(); i = i+3) {
                cj = Math.max(cj, entry.getValue().get(i));
            }
        }

        System.out.println("cj is: " + cj);

        for (Map.Entry<String,ArrayList<Double>> entry: sitesMatrix.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i = i+3) {
                aj = Math.min(aj, entry.getValue().get(i));
            }
        }

        System.out.println("aj is: " + aj);

        for (Map.Entry<String,ArrayList<Double>> entry: sitesMatrix.entrySet()) {

            ArrayList<Double> rij = new ArrayList<>();

            for (int i = 0; i < entry.getValue().size(); i = i+3) {

                // for price: cost criterion
                if (i == 12) {
                    // Avoid dividing zero by zero
                    if(aj == 0){
                        rij.add(0.0);
                        rij.add(0.0);
                        rij.add(0.0);
                    } else {
                        double aj_cij = aj / entry.getValue().get(i + 2);
                        double aj_bij = aj / entry.getValue().get(i + 1);
                        double aj_aij = aj / entry.getValue().get(i);

                        rij.add(aj_cij);
                        rij.add(aj_bij);
                        rij.add(aj_aij);
                    }
                }
                // For beneficial criteria
                else {
                    double aij_cj = entry.getValue().get(i) / cj;
                    double bij_cj = entry.getValue().get(i + 1) / cj;
                    double cij_cj = entry.getValue().get(i + 2) / cj;

                    rij.add(aij_cj);
                    rij.add(bij_cj);
                    rij.add(cij_cj);
                }
            }

            normalisedMatrix.put(entry.getKey(), rij);
        }

        System.out.println("normalised fuzzy values: " + normalisedMatrix);

        return normalisedMatrix;
    }


    private TreeMap<String, ArrayList<Double>> calculateWeightedFuzzyMatrix(TreeMap<String, ArrayList<Double>> sitesMatrix){

        TreeMap<String, ArrayList<Double>> normalisedWeightedFuzzyMatrix = new TreeMap<>();

        for (Map.Entry<String,ArrayList<Double>> entry: sitesMatrix.entrySet()) {

            ArrayList<Double> weightedMatrix = new ArrayList<>();

            int j = 0;
            for (int i = 0; i < entry.getValue().size(); i++) {
                weightedMatrix.add(entry.getValue().get(i) * Config.ahpWeights[j]);
                if (j%3 == 0)
                    j++;
            }
            normalisedWeightedFuzzyMatrix.put(entry.getKey(), weightedMatrix);
        }

        System.out.println("weighted fuzzy values: " + normalisedWeightedFuzzyMatrix);

//        System.out.println("weighted fuzzy values: ");
//
//        for (Map.Entry<String, ArrayList<Double>> entry: sitesMatrix.entrySet()){
//            System.out.println(entry.getKey() + ": ");
//            for (int j=0; j< entry.getValue().size(); j++) {
//                Double d = entry.getValue().get(j);
//                if (j%3 == 0) {
//                    System.out.println();
//                }
//                System.out.print(Config.df.format(d) + ", ");
//            }
//
//            System.out.println();
//        }

        return normalisedWeightedFuzzyMatrix;
    }

    private ArrayList<Fuzzy> profileNode(String node){
        ArrayList<Fuzzy> siteCriteria = new ArrayList<>();

//        // Mobile node
//        if (node.equalsIgnoreCase(Config.alternatives[0])){
//            siteCriteria.add(Config.MOBILE_BANDWIDTH);
//            siteCriteria.add(Config.MOBILE_SPEED);
//            siteCriteria.add(Config.MOBILE_AVAILABILITY);
//            siteCriteria.add(Config.MOBILE_SECURITY);
//            siteCriteria.add(Config.MOBILE_PRICE);
//        }
//        else if (node.equalsIgnoreCase(Config.alternatives[1])) { // Edge
//            siteCriteria.add(Config.EDGE_BANDWIDTH);
//            siteCriteria.add(Config.EDGE_SPEED);
//            siteCriteria.add(Config.EDGE_AVAILABILITY);
//            siteCriteria.add(Config.EDGE_SECURITY);
//            siteCriteria.add(Config.EDGE_PRICE);
//        }
//        // Public cloud instance
//        else {
//            siteCriteria.add(Config.PUBLIC_BANDWIDTH);
//            siteCriteria.add(Config.PUBLIC_SPEED);
//            siteCriteria.add(Config.PUBLIC_AVAILABILITY);
//            siteCriteria.add(Config.PUBLIC_SECURITY);
//            siteCriteria.add(Config.PUBLIC_PRICE);
//        }

        return siteCriteria;
    }

    private TreeMap<String, Double> calculateDistance(TreeMap<String, ArrayList<Double>> sitesMatrix, boolean ideal) {
        EuclideanDistance distance = new EuclideanDistance();
        // The normalized values for the ideal solution and negative ideal solution on criteria are always (1,1,1) and (0,0,0) respectively
        TreeMap<String, Double> results = new TreeMap<>();

        for (Map.Entry<String,ArrayList<Double>> entry: sitesMatrix.entrySet()) {
            double dValue = 0.0;
            for (int i = 0; i < entry.getValue().size(); i = i + 3) {
                double[] fuzzyValues = {entry.getValue().get(i), entry.getValue().get(i+1), entry.getValue().get(i+2)};
                if (ideal) { // For D+
                    if (Config.costCriteria[i/3]) { // cost value for price criterion
                        dValue += distance.compute(fuzzyValues, Config.antiIdealSolution) * (1.0/3.0);
//                        dValue += Math.sqrt((Math.pow(entry.getValue().get(i) - Config.antiIdealSolution[0], 2) +
//                                Math.pow(entry.getValue().get(i+1) - Config.antiIdealSolution[1], 2) +
//                                Math.pow(entry.getValue().get(i+2) - Config.antiIdealSolution[2], 2)) * (1.0/3.0));
                    } else {
                        dValue += distance.compute(fuzzyValues, Config.idealSolution) * (1.0/3.0);
//                        dValue += Math.sqrt((Math.pow(entry.getValue().get(i) - Config.idealSolution[0], 2) +
//                                Math.pow(entry.getValue().get(i+1) - Config.idealSolution[1], 2) +
//                                Math.pow(entry.getValue().get(i+2) - Config.idealSolution[2], 2)) * (1.0/3.0));
                    }
                } else { // For D-
                    if (Config.costCriteria[i/3]) { // cost value for price criterion
                        dValue += distance.compute(fuzzyValues, Config.idealSolution) * (1.0/3.0);
//                        dValue += Math.sqrt((Math.pow(entry.getValue().get(i) - Config.idealSolution[0], 2) +
//                                Math.pow(entry.getValue().get(i+1) - Config.idealSolution[1], 2) +
//                                Math.pow(entry.getValue().get(i+2) - Config.idealSolution[2], 2)) * (1.0/3.0));
                    } else {
                        dValue += distance.compute(fuzzyValues, Config.antiIdealSolution) * (1.0/3.0);
//                        dValue += Math.sqrt((Math.pow(entry.getValue().get(i) - Config.antiIdealSolution[0], 2) +
//                                Math.pow(entry.getValue().get(i+1) - Config.antiIdealSolution[1], 2) +
//                                Math.pow(entry.getValue().get(i+2) - Config.antiIdealSolution[2], 2)) * (1.0/3.0));
                    }
                }
            }

            results.put(entry.getKey(), dValue);
            System.out.print(ideal? "D+": "D-");
            System.out.println(" for " + entry.getKey() + " is: " + Config.df.format(dValue));
        }

        return results;
    }

    private TreeMap<Double, String> calculateRelativeCloseness(TreeMap<String, Double> dPlusList, TreeMap<String, Double> dMinusList) {

        TreeMap<Double, String> cStar = new TreeMap<>(Collections.<Double>reverseOrder());

        for (Map.Entry<String,Double> entry: dPlusList.entrySet()) {
            // c* = d- / (d- + d+)
            double c = dMinusList.get(entry.getKey()) / (dMinusList.get(entry.getKey()) + entry.getValue());
            cStar.put(c, entry.getKey());
        }

        System.out.println("closeness coefficient set is: " + cStar);

        return cStar;
    }
}
