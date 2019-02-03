import java.text.DecimalFormat;

class Config {

    // Set your alternatives here
    static String alternatives[] = new String[]{"Mobile", "Edge", "Public"};

    // Set your criteria here
    static String criteria[] = new String[]{"Bandwidth", "Speed", "Availability", "Security", "Price"};

    // Set true for benefir criterion, false for cost criterion
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

    // These values can also be computed from max and min of weighted decision matrix
    static double[] idealSolution = {1,1,1};
    static double[] antiIdealSolution = {0,0,0};

    // Number of decimal points for float number formatting
    static DecimalFormat df = new DecimalFormat("0.0000");

}
