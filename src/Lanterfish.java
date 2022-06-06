import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;

public class Lanterfish {
    private final HashMap<Integer, BigInteger> ages = new HashMap<>();

    private BigInteger get(int age) {
        return ages.getOrDefault(age, BigInteger.valueOf(0));
    }

    private void add(int age) {
        ages.put(age, get(age).add(BigInteger.valueOf(1)));
    }

    public Lanterfish(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        var ages = scanner.nextLine().split(",");

        for (String s : ages) {
            int age = Integer.parseInt(s);
            add(age);
        }

        scanner.close();
    }

    public Lanterfish(int[] ages) {
        for (int age : ages) {
            add(age);
        }
    }

    public void passDay() {
        BigInteger toSpawn = get(0);

        for (int i = 0; i < 8; i++) {
            ages.put(i, get(i + 1));
        }

        ages.put(8, toSpawn);
        ages.put(6, get(6).add(toSpawn));
    }

    public void passDays(int n) {
        for (int i = 0; i < n; i++) {
            passDay();
        }
    }

    public BigInteger count() {
        BigInteger count = BigInteger.valueOf(0);
        for (int i = 0; i < 9; i++) {
            count = count.add(get(i));
        }

        System.out.println(ages);
        return count;
    }

    @Override
    public String toString() {
        return ages.toString();
    }
}
