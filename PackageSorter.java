public class PackageSorter {

    public enum Stack {
        STANDARD,
        SPECIAL,
        REJECTED;

        @Override
        public String toString() {
            return name();
        }
    }

    private static final double BULKY_VOLUME_THRESHOLD = 1_000_000.0;
    private static final double BULKY_DIMENSION_THRESHOLD = 150.0;
    private static final double HEAVY_MASS_THRESHOLD = 20.0;

    public static String sort(double width, double height, double length, double mass) {
        if (width < 0 || height < 0 || length < 0 || mass < 0) {
            throw new IllegalArgumentException("Dimensions and mass must be non-negative");
        }

        boolean isBulky = isBulky(width, height, length);
        boolean isHeavy = isHeavy(mass);

        if (isHeavy && isBulky) return Stack.REJECTED.toString();
        if (isHeavy || isBulky) return Stack.SPECIAL.toString();

        return Stack.STANDARD.toString();
    }

    private static boolean isBulky(double width, double height, double length) {
        if (width >= BULKY_DIMENSION_THRESHOLD || 
            height >= BULKY_DIMENSION_THRESHOLD || 
            length >= BULKY_DIMENSION_THRESHOLD) {
            return true;
        }

        double volume = width * height * length;
        
        return volume >= BULKY_VOLUME_THRESHOLD;
    }

    private static boolean isHeavy(double mass) {
        return mass >= HEAVY_MASS_THRESHOLD;
    }

    public static void main(String[] args) {
        System.out.println("=== Package Sorting Test Cases ===\n");

        testCase("Test 1: Small, light package", 
                 10, 10, 10, 5, "STANDARD");

        testCase("Test 2: Bulky by dimension (width >= 150)", 
                 150, 50, 50, 10, "SPECIAL");

        testCase("Test 3: Bulky by volume (>= 1,000,000 cm³)", 
                 100, 100, 100, 10, "SPECIAL");

        testCase("Test 4: Heavy only (mass >= 20 kg)", 
                 50, 50, 50, 20, "SPECIAL");

        testCase("Test 5: REJECTED - Heavy and bulky by dimension", 
                 150, 50, 50, 20, "REJECTED");

        testCase("Test 6: REJECTED - Heavy and bulky by volume", 
                 100, 100, 100, 20, "REJECTED");

        testCase("Test 7: Exactly at volume threshold", 
                 100, 100, 100, 19, "SPECIAL");

        testCase("Test 8: Exactly at mass threshold", 
                 50, 50, 50, 20, "SPECIAL");

        testCase("Test 9: Exactly at dimension threshold", 
                 150, 40, 40, 19, "SPECIAL");

        testCase("Test 10: REJECTED - All thresholds met", 
                 150, 100, 100, 20, "REJECTED");

        testCase("Test 11: Large volume from many small dimensions", 
                 100, 100, 100.1, 19, "SPECIAL");

        testCase("Test 12: Zero volume, zero mass", 
                 0, 0, 0, 0, "STANDARD");
    }

    private static void testCase(String description, double width, double height, 
                                 double length, double mass, String expected) {
        String result = sort(width, height, length, mass);
        String status = result.equals(expected) ? "PASS" : "FAIL";
        System.out.printf("%s: %s\n", status, description);
        System.out.printf("Input: width=%.2f, height=%.2f, length=%.2f, mass=%.2f\n", 
                         width, height, length, mass);
        System.out.printf("Expected: %s, Got: %s\n\n", expected, result);
    }
}

