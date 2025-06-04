package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.OptionalInt;

public class Day13 {

    /*

#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

     */
    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example13.txt";
        final String path = "2023/inputs/input13.txt";

        var input = Files.readString(Path.of(path));
        var landscapes = input.split("\n\n");

        var result = 0;
        for (var map : landscapes) {
            var rows = map.split("\n");
            OptionalInt vert = findVerticalReflection(rows);
            OptionalInt horiz = findHorizontalReflection(rows);
            if (vert.isEmpty() == horiz.isEmpty()) {
                for (var r : rows) {
                    System.out.println(r);
                }
                System.out.println("v:" + vert);
                System.out.println("h:" + horiz);
                System.out.println();
                System.exit(1);
            }
            result += vert.orElse(0) + 100* horiz.orElse(0);
        }

        System.out.println(result);

    }

    private static OptionalInt findVerticalReflection(String[] rows) {
        int xmax = rows[0].length();
        for (int i = 1; i < xmax; i++) {
            if (isVerticalSmudge(rows, i)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    private static OptionalInt findHorizontalReflection(String[] rows) {
        var xmax = rows.length;
        for (int i = 1; i < xmax; i++) {
            if (isHorizontalSmudge(rows, i)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    private static boolean isHorizontalReflection(String[] rows, int n) {
        var ymax = rows.length;
        for (int i0 = n-1, i1 = n; i0 >= 0 && i1 < ymax; i0--, i1++) {
            boolean b = !rows[i0].equals(rows[i1]);
            if (b) {
                return false;
            }
        }
        return true;
    }

    private static boolean isVerticalReflection(String[] rows, int n) {
        var xmax = rows[0].length();
        for (int i0 = n-1, i1 = n; i0 >= 0 && i1 < xmax; i0--, i1++) {
            for (int j = 0; j < rows.length; j++) {
                if (rows[j].charAt(i0) != rows[j].charAt(i1)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isHorizontalSmudge(String[] rows, int n) {
        var xmax = rows[0].length();
        int ymax = rows.length;
        var flaws = 0;
        for (int i0 = n-1, i1 = n; i0 >= 0 && i1 < ymax; i0--, i1++) {
            for (int j = 0; j < xmax; j++) {
                if (rows[i0].charAt(j) != rows[i1].charAt(j)) {
                    flaws++;
                }
            }
        }
        return flaws == 1;
    }

    private static boolean isVerticalSmudge(String[] rows, int n) {
        var xmax = rows[0].length();
        int ymax = rows.length;
        var flaws = 0;
        for (int i0 = n-1, i1 = n; i0 >= 0 && i1 < xmax; i0--, i1++) {
            for (int j = 0; j < ymax; j++) {
                if (rows[j].charAt(i0) != rows[j].charAt(i1)) {
                    flaws++;
                }
            }
        }
        return flaws == 1;
    }

}
