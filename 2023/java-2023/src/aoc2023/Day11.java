package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class Day11 {

    record Galaxy(int x, int y) {

        int dist(Galaxy o) {
            return abs(o.x - x) + abs(o.y - y);
        }

    }

    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example11.txt";
        final String path = "2023/inputs/input11.txt";

//        int expansionFactor = 100;
        int expansionFactor = 1000000;

        var universe = Files.readAllLines(Path.of(path));
//        universe = expandX(universe);
//        universe = expandY(universe);

        int[] expandedRows = findExpandedRows(universe);
        int[] expandedCols = findExpandedCols(universe);

        for (var row : universe) {
            System.out.println(row);
        }

        var xmax = universe.get(0).length();
        var ymax = universe.size();

        var galaxies = new ArrayList<Galaxy>();
        for (int j = 0; j < ymax; j++) {
            for (int i = 0; i < xmax; i++) {
                if (universe.get(j).charAt(i) == '#') {
                    galaxies.add(new Galaxy(i, j));
                }
            }
        }

        var n = galaxies.size();
        long pathSum1 = 0;
        long pathSum2 = 0;
        for (int i = 0; i < n; i++) {
            var g0 = galaxies.get(i);
            for (int j = i+1; j < n; j++) {
                pathSum1 += g0.dist(galaxies.get(j));
                pathSum2 += dist(g0, galaxies.get(j), expandedRows, expandedCols, expansionFactor);
            }
        }

        System.out.println(pathSum1);
        System.out.println(pathSum2);

    }

    private static int dist(Galaxy g0, Galaxy g1, int[] expandedRows, int[] expandedCols, int expansionFactor) {
        var xmin = Math.min(g0.x, g1.x);
        var xmax = Math.max(g0.x, g1.x);
        var ymin = Math.min(g0.y, g1.y);
        var ymax = Math.max(g0.y, g1.y);

        var dist = (xmax-xmin) + (ymax - ymin);
        for (int i = xmin + 1; i < xmax; i++) {
            if (Arrays.binarySearch(expandedCols, i) >= 0) {
                dist += expansionFactor - 1;
            }
        }
        for (int i = ymin + 1; i < ymax; i++) {
            if (Arrays.binarySearch(expandedRows, i) >= 0) {
                dist += expansionFactor - 1;
            }
        }

        return dist;
    }

    private static int[] findExpandedCols(List<String> rows) {
        var ymax = rows.size();
        var xmax = rows.get(0).length();

        return IntStream.range(0, xmax)
                .filter(i -> {
                    for (int j = 0; j < ymax; j++) {
                        if (rows.get(j).charAt(i) == '#') {
                            return false;
                        }
                    }
                    return true;
                })
                .toArray();
    }

    private static int[] findExpandedRows(List<String> rows) {
        var ymax = rows.size();

        return IntStream.range(0, ymax)
                .filter(j -> !rows.get(j).contains("#"))
                .toArray();
    }

    private static List<String> expandX(List<String> rows) {
        var mutRows = rows.stream().map(StringBuilder::new).toList();
        var xmax = mutRows.get(0).length();
        var ymax = mutRows.size();

        for (int i = 0; i < xmax; i++) {
            boolean empty = true;
            for (int j = 0; j < ymax && empty; j++) {
                if (mutRows.get(j).charAt(i) == '#') {
                    empty = false;
                }
            }

            if (empty) {
                for (int j = 0; j < ymax; j++) {
                    mutRows.get(j).insert(i, '.');
                }
                i++;
                xmax++;
            }

        }
        return mutRows.stream()
                .map(StringBuilder::toString)
                .toList();
    }

    private static List<String> expandY(List<String> rows) {
        var expanded = new ArrayList<String>();
        for (String row : rows) {
            expanded.add(row);
            if (!row.contains("#")) {
                expanded.add(row);
            }
        }
        return expanded;
    }
}
