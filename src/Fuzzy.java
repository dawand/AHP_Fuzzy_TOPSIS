public enum Fuzzy {
    // A triangular fuzzy number A is a fuzzy number with piecewise linear membership function
    // performance ratings and weights are evaluated with linguistic terms
    // The linguistic weights for presenting the importance of criteria are very low (VL), low (L), medium (M), high (H) and very high (VH)
    VERY_LOW(new double[] {0.0, 0.0, 0.25}),
    LOW(new double[] {0.0, 0.25, 0.50}),
    GOOD(new double[] {0.25, 0.50, 0.75}),
    HIGH(new double[] {0.50, 0.75, 1.0}),
    VERY_HIGH(new double[] {0.75, 1.0, 1.0});

    private double[] value;

    public double[] getValue() {
            return value;
        }

        Fuzzy(double[] value) {
            this.value = value;
        }
}
