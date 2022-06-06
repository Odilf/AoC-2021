import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class SonarSweeper {
    int[] depths;

    public SonarSweeper(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        ArrayList<Integer> input = new ArrayList<>(1000);

        while (scanner.hasNextInt()) {
            input.add(scanner.nextInt());
        }

        depths = input.stream().mapToInt(Integer::intValue).toArray();

        scanner.close();
    }

    public int countIncreases() {
        return countIncreases(1);
    }

    public int countIncreases(int clumpLength) {
        ArrayList<Integer> clumped = new ArrayList<>();

        for (int i = clumpLength - 1; i < depths.length; i++) {
            int clumpValue = 0;
            for (int j = 0; j < clumpLength; j++) {
                clumpValue += depths[i - j];
            }
            clumped.add(clumpValue);
        }

        int count = 0;
        for (int i = 1; i < clumped.size(); i++) {
            if (clumped.get(i) > clumped.get(i - 1)) {
                count += 1;
            }
        }
        return count;
    }
}
