import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day02 {

    public static void main(String[] args) {
        try {
            // Read input from the input dir/file
            String input = Files.readString(Paths.get("input/day02.txt"));

            // Create a list of games from input
            List<Game> games = input.lines().map(Game::from).collect(Collectors.toList());

            // Part 1: Calculate the sum of ids for games that are possible with certain values
            int part1 = games.stream()
                    .filter(g -> g.possible(12, 13, 14))
                    .mapToInt(g -> g.getId())
                    .sum();

            // Part 2: Calculate the sum of powers for all games
            int part2 = games.stream()
                    .mapToInt(Game::power)
                    .sum();

            // Print the results
            System.out.println("Part1: " + part1);
            System.out.println("Part2: " + part2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Game {
    private final int id;
    private int maxReds;
    private int maxGreens;
    private int maxBlues;

    public Game(int id, int maxReds, int maxGreens, int maxBlues) {
        this.id = id;
        this.maxReds = maxReds;
        this.maxGreens = maxGreens;
        this.maxBlues = maxBlues;
    }

    public boolean possible(int reds, int greens, int blues) {
        return maxReds <= reds && maxGreens <= greens && maxBlues <= blues;
    }

    public int power() {
        return maxReds * maxGreens * maxBlues;
    }

    public int getId() {
        return id;
    }

    public static Game from(String line) {
        String[] parts = line.split(":", 2);
        String meta = parts[0];
        String sets = parts[1];

        String[] metaParts = meta.split(" ", 2);
        int id = Integer.parseInt(metaParts[1].trim());

        Game game = new Game(id, 0, 0, 0);

        String[] setArray = sets.trim().split(";");
        for (String set : setArray) {
            String[] drawArray = set.trim().split(",");
            for (String draw : drawArray) {
                String[] drawParts = draw.trim().split(" ", 2);
                int number = Integer.parseInt(drawParts[0]);
                String color = drawParts[1].trim();
                switch (color) {
                    case "red":
                        game.maxReds = Math.max(number, game.maxReds);
                        break;
                    case "green":
                        game.maxGreens = Math.max(number, game.maxGreens);
                        break;
                    case "blue":
                        game.maxBlues = Math.max(number, game.maxBlues);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + color);
                }
            }
        }

        return game;
    }
}
