import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Day01 {

    public static void main(String[] args) {
        try {
            // Read input from the input dir/file
            String input = Files.readString(Paths.get("input/day01"));

            // Call the functions for every line and calculate the sum using Stream API
            int sum1 = Arrays.stream(input.split("\n"))
                    .mapToInt(Day01::part1CalibrationValue)
                    .sum();

            int sum2 = Arrays.stream(input.split("\n"))
                    .mapToInt(Day01::part2CalibrationValue)
                    .sum();

            System.out.println("Part1 answer: " + sum1);
            System.out.println("Part2 answer: " + sum2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int part1CalibrationValue(String line) {
        char firstChar = line
                .chars()
                .filter(Character::isDigit)
                .findFirst()
                .orElseThrow();

        char lastChar = new StringBuilder(line)
                .reverse()
                .chars()
                .filter(Character::isDigit)
                .findFirst()
                .orElseThrow();

        return Integer.parseInt("" + firstChar + lastChar);
    }

    private static int part2CalibrationValue(String line) {
        line = line.replace("one", "o1e")
                .replace("two", "t2o")
                .replace("three", "t3e")
                .replace("four", "f4r")
                .replace("five", "f5e")
                .replace("six", "s6x")
                .replace("seven", "s7n")
                .replace("eight", "e8t")
                .replace("nine", "n9e");

        // Fix cases where string/number intertwine
        // Normalize the input
        return part1CalibrationValue(line); // Call function from part1
    }
}
