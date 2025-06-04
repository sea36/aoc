package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class Day17 {

    static final int MIN_STEPS = 4;
    static final int MAX_STEPS = 10;

    enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        final int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    record Move(int x, int y, Direction dir, int steps, int cost) implements Comparable<Move> {
        @Override
        public int compareTo(Move o) {
            if (this.cost != o.cost) {
                return this.cost < o.cost ? -1 : 1;
            }
            return (o.x + o.y) - (this.x + this.y);
        }
    }

    private final int[][] grid;
    private final int[][][] costs;
    private final int ymax;
    private final int xmax;
    private final Queue<Move> queue;

    public Day17(int[][] grid) {
        this.grid = grid;
        this.ymax = grid.length;
        this.xmax = grid[0].length;

        costs = new int[ymax][xmax][4 * MAX_STEPS];
        for (var aa : costs) {
            for (var a : aa) {
                Arrays.fill(a, Integer.MAX_VALUE);
            }
        }

        queue = new PriorityQueue<>();
    }

    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example17.txt";
        final String path = "2023/inputs/input17.txt";

        int[][] grid = Files.readAllLines(Path.of(path)).stream().map(s -> s.chars().map(c -> c - '0').toArray()).toArray(int[][]::new);

        for (int[] row : grid) {
            for (int i : row) {
                System.out.print(i);
            }
            System.out.println();
        }

        Day17 search = new Day17(grid);
        search.trystep(0, 0, Direction.RIGHT, 0);
        search.trystep(0, 0, Direction.DOWN, 0);
        search.search();

        var result = Arrays.stream(search.costs[search.ymax - 1][search.xmax - 1]).min().orElse(-1);
        System.out.println(result);
    }

    private void search() {
        long n = 0;
        while (!queue.isEmpty()) {
            Move next = queue.remove();

//            System.out.println(n + "\t" + next);
            visit(next.x, next.y, next.dir, next.steps, next.cost);
            n++;
        }
        System.out.println(n);
    }

    private void visit(int x, int y, Direction dir, int steps, int cost) {

        if (steps < MAX_STEPS) {
            trystep(x, y, dir, steps + 1, cost);
        }

        for (Direction d : Direction.values()) {
            if (dir == d || (dir.x == -d.x && dir.y == -d.y)) {
                continue;
            }

            trystep(x, y, d, cost);
        }
    }

    private void trystep(int x0, int y0, Direction d, int cost) {
        int x = x0 + MIN_STEPS * d.x;
        int y = y0 + MIN_STEPS * d.y;

        if (x >= 0 && x < xmax && y >= 0 && y < ymax) {

            int newCost = cost;
            for (int i = 1; i <= MIN_STEPS; i++) {
                int x1 = x0 + i * d.x;
                int y1 = y0 + i * d.y;
                newCost += grid[y1][x1];
            }

            enqueue(x, y, d, MIN_STEPS, newCost);
        }
    }

    private void trystep(int x0, int y0, Direction d, int steps, int cost) {
        int x = x0 + d.x;
        int y = y0 + d.y;

        if (x >= 0 && x < xmax && y >= 0 && y < ymax) {
            int newCost = cost + grid[y][x];

            enqueue(x, y, d, steps, newCost);
        }
    }

    private void enqueue(int x, int y, Direction d, int steps, int newCost) {
        int z = (MAX_STEPS * d.ordinal()) + steps - 1;
        if (newCost < costs[y][x][z]) {
            costs[y][x][z] = newCost;
            queue.add(new Move(x, y, d, steps, newCost));
        }
    }
}
