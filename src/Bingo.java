import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

public class Bingo {
    static final int size = 5;
    ArrayList<Board> boards = new ArrayList<>();
    ArrayList<Integer> draws;

    public Bingo(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        // Draws
        String draws = scanner.nextLine();
        this.draws = Arrays.stream(draws.split(","))
                .map(Integer::valueOf)
                .collect(toCollection(ArrayList::new));

        // Boards
        ArrayList<String> boards = new ArrayList<>();

        while (scanner.hasNextLine()) {
            scanner.nextLine();
            StringBuilder board = new StringBuilder();
            for (int i = 0; i < size; i++) {
                board.append(scanner.nextLine()).append(" ");
            }
            boards.add(board.toString());
        }

        boards.forEach(board -> this.boards.add(new Board(board)));

        scanner.close();
    }

    public Result findWinner() {
        for (int draw : draws) {
            for (Board board : boards) {
                board.cross(draw);

                if (board.hasWon()) {
                    return new Result(board, draw);
                }
            }
        }

        System.err.println("Didn't find winner!");
        return new Result(null, 0);
    }

    public Result findLoser() throws Exception {
        for (int draw : draws) {
            for (Board board : boards) {
                board.cross(draw);
            }

            if (boards.size() == 1 && boards.get(0).hasWon()) {
                return new Result(boards.get(0), draw);
            }

            boards = boards.stream()
                    .filter(board -> !board.hasWon())
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        throw new Exception("Shouldn't reach this");
    }

    record Result(Board board, int draw) {
    }

    static int getScore(Result result) {
        int sum = result.board.toStream()
                .filter(tile -> !tile.crossed)
                .map(tile -> tile.value)
                .reduce(0, Integer::sum);

        return sum * result.draw;
    }

    static class Board {
        @Override
        public String toString() {
            StringBuilder output = new StringBuilder();

            for (Tile[] row : tiles) {
                for (Tile tile : row) {
                    output.append(tile.toString()).append(" ");
                }
                output.append('\n');
            }

            return output.toString();
        }

        public Board(String input) {
            Scanner scanner = new Scanner(input);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    tiles[i][j] = new Tile(scanner.nextInt());
                }
            }

            scanner.close();
        }

        final Tile[][] tiles = new Tile[size][size];

        public void cross(int value) {
            toStream().forEach(tile -> tile.cross(value));
        }

        public Stream<Tile> toStream() {
            return Arrays.stream(tiles).flatMap(Arrays::stream);
        }

        public boolean hasWon() {
            for (int i = 0; i < size; i++) {
                // Improvable, probably.
                checkRow:
                {
                    for (int j = 0; j < size; j++) {
                        if (!tiles[i][j].crossed) {
                            break checkRow;
                        }
                    }

                    return true;
                }

                checkCol:
                {
                    for (int j = 0; j < size; j++) {
                        if (!tiles[j][i].crossed) {
                            break checkCol;
                        }
                    }

                    return true;
                }
            }

            return false;
        }


        static class Tile {
            @Override
            public String toString() {
                return value + "[" + (this.crossed ? "X" : " ") + "]";
            }

            public Tile(int value) {
                this.value = value;
            }

            final int value;
            boolean crossed = false;

            public void cross(int value) {
                if (this.value == value) {
                    crossed = true;
                }
            }
        }
    }
}