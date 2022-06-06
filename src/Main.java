import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var HA = new HydrothermalAnalyzer("src/ventLines.txt");

        System.out.println(HA.countOverlaps());
    }
}