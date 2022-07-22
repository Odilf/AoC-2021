import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class SyntaxScorer {
    ArrayList<Chunk> chunks = new ArrayList<>();
    public SyntaxScorer(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            chunks.add(new Chunk(scanner.nextLine()));
        }
        scanner.close();
    }

    public int getCorruptedScore() {
        int score = 0;
        for (Chunk chunk : chunks) {
            char c = chunk.getCorruptedChar();
            score += scoreTable.get(c);
        }

        return score;
    }

    public BigInteger getAutocompleteScore() {
        ArrayList<BigInteger> scores = new ArrayList<>();
        for (Chunk chunk : chunks) {
            if (chunk.getCorruptedChar() != 'X') { continue; }
            var chars = chunk.autocomplete();

            BigInteger score = BigInteger.valueOf(0);
            for (char c : chars) {
                score = score.multiply(BigInteger.valueOf(5));
                score = score.add(BigInteger.valueOf( switch (c) {
                    case ')' -> 1;
                    case ']' -> 2;
                    case '}' -> 3;
                    case '>' -> 4;
                    default -> throw new RuntimeException("Unexpected value");
                }));
            }

            scores.add(score);
        }

        scores.sort(BigInteger::compareTo);
        System.out.println(scores);
        return scores.get(Math.floorDiv(scores.size(), 2));
    }

    static final private HashMap<Character, Integer> scoreTable = new HashMap<>(){{
        put(')', 3);
        put(']', 57);
        put('}', 1197);
        put('>', 25137);
        put('X', 0);
    }};
}

class Chunk {
    private static final HashMap<Character, Character> delimiters = new HashMap<>(4){{
        put('(', ')');
        put('[', ']');
        put('{', '}');
        put('<', '>');
    }};

    private final String value;
    public Chunk(String line) {
        value = line;
    }

    public char getCorruptedChar() {
        ArrayDeque<Character> expectingClosers = new ArrayDeque<>();

        for (char c : value.toCharArray()) {
            if (isOpener(c)) {
                expectingClosers.push(getCloser(c));
            }

            if (isCloser(c)) {
                if (c != expectingClosers.pop()) {
                    return c;
                }
            }
        }

        return 'X';
    }

    private static boolean isOpener(char c) {
        return c == '(' || c == '[' || c == '{' || c == '<';
    }

    private static boolean isCloser(char c) {
        return c == ')' || c == ']' || c == '}' || c == '>';
    }

    private static char getCloser(char c) {
        return delimiters.get(c);
    }

    public ArrayList<Character> autocomplete() {
        ArrayDeque<Character> expectingClosers = new ArrayDeque<>();

        for (char c : value.toCharArray()) {
            if (isOpener(c)) {
                expectingClosers.push(getCloser(c));
            }

            if (isCloser(c)) {
                expectingClosers.pop();
            }
        }

        return new ArrayList(expectingClosers);
    }

}
