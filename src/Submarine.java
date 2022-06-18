import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Submarine {
    int depth = 0;
    int position = 0;
    int aim = 0;

    public Submarine(String inputPath, boolean aim) throws FileNotFoundException {
        File file = new File(inputPath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            var command = new Command(scanner.nextLine());
            this.apply(command, aim);
        }
    }

    void apply(Command command, boolean aim) {
        if (aim) {
            this.aim += command.deltaY;
            this.position += command.deltaX;
            this.depth += this.aim * command.deltaX;
        } else {
            this.position += command.deltaX;
            this.depth += command.deltaY;
        }
    }
}

class Command {
    int deltaX = 0;
    int deltaY = 0;

    public Command(String input) {
        Scanner scanner = new Scanner(input);

        String keyword = scanner.next();
        int amount = scanner.nextInt();

        switch (keyword.toLowerCase(Locale.ROOT)) {
            case "forward" -> this.deltaX = 1;
            case "down" -> this.deltaY = 1;
            case "up" -> this.deltaY = -1;
        }

        this.deltaX *= amount;
        this.deltaY *= amount;

        scanner.close();
    }
}