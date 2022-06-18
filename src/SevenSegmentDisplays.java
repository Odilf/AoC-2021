import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SevenSegmentDisplays {
    final ArrayList<Entry> entries = new ArrayList<>();

    public SevenSegmentDisplays(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            entries.add(new Entry(scanner.nextLine()));
        }

        scanner.close();
    }

    public int countEasy() {
        int count = 0;
        for (Entry entry : entries) {
            for (String string : entry.output) {
                if (Arrays.stream(simples).anyMatch(l -> l == string.length())) {
                    count += 1;
                }
            }
        }

        return count;
    }

    public int count() {
        int sum = 0;
        for (Entry entry : entries) {
            sum += entry.getOutput();
        }

        return sum;
    }

    static final int[] simples = {2, 4, 3, 7};
}

class Entry {
    final String[] signalPatterns;
    final String[] output;

    public Entry(String string) {
        String[] splits = string.split(" \\| ");
        signalPatterns = splits[0].split(" ");
        output = splits[1].split(" ");
    }

    public int getOutput() {
        var mapping = getMapping();

        return getDigitOutput(output[0], mapping) * 1000 +
                getDigitOutput(output[1], mapping) * 100 +
                getDigitOutput(output[2], mapping) * 10 +
                getDigitOutput(output[3], mapping);
    }

    private int getDigitOutput(String digit, HashMap<Character, Character> mapping) {
        if (digit.length() == 2) { return 1; }
        if (digit.length() == 4) { return 4; }
        if (digit.length() == 7) { return 8; }
        if (digit.length() == 3) { return 7; }

        if (digit.length() == 6) {
            HashSet<Integer> possibilities = new HashSet<>(){{
                add(0);
                add(6);
                add(9);
            }};

            for (char c : digit.toCharArray()) {
                if (mapping.get(c) == 'D') { possibilities.remove(0); }
                if (mapping.get(c) == 'C') { possibilities.remove(6); }
                if (mapping.get(c) == 'E') { possibilities.remove(9); }
            }

            return (int) possibilities.toArray()[0];
        }

        if (digit.length() == 5) {
            for (char c : digit.toCharArray()) {
                if (mapping.get(c) == 'B') { return 5; }
                if (mapping.get(c) == 'E') { return 2; }
            }

            return 3;
        }

        throw new RuntimeException("What");
    }

    private HashMap<Character, Integer> getFrequencies() {
        HashMap<Character, Integer> frequencies = new HashMap<>();

        // Get frequencies
        for (String signal : signalPatterns) {
            for (char c : signal.toCharArray()) {
                frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
            }
        }

        return frequencies;
    }

    private HashMap<Character, Character> getMapping() {
        HashMap<Character, Character> mapping = new HashMap<>();
        ArrayList<Character> sevens = new ArrayList<>();
        ArrayList<Character> eights = new ArrayList<>();

        var frequencies = getFrequencies();

        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            char dest = switch (entry.getValue()) {
                case 8 -> '8';
                case 7 -> '7';

                case 6 -> 'B';
                case 4 -> 'E';
                case 9 -> 'F';
                default -> 'X';
            };

            if (dest == '8') {
                eights.add(entry.getKey());
            } else if (dest == '7') {
                sevens.add(entry.getKey());
            } else {
                mapping.put(entry.getKey(), dest);
            }
        }

        // Handle A vs C
        for (String signal : signalPatterns) {
            if (signal.length() == 2) {
                if (signal.contains(String.valueOf(eights.get(0)))) {
                    mapping.put(eights.get(0), 'C');
                    mapping.put(eights.get(1), 'A');
                } else {
                    mapping.put(eights.get(0), 'A');
                    mapping.put(eights.get(1), 'C');
                }
            }
        }

        // Handle D vs G
        for (String signal : signalPatterns) {
            if (signal.length() == 4) {
                if (signal.contains(String.valueOf(sevens.get(0)))) {
                    mapping.put(sevens.get(0), 'D');
                    mapping.put(sevens.get(1), 'G');
                } else {
                    mapping.put(sevens.get(0), 'G');
                    mapping.put(sevens.get(1), 'D');
                }
            }
        }

        return mapping;
    }
}