import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HydrothermalAnalyzer {
    private final ArrayList<Line> lines = new ArrayList<>(500);

    public HydrothermalAnalyzer(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            lines.add(new Line(scanner.nextLine()));
        }

        scanner.close();
    }

    private ArrayList<Line> getGridLines() {
        return lines.stream()
                .filter(Line::isGridLine)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int countGridOverlaps() {
        Board board = new Board();
        for (Line line : getGridLines()) {
            board.setLine(line);
        }

        return board.count(entry -> entry.getValue() >= 2);
    }

    public int countOverlaps() {
        Board board = new Board();
        for (Line line : lines) {
            board.setLine(line);
        }

        return board.count(entry -> entry.getValue() >=2);
    }
}

class Line {
    Coordinate start;
    Coordinate end;

    @Override
    public String toString() {
        return "Line from " + start + " to " + end;
    }

    public Line(String input) {
        String[] coords = input.split(" -> ");
        start = new Coordinate(coords[0]);
        end = new Coordinate(coords[1]);
    }

    public boolean isHorizontal() { return start.y == end.y; }
    public boolean isVertical() { return start.x == end.x; }
    public boolean isGridLine() { return isHorizontal() || isVertical(); }

    public int lengthX() { return Math.abs(start.x - end.x) + 1; }
    public int lengthY() { return Math.abs(start.y - end.y) + 1; }
    public int length() { return Math.max(lengthX(), lengthY()); }

    public int directionX() { return isVertical() ? 0 : start.x > end.x ? -1 : 1;}
    public int directionY() { return isHorizontal() ? 0 : start.y > end.y ? -1 : 1;}
}

class Coordinate {
    int x;
    int y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Coordinate(String input) {
        String[] coords = input.split(",");
        x = Integer.parseInt(coords[0]);
        y = Integer.parseInt(coords[1]);
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

class Board {
    private final HashMap<Coordinate, Integer> board = new HashMap<>();

    public int get(Coordinate coord) {
        return board.getOrDefault(coord, 0);
    }

    public int get(int x, int y) {
        return get(new Coordinate(x, y));
    }

    public void setLine(Line line) {
        for (int i = 0; i < line.length(); i++) {
            var coord = new Coordinate(
                    line.start.x + i * line.directionX(),
                    line.start.y + i * line.directionY()
            );
            board.put(coord, get(coord) + 1);
        }
    }

    public String toString(int x, int y) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                builder.append(get(i, j));
            }
            builder.append('\n');
        }

        return builder.toString();
    }

    public int count(Predicate<? super Map.Entry<Coordinate, Integer>> predicate) {
        return (int) board.entrySet().stream().filter(predicate).count();
    }
}