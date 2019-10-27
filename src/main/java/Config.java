import java.text.DecimalFormat;

class Config {

    // Set your alternatives here
//    static String[] alternatives = new String[]{"Mobile", "Edge", "Public"};
    static String[] alternatives = new String[]{"Mobile-1", "Mobile-2", "Mobile-3", "Edge-1", "Edge-2", "Edge-3", "Public-1", "Public-2", "Public-3"};

    // Set your criteria here
    static String[] criteria = new String[]{"Bandwidth", "Speed", "Availability", "Security", "Price"};

    // Set true for benefit criterion, false for cost criterion
    static boolean[] costCriteria = new boolean[]{false, false, false, false, true}; // price is cost

    static Double[] ahpWeights = new Double[Config.criteria.length];

    // AHP criteria weights in respect to each other
    static final Double BANDWIDTH_SPEED = 1.0;
    static final Double BANDWIDTH_AVAILABILITY = 5.0;
    static final Double BANDWIDTH_SECURITY = 7.0;
    static final Double BANDWIDTH_PRICE = 9.0;
    static final Double SPEED_AVAILABILITY = 5.0;
    static final Double SPEED_SECURITY = 6.0;
    static final Double SPEED_PRICE = 8.0;
    static final Double AVAILABLITY_SECURITY = 3.0;
    static final Double AVAIALABILITY_PRICE = 3.0;
    static final Double SECURITY_PRICE = 2.0;

    // The following values are obtained in profiling stage prior to offloading
    // Here, we just use static fuzzy values for each alternative
    static final Fuzzy MOBILE_BANDWIDTH = Fuzzy.VERY_HIGH;
    static final Fuzzy MOBILE_SPEED = Fuzzy.GOOD;
    static final Fuzzy MOBILE_AVAILABILITY = Fuzzy.HIGH;
    static final Fuzzy MOBILE_SECURITY = Fuzzy.HIGH;
    static final Fuzzy MOBILE_PRICE = Fuzzy.VERY_LOW;

    static final Fuzzy EDGE_BANDWIDTH = Fuzzy.VERY_HIGH;
    static final Fuzzy EDGE_SPEED = Fuzzy.HIGH;
    static final Fuzzy EDGE_AVAILABILITY = Fuzzy.HIGH;
    static final Fuzzy EDGE_SECURITY = Fuzzy.HIGH;
    static final Fuzzy EDGE_PRICE = Fuzzy.LOW;

    static final Fuzzy PUBLIC_BANDWIDTH = Fuzzy.LOW;
    static final Fuzzy PUBLIC_SPEED = Fuzzy.VERY_HIGH;
    static final Fuzzy PUBLIC_AVAILABILITY = Fuzzy.VERY_HIGH;
    static final Fuzzy PUBLIC_SECURITY = Fuzzy.GOOD;
    static final Fuzzy PUBLIC_PRICE = Fuzzy.VERY_HIGH;

    // These values can also be computed from max and min of weighted decision matrix
    static double[] idealSolution = {0.75, 1.0, 1.0};
    static double[] antiIdealSolution = {0.0, 0.0, 0.25};

    // Number of decimal points for float number formatting
    static DecimalFormat df = new DecimalFormat("0.0000");

}
