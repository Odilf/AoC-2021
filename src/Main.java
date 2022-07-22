import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var syntax = new SyntaxScorer("src/syntax.txt");
        var score = syntax.getAutocompleteScore();
        System.out.println(score);
    }
}