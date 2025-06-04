package aoc2022.day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class Day09Main {

    record Position(int x, int y) {

        Position move(Direction dir) {
            return new Position(x + dir.x, y + dir.y);
        }

        public Position follow(Position o) {
            int dx = this.x - o.x;
            int dy = this.y - o.y;

            // overlap or adjacent
            if (abs(dx) <= 1 && abs(dy) <= 1) {
                return this;
            }
            // same row
            if (dy == 0) {
                return new Position(x - (int) signum(dx), y);
            }
            // same column
            if (dx == 0) {
                return new Position(x, y - (int) signum(dy));
            }

            return new Position(x - (int) signum(dx), y - (int) signum(dy));
        }
    }

    enum Direction {
        RIGHT(1, 0),
        LEFT(-1, 0),
        UP(0, 1),
        DOWN(0, -1);

        private final int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws IOException {
//        final String input = "2022/inputs/test09.txt";
        final String input = "2022/inputs/input09.txt";


        final Rope rope = new Rope(9);
        Files.lines(Paths.get(input))
                .forEach(s -> {
                    Direction dir = getDirection(s);
                    int steps = getStepCount(s);
                    rope.moveHead(dir, steps);
                });

        System.out.println(rope.tailVisited.size());

    }

    private static Direction getDirection(String s) {
        return switch (s.charAt(0)) {
            case 'L' -> Direction.LEFT;
            case 'R' -> Direction.RIGHT;
            case 'U' -> Direction.UP;
            case 'D' -> Direction.DOWN;
            default -> throw new IllegalStateException("Unexpected value: " + s.charAt(0));
        };
    }

    private static int getStepCount(String s) {
        return Integer.parseInt(s.substring(2));
    }

    static class Rope {

        Position[] knots;

        Set<Position> tailVisited = new HashSet<>();

        public Rope(int n) {
            knots = new Position[n + 1];
            Position start = new Position(0, 0);
            Arrays.fill(knots, start);
            tailVisited.add(start);
        }

        public void moveHead(Direction dir, int steps) {
            for (int i = 0; i < steps; i++) {
                knots[0] = knots[0].move(dir);

                for (int j = 1; j < knots.length; j++) {
                    knots[j] = knots[j].follow(knots[j - 1]);
                }

                tailVisited.add(knots[knots.length - 1]);
            }
        }
    }
}