import java.util.ArrayList;
import java.util.TreeMap;

class Profiler {

    private TreeMap<String, ArrayList<Fuzzy>> availableSites = new TreeMap<>();

    TreeMap<String, ArrayList<Fuzzy>> start(){
        for (String alternative : Config.alternatives) {
            ArrayList<Fuzzy> criteriaImportance = profileNode(alternative);
            availableSites.put(alternative, criteriaImportance);
        }

        return availableSites;
    }

    private ArrayList<Fuzzy> profileNode(String node) {

        ArrayList<Fuzzy> siteCriteria = new ArrayList<>();

//        // Mobile node
//        if (node.equalsIgnoreCase(Config.alternatives[0])) {
//            siteCriteria.add(Config.MOBILE_BANDWIDTH);
//            siteCriteria.add(Config.MOBILE_SPEED);
//            siteCriteria.add(Config.MOBILE_AVAILABILITY);
//            siteCriteria.add(Config.MOBILE_SECURITY);
//            siteCriteria.add(Config.MOBILE_PRICE);
//        } else if (node.equalsIgnoreCase(Config.alternatives[1])) { // Edge
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

        //Mobile-1
        if (node.equalsIgnoreCase(Config.alternatives[0])) {
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.GOOD);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_LOW);
        }

        //Mobile-2
        else if (node.equalsIgnoreCase(Config.alternatives[1])) {
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_LOW);
        }

        //Mobile-3
        else if (node.equalsIgnoreCase(Config.alternatives[2])) {
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_LOW);
        }

        //Edge-1
        else if (node.equalsIgnoreCase(Config.alternatives[3])) {
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.GOOD);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.LOW);
        }

        //Edge-2
        else if (node.equalsIgnoreCase(Config.alternatives[4])) {
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.GOOD);
        }

        //Edge-3
        else if (node.equalsIgnoreCase(Config.alternatives[5])) {
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
        }

        //Public-1
        else if (node.equalsIgnoreCase(Config.alternatives[6])) {
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.GOOD);
        }

        //Public-2
        else if (node.equalsIgnoreCase(Config.alternatives[7])) {
            siteCriteria.add(Fuzzy.GOOD);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.HIGH);
        }

        //Public-3
        else if (node.equalsIgnoreCase(Config.alternatives[8])) {
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.VERY_HIGH);
        }

        return siteCriteria;
    }
}
