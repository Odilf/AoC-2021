import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.AnnotatedArrayType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class LavaAssessor {
    int[][] heightMap;

    public LavaAssessor(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);

        ArrayList<ArrayList<Integer>> heightMap = new ArrayList<>();
        while (scanner.hasNextLine()) {
            ArrayList<Integer> row = new ArrayList<>();
            for (char c : scanner.nextLine().toCharArray()) {
                row.add(Integer.parseInt(String.valueOf(c)));
            }

            heightMap.add(row);
        }

        this.heightMap = new int[heightMap.size()][heightMap.get(0).size()];
        for (int i = 0; i < heightMap.size(); i++) {
            for (int j = 0; j < heightMap.get(i).size(); j++) {
                this.heightMap[i][j] = heightMap.get(i).get(j);
            }
        }

        scanner.close();
    }

    private boolean isLowPoint(int i, int j) {
        int v = heightMap[i][j];
        return (i == 0 || v < heightMap[i - 1][j]) &&
                (i == heightMap.length - 1 || v < heightMap[i + 1][j]) &&
                (j == 0 || v < heightMap[i][j - 1]) &&
                (j == heightMap.length - 1 || v < heightMap[i][j + 1]);
    }

    public int riskLevel() {
        int sum = 0;
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[i].length; j++) {
                if (isLowPoint(i, j)) {
                    sum += heightMap[i][j] + 1;
                }
            }
        }

        return sum;
    }

    static final Coord[] directions = {
            new Coord(0, 1),
            new Coord(1, 0),
            new Coord(0,-1),
            new Coord(-1, 0),
    };

    private int get(Coord coord) {
        if (coord.x() < 0 || coord.x() >= heightMap.length) {
            return Integer.MAX_VALUE;
        }
        if (coord.y() < 0 || coord.y() >= heightMap[0].length) {
            return Integer.MAX_VALUE;
        }

        return heightMap[coord.x()][coord.y()];
    }

    private final ArrayList<Coord> visited = new ArrayList<>();

    private int getBasinSize(Coord position, ArrayList<Coord> caca) {
        int size = 0;
        for (Coord direction : directions) {
            Coord coord = position.add(direction);

            if (get(coord) < 9 && !visited.contains(coord)) {
                System.out.println(coord + ", " + get(coord));
                visited.add(coord);
                size += getBasinSize(coord, visited) + 1;
            }
        }

        System.out.println(size);
        return size;
    }

    public ArrayList<Integer> getBasinSizes() {
        ArrayList<Integer> basinSizes = new ArrayList<>();
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[i].length; j++) {
                if (isLowPoint(i, j)) {
                    int size = getBasinSize(new Coord(i, j), new ArrayList<>());
                    basinSizes.add(size);
                }
            }
        }

        basinSizes.sort(Integer::compare);

        return basinSizes;
    }
}

record Coord(int x, int y) {
    public Coord add(Coord coord) {
        return new Coord(x + coord.x(), y + coord.y());
    }
}
