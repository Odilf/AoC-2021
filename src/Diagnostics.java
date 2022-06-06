import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Diagnostics {
    ArrayList<String> input = new ArrayList<>();

    public Diagnostics(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            this.input.add(scanner.nextLine());
        }

        scanner.close();
    }

    public int getPower() {
        return getGammaRate() * getEpsilonRate();
    }

    public int getLifeSupportRating() throws Exception {
        return getRating(RatingType.OXYGEN_GENERATOR) * getRating(RatingType.CO2_SCRUBBER);
    }

    private int getGammaRate() {
        StringBuilder gammaRate = new StringBuilder();
        for (int i = 0; i < input.get(0).length(); i++) {
            gammaRate.append(getMostCommon(i, this.input));
        }

        return parseBinary(gammaRate.toString());
    }

    private int getEpsilonRate() {
        StringBuilder epsilonRate = new StringBuilder();
        for (int i = 0; i < input.get(0).length(); i++) {
            epsilonRate.append(getMostCommon(i, this.input) == 1 ? 0 : 1);
        }

        return parseBinary(epsilonRate.toString());
    }

    private int getRating(RatingType type) throws Exception {
        var values = new ArrayList<>(this.input);

        for (int i = 0; i < values.get(0).length(); i++) {
            int bit = getMostCommon(i, values);

            if (type == RatingType.CO2_SCRUBBER) {
                bit = bit == 1 ? 0 : 1;
            }

            System.out.println("Bit: " + String.valueOf(bit).charAt(0) + ", size: " + values.size() + ", " + values);

            for (int j = values.size() - 1; j >= 0; j--) {
                String value = values.get(j);
                if (value.charAt(i) != String.valueOf(bit).charAt(0)) {
                    values.remove(j);
                }
            }

            if (values.size() == 1) {
                System.out.println("\n\n");
                return parseBinary(values.get(0));
            }
        }

        throw new Exception("Unreachable. ArrayList seems not to be getting filtered");
    }

    private int getMostCommon(int index, ArrayList<String> input) {
        boolean is0 = input.stream().filter(x -> x.charAt(index) == '0').count() > input.size() / 2;

        return is0 ? 0 : 1;
    }

    private int parseBinary(String binary) {
        return Integer.parseInt(binary, 2);
    }
}

enum RatingType {
    OXYGEN_GENERATOR, CO2_SCRUBBER
}
