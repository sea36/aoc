package aoc2022.day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day12Main {

    record Position(int x, int y) {
    }

    public static void main(String[] args) throws IOException {
//        final var input = "2022/inputs/test12.txt";
        final var input = "2022/inputs/input12.txt";


        var landscape = Files.lines(Paths.get(input)).collect(Collectors.toList());
        Position start = findStart(landscape);

        int minX = 0, minY = 0;
        int maxX = landscape.get(0).length() - 1;
        int maxY = landscape.size() - 1;

        // BFS
        int steps = 0;
        var visited = new HashSet<>();
        var frontier = Arrays.asList(start);
        out:
        while (!frontier.isEmpty()) {

            steps++;

            var newFrontier = new ArrayList<Position>();
            for (Position pos : frontier) {
                if (!visited.add(pos)) {
                    continue;
                }

                var current = landscape.get(pos.y).charAt(pos.x);

                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if (dx == 0 && dy == 0) continue;
                        if (dx != 0 && dy != 0) continue;
                        int x = pos.x + dx;
                        int y = pos.y + dy;
                        if (x < minX || y < minY || x > maxX || y > maxY) continue;

                        var next = landscape.get(y).charAt(x);
                        if (next == 'E' && (current == 'y' || current == 'z')) {
                            System.out.println("***");
                            break out;
                        }
                        if (current == 'S' || next <= (current + 1)) {
                            newFrontier.add(new Position(x, y));
                        }
                    }
                }

            }

            frontier = newFrontier;
        }

        System.out.println(steps);

        int shortestPath = findShortestPath(landscape);
        System.out.println(shortestPath);

    }

    private static int findShortestPath(List<String> landscape) {

        int minX = 0, minY = 0;
        int maxX = landscape.get(0).length() - 1;
        int maxY = landscape.size() - 1;

        var frontier = new ArrayList<Position>();

        int[][] dists = new int[maxY+1][maxX+1];
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                char c = landscape.get(y).charAt(x);
                dists[y][x] = (c == 'a' || c == 'S') ? 0 : Integer.MAX_VALUE;

                if (c == 'a' || c == 'S') {
                    frontier.add(new Position(x, y));
                }
            }
        }

        int step = 0;
        while (!frontier.isEmpty()) {
            step++;

            var newFrontier = new ArrayList<Position>();
            for (Position pos : frontier) {

                var current = landscape.get(pos.y).charAt(pos.x);

                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if (dx == 0 && dy == 0) continue;
                        if (dx != 0 && dy != 0) continue;
                        int x = pos.x + dx;
                        int y = pos.y + dy;
                        if (x < minX || y < minY || x > maxX || y > maxY) continue;

                        var next = landscape.get(y).charAt(x);
                        if (next == 'E' && (current == 'y' || current == 'z')) {
                            System.out.println("***");
                            return step;
                        }
                        if ((current == 'S' || next <= (current + 1)) && (dists[y][x] > step)) {
                            newFrontier.add(new Position(x, y));
                            dists[y][x] = step;
                        }
                    }
                }

            }

            frontier = newFrontier;


        }
        return 0;
    }

    private static Position findStart(List<String> landscape) {
        for (int y = 0; y < landscape.size(); y++) {
            int x = landscape.get(y).indexOf('S');
            if (x != -1) {
                return new Position(x, y);
            }
        }
        throw new IllegalArgumentException();
    }

}