package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day21 {

    enum Dir {
        N(0, -1),
        S(0, 1),
        E(1, 0),
        W(-1, 0);

        final int x,y;

        Dir(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    record Xy(int x, int y){}

    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example21.txt";
        final String path = "2023/inputs/input21.txt";

        int steps = 26501365;

        var rows = Files.readAllLines(Path.of(path)).toArray(String[]::new);

        part2(rows, steps);

//        part2b(rows, steps);

    }

    private static void part2b(String[] rows, int steps) {
        var start = findStart(rows);
        int ymax = rows.length;
        int xmax = rows[0].length();


        long[][] frontier = new long[ymax][xmax];
        long[][] visited = new long[ymax][xmax];
        frontier[start.y][start.x] = 1;

        for (int i = 1; i <= steps; i++) {
            for (int y = 0; y < ymax; y++) {
                for (int x = 0; x < xmax; x++) {



                }
            }
        }

//        /*


        long[][] counts = new long[ymax][xmax];
        for (int y = 0; y < ymax; y++) {
            for (int x = 0; x < xmax; x++) {
                char c = rows[y].charAt(x);
                if (c == 'S') {
                    counts[y][x] = 1;
                } else if (c == '#') {
                    counts[y][x] = -1;
                }
            }
        }


        for (int i = 1; i <= steps; i++) {
            long[][] next = new long[ymax][xmax];

            for (int y = 0; y < ymax; y++) {
                for (int x = 0; x < xmax; x++) {

                    if (counts[y][x] == -1) {
                        next[y][x] = -1;
                    } else {
                        next[y][x] = Math.max(0, counts[y][(x + 1) % xmax])
                                + Math.max(0, counts[y][(x - 1 + xmax) % xmax])
                                + Math.max(0, counts[(y + 1) % ymax][x])
                                + Math.max(0, counts[(y - 1 + ymax) % ymax][x]);
                    }
                }
            }

            counts = next;

        }

        var sum = Arrays.stream(counts).flatMapToLong(Arrays::stream).filter(i -> i >= 0).sum();
//        */
    }

    private static void part1(String[] rows, int steps) {
        var start = findStart(rows);
        int ymax = rows.length;
        int xmax = rows[0].length();

        var visited = new HashSet<Xy>();
        var frontier = new HashSet<Xy>();
        frontier.add(start);

        int[] counts = new int[steps];

        for (int i = 0; i < steps; i++) {
            var newFrontier = new HashSet<Xy>();
            for (var p : frontier) {
                for (var d : Dir.values()) {
                    int x = p.x + d.x;
                    int y = p.y + d.y;

                    if (x >= 0 && y >= 0 && x < xmax && y < xmax && rows[y].charAt(x) != '#') {
                        var q = new Xy(x, y);
                        if (visited.add(q)) {
                            newFrontier.add(q);
                        }
                    }
                }
            }
            counts[i] = newFrontier.size() + (i > 1 ? counts[i-2] : 0);
            frontier = newFrontier;
        }

        System.out.println(counts[steps -1]);
    }

    private static void part2(String[] rows, int targetSteps) {

        long t0 = System.nanoTime();

        var start = findStart(rows);
        int ymax = rows.length;
        int xmax = rows[0].length();

        var visited = new HashSet<Xy>();
        var frontier = new HashSet<Xy>();
        frontier.add(start);

        long[] counts = new long[3];

        int[] frontiers = new int[xmax];
        int[] d1 = new int[xmax];
        int[] d2 = new int[xmax];

        int step = 0;
        while (true) {

            var newFrontier = new HashSet<Xy>();
            for (var p : frontier) {
                for (var d : Dir.values()) {
                    int x = p.x + d.x;
                    int y = p.y + d.y;
                    int x1 = (x % xmax);
                    int y1 = (y % ymax);
                    x1 = x1 >= 0 ? x1 : x1 + xmax;
                    y1 = y1 >= 0 ? y1 : y1 + ymax;

                    if (rows[y1].charAt(x1) != '#') {
                        var q = new Xy(x, y);
                        if (visited.add(q)) {
                            newFrontier.add(q);
                        }
                    }
                }
            }

            int fsize = newFrontier.size();
            counts[2] = fsize + counts[0];
            counts[0] = counts[1];
            counts[1] = counts[2];

            int ix = step % xmax;
            if (step >= xmax) {
                int dx = fsize - frontiers[ix];
                d2[ix] = dx - d1[ix];
                d1[ix] = dx;
            }
            frontiers[ix] = fsize;

            frontier = newFrontier;
            step++;

            if (step >= 2*xmax) {
                if (Arrays.stream(d2).allMatch(i -> i == 0)) {
                    break;
                }
            }
        }

        System.out.println("step: " + step);
        System.out.println(counts[2]);
        System.out.println(Arrays.toString(frontiers));
        System.out.println(Arrays.toString(d1));
        System.out.println(Arrays.toString(d2));

        // extrapolate
        for (int i = step; i < targetSteps; i++) {
            int ix = i % xmax;
            d1[ix] += d2[ix];
            frontiers[ix] += d1[ix];

            counts[2] = counts[0] + frontiers[ix];
            counts[0] = counts[1];
            counts[1] = counts[2];
        }

        System.out.println(counts[2]);

        long t1 = System.nanoTime();

        System.out.println((t1-t0) / 1000000);
    }

    private static Xy findStart(String[] rows) {
        for (int y = 0; y < rows.length; y++) {
            var row = rows[y];
            for (int x = 0; x < row.length(); x++) {
                if (row.charAt(x) == 'S') {
                    return new Xy(x, y);
                }
            }
        }
        throw new RuntimeException();
    }
}
