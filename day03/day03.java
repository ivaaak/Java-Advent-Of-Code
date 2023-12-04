import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Path.of("input/day03.txt"));

            partOne(lines);
            partTwo(lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void partOne(List<String> matrix) {
        Pattern numberPattern = Pattern.compile("\\d+");
        int sum = 0;

        for (int i = 0; i < matrix.size(); i++) {
            String row = matrix.get(i);

            outer: for (Matcher matcher : getMatchers(numberPattern, row)) {
                if (i > 0 && checkAbove(matrix, i, matcher)) {
                    sum += Integer.parseInt(matcher.group());
                    continue outer;
                }

                if (i < matrix.size() - 1 && checkBelow(matrix, i, matcher)) {
                    sum += Integer.parseInt(matcher.group());
                    continue outer;
                }

                if (matcher.start() > 0 && checkLeft(row, matcher)) {
                    sum += Integer.parseInt(matcher.group());
                    continue outer;
                }

                if (matcher.end() < row.length() && checkRight(row, matcher)) {
                    sum += Integer.parseInt(matcher.group());
                    continue outer;
                }
            }
        }

        System.out.println("Part 1: " + sum);
    }

    private static void partTwo(List<String> matrix) {
        Pattern numberPattern = Pattern.compile("\\d+");
        HashMap<Pair, int[]> gears = new HashMap<>();

        for (int i = 0; i < matrix.size(); i++) {
            String row = matrix.get(i);

            for (Matcher matcher : getMatchers(numberPattern, row)) {
                if (i > 0 && checkAbove(matrix, i, matcher) && matrix.get(i - 1).charAt(matcher.start()) == '*') {
                    gears.computeIfAbsent(new Pair(i - 1, matcher.start()), k -> new int[2])[0] = Integer.parseInt(matcher.group());
                }

                if (i < matrix.size() - 1 && checkBelow(matrix, i, matcher) && matrix.get(i + 1).charAt(matcher.start()) == '*') {
                    gears.computeIfAbsent(new Pair(i + 1, matcher.start()), k -> new int[2])[0] = Integer.parseInt(matcher.group());
                }

                if (matcher.start() > 0 && row.charAt(matcher.start() - 1) == '*' ) {
                    gears.computeIfAbsent(new Pair(i, matcher.start() - 1), k -> new int[2])[0] = Integer.parseInt(matcher.group());
                }

                if (matcher.end() < row.length() && row.charAt(matcher.end()) == '*') {
                    gears.computeIfAbsent(new Pair(i, matcher.end()), k -> new int[2])[0] = Integer.parseInt(matcher.group());
                }
            }
        }

        int sum = gears.values().stream().filter(arr -> arr[0] != 0 && arr[1] != 0).mapToInt(arr -> arr[0] * arr[1]).sum();

        System.out.println("Part 2: " + sum);
    }

    private static boolean checkAbove(List<String> matrix, int i, Matcher matcher) {
        String aboveRow = matrix.get(i - 1);
        for (int j = max(matcher.start() - 1, 0); j < min(aboveRow.length(), matcher.end() + 1); j++) {
            if (aboveRow.charAt(j) != '.' && !Character.isDigit(aboveRow.charAt(j))) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkBelow(List<String> matrix, int i, Matcher matcher) {
        String belowRow = matrix.get(i + 1);
        for (int j = max(matcher.start() - 1, 0); j < min(belowRow.length(), matcher.end() + 1); j++) {
            if (belowRow.charAt(j) != '.' && !Character.isDigit(belowRow.charAt(j))) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkLeft(String row, Matcher matcher) {
        char left = row.charAt(matcher.start() - 1);
        return left != '.' && !Character.isDigit(left);
    }

    private static boolean checkRight(String row, Matcher matcher) {
        char right = row.charAt(matcher.end());
        return right != '.' && !Character.isDigit(right);
    }

    private static Iterable<Matcher> getMatchers(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        return () -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return matcher.find();
            }

            @Override
            public Matcher next() {
                return matcher;
            }
        };
    }

    static class Pair {
        int x, y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(x) ^ Integer.hashCode(y);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Pair pair = (Pair) obj;
            return x == pair.x && y == pair.y;
        }
    }
}
