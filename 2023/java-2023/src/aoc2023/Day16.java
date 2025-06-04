package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class Day16 {

    enum Direction {
        UP(0, -1, 1),
        DOWN(0, 1, 2),
        LEFT(-1, 0, 4),
        RIGHT(1, 0, 8);

        final int x, y, mask;

        Direction(int x, int y, int mask) {
            this.x = x;
            this.y = y;
            this.mask = mask;
        }
    }

    record Beam(int x, int y, Direction dir) {}

    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example16.txt";
        final String path = "2023/inputs/input16.txt";

        var grid = Files.readAllLines(Path.of(path)).toArray(String[]::new);
        var ymax = grid.length;
        var xmax = grid[0].length();


        var result1 = testBeam(0, 0, Direction.RIGHT, grid);

        var maxLit = 0L;
        for (int i = 0; i < xmax; i++) {
            maxLit = Math.max(maxLit, testBeam(0, i, Direction.DOWN, grid));
            maxLit = Math.max(maxLit, testBeam(ymax - 1, i, Direction.UP, grid));
        }
        for (int i = 0; i < ymax; i++) {
            maxLit = Math.max(maxLit, testBeam(i, 0, Direction.RIGHT, grid));
            maxLit = Math.max(maxLit, testBeam(i, xmax - 1, Direction.LEFT, grid));
        }

        System.out.println(result1);
        System.out.println(maxLit);

    }

    private static long testBeam(int y0, int x0, Direction dir0, String[] grid) {
        var beams = new ArrayList<Beam>();
        beams.add(new Beam(x0,y0, dir0));

        var ymax = grid.length;
        var xmax = grid[0].length();

        var lit = new int[ymax][xmax];

        while (!beams.isEmpty()) {
            var beam = beams.remove(0);
            int x = beam.x;
            int y = beam.y;
            Direction dir = beam.dir;

            while (x >= 0 && y >= 0 && x < xmax && y < ymax) {

                // detect cycles
                if ((lit[y][x] & dir.mask) != 0) {
                    break;
                }
                lit[y][x] |= dir.mask;

                var ch = grid[y].charAt(x);
                switch (ch) {
                    case '.':
                        break;

                    case '/':
                        dir = reflectForward(dir);
                        break;

                    case '\\':
                        dir = refectBackward(dir);
                        break;

                    case '|':
                        if (dir == Direction.LEFT || dir == Direction.RIGHT) {
                            dir = Direction.UP;
                            beams.add(new Beam(x, y + 1, Direction.DOWN));
                        }
                        break;

                    case '-':
                        if (dir == Direction.UP || dir == Direction.DOWN) {
                            dir = Direction.LEFT;
                            beams.add(new Beam(x + 1, y, Direction.RIGHT));
                        }
                }

                x += dir.x;
                y += dir.y;
            }

        }

//        debug(ymax, xmax, lit);

        return Arrays.stream(lit)
                .flatMapToInt(Arrays::stream)
                .filter(i -> i != 0)
                .count();
    }

    private static void debug(int ymax, int xmax, int[][] lit) {
        for (int j = 0; j < ymax; j++) {
            for (int i = 0; i < xmax; i++) {
                System.out.print(lit[j][i] == 0 ? '.' : '#');
            }
            System.out.println();
        }
        System.out.println();
    }

    private static Direction reflectForward(Direction dir) {
        //  '/'
        return switch (dir) {
            case UP -> Direction.RIGHT;
            case DOWN -> Direction.LEFT;
            case LEFT -> Direction.DOWN;
            case RIGHT -> Direction.UP;
        };
    }

    private static Direction refectBackward(Direction dir) {
        // '\`
        return switch (dir) {
            case UP -> Direction.LEFT;
            case DOWN -> Direction.RIGHT;
            case LEFT -> Direction.UP;
            case RIGHT -> Direction.DOWN;
        };
    }
}
