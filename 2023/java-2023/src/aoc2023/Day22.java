package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day22 {

    record Brick(int ix, int x0, int y0, int z0, int x1, int y1, int z1) {

        int dx() {
            return x1 - x0;
        }

        int dy() {
            return y1 - y0;
        }

        int dz() {
            return z1 - z0;
        }

        public Brick drop(int drop) {
            return new Brick(ix, x0, y0, z0 - drop, x1, y1, z1 - drop);
        }
    }

    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example22.txt";
        final String path = "2023/inputs/input22.txt";

        int[] count = new int[1];
        List<Brick> bricks = Files.lines(Path.of(path))
                .map(line -> {
                    var a = Arrays.stream(line.split("[,~]"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    return new Brick(++count[0], a[0], a[1], a[2], a[3], a[4], a[5]);
                })
                .sorted(Comparator.comparingInt(o -> o.z0))
                .toList();

        int xmax = 0, ymax = 0, zmax = 0;
        for (var b : bricks) {
            if (b.x0 > b.x1 || b.y0 > b.y1 || b.z0 > b.z1) {
                System.err.println(b);
            }
            xmax = Math.max(xmax, b.x1 + 1);
            ymax = Math.max(ymax, b.y1 + 1);
            zmax = Math.max(zmax, b.z1 + 1);
        }

        System.out.println("dim: " + xmax + "," + ymax + "," + zmax);

        Map<Integer, Set<Integer>> brickSupports = new HashMap<>();

        List<Brick> restingBricks = new ArrayList<>();
        var grid = new int[zmax][ymax][xmax];
        for (var b : bricks) {
            int drop = 0;
            boolean collision = false;
            while (b.z0 - drop > 0) {
                int n = Math.max(b.dx(), b.dy());
                for (int i = 0; i <= n; i++) {
                    int x = b.x0 + i * (b.dx() == 0 ? 0 : 1);
                    int y = b.y0 + i * (b.dy() == 0 ? 0 : 1);
                    int z = b.z0 - drop - 1;
                    if (grid[z][y][x] != 0) {
                        collision = true;
                        brickSupports.computeIfAbsent(b.ix, foo -> new HashSet<>())
                                .add(grid[z][y][x]);
                    }
                }
                if (collision) {
                    break;
                }
                drop++;
            }

            Brick b1 = b.drop(drop);
            restingBricks.add(b1);
//            System.out.println(b1);

            int n = Math.max(Math.max(b.dx(), b.dy()), b.dz());
            for (int i = 0; i <= n; i++) {
                int x = b1.x0 + i * (b1.dx() == 0 ? 0 : 1);
                int y = b1.y0 + i * (b1.dy() == 0 ? 0 : 1);
                int z = b1.z0 + i * (b1.dz() == 0 ? 0 : 1);
                grid[z][y][x] = b1.ix;
            }
        }

        System.out.println(brickSupports);

        Set<Integer> unremovable = new HashSet<>();
        for (Set<Integer> supports : brickSupports.values()) {
            if (supports.size() == 1) {
                unremovable.addAll(supports);
            }
        }

        System.out.println(bricks.size() - unremovable.size());

        Map<Integer,Set<Integer>> supported = new HashMap<>();
        for (var b : restingBricks) {
            supported.put(b.ix, new HashSet<>());
        }
        for (var e : brickSupports.entrySet()) {
            for (var b : e.getValue()) {
                supported.get(b).add(e.getKey());
            }
        }
        System.out.println(supported);

        int sum = 0;

        for (var base : restingBricks) {
            var falling = new LinkedHashSet<Integer>();
            falling.add(base.ix);
            var queue = new ArrayDeque<Integer>();
            queue.add(base.ix);

            while (!queue.isEmpty()) {
                var b0 = queue.remove();
                for (var b : supported.get(b0)) {
                    if (falling.containsAll(brickSupports.getOrDefault(b, Collections.emptySet()))) {
                        if (falling.add(b)) {
                            queue.add(b);
                        }
                    }
                }
            }

//            System.out.println(base.ix + " " + falling);
            sum += falling.size() - 1;
        }


        System.out.println(sum);
    }
}
