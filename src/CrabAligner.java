import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class CrabAligner {
    private final int[] crabPositions;

    CrabAligner(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        crabPositions = Arrays.stream(scanner.nextLine()
                        .split(","))
                .mapToInt(Integer::parseInt)
                .sorted()
                .toArray();

        scanner.close();

        System.out.println(Arrays.toString(crabPositions));
    }

    private IntStream stream() {
        return Arrays.stream(crabPositions);
    }

    private int fuelToAlign(int target, boolean isConstant) {
        if (isConstant) {
            return stream().reduce(0, (acc, cur) -> Math.abs(cur - target) + acc);
        } else {
            return stream().reduce(0, (acc, cur) -> {
                int sum = 0;
                for (int i = 1; i <= Math.abs(cur - target); i++) {
                    sum += i;
                }
                return sum + acc;
            });
        }
    }

    public int minFuelToAlign(boolean isConstant) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < stream().max().getAsInt(); i++) {
            int fuel = fuelToAlign(i, isConstant);
            if (fuel < min) {
                min = fuel;
            }
        }

        return min;
    }
}
