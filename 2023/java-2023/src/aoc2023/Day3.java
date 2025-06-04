package aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Day3 {

    record XY(int x, int y) {
    }

    public static void main(String[] args) throws Exception {
//        final String path = "2023/inputs/example3.txt";
        final String path = "2023/inputs/input3.txt";

        var partNos = new ArrayList<String>();
        var gears = new HashMap<XY, List<String>>();

        var lines = Files.readAllLines(Path.of(path));
        for (int j = 0; j < lines.size(); j++) {
            var line = lines.get(j);
            for (int i = 0; i < line.length(); i++) {
                if (Character.isDigit(line.charAt(i))) {
                    var number = readNumber(line, i);
                    if (isPartNo(i, number.length(), j, lines)) {
                        partNos.add(number);
                    }

                    var gear = isGear(i, number.length(), j, lines);
                    if (gear.isPresent()) {
                        var xy = gear.get();
                        gears.computeIfAbsent(xy, k -> new ArrayList<>())
                                .add(number);
                    }

                    i += number.length() - 1;
                }
            }
        }

        int result1 = partNos.stream()
                .mapToInt(Integer::parseInt)
                .sum();

        int result2 = gears.values().stream()
                .filter(n -> n.size() == 2)
                .mapToInt(g -> Integer.parseInt(g.get(0)) * Integer.parseInt(g.get(1)))
                .sum();


        System.out.println(result1);
        System.out.println(result2);
    }

    private static boolean isPartNo(int x, int length, int y, List<String> lines) {
        int i0 = Math.max(0, x - 1);
        int i1 = Math.min(lines.get(0).length() - 1, x + length);
        int j0 = Math.max(0, y - 1);
        int j1 = Math.min(lines.size() - 1, y + 1);

        for (int j = j0; j <= j1; j++) {
            for (int i = i0; i <= i1; i++) {
                var ch = lines.get(j).charAt(i);
                if (ch != '.' && !Character.isDigit(ch)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static Optional<XY> isGear(int x, int length, int y, List<String> lines) {
        int i0 = Math.max(0, x - 1);
        int i1 = Math.min(lines.get(0).length() - 1, x + length);
        int j0 = Math.max(0, y - 1);
        int j1 = Math.min(lines.size() - 1, y + 1);

        for (int j = j0; j <= j1; j++) {
            for (int i = i0; i <= i1; i++) {
                var ch = lines.get(j).charAt(i);
                if (ch == '*') {
                    return Optional.of(new XY(i, j));
                }
            }
        }

        return Optional.empty();
    }

    private static String readNumber(String line, int i) {
        StringBuilder sb = new StringBuilder();
        while (line.length() > i && Character.isDigit(line.charAt(i))) {
            sb.append(line.charAt(i));
            i++;
        }
        return sb.toString();
    }

}
