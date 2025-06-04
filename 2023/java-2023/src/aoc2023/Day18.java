package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Day18 {

    record Xy(long x, long y) {
    }

    enum Direction {
        RIGHT(1, 0),
        DOWN(0, 1),
        LEFT(-1, 0),
        UP(0, -1);

        final int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    record Leg(Direction dir, int len, String rgb) {
    }

    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example18.txt";
        final String path = "2023/inputs/input18.txt";

        var legs = Files.lines(Path.of(path))
                .map(s -> s.split(" +"))
                .map(s -> {
                    Direction dir1 = parseDirection(s[0]);
                    int len1 = Integer.parseInt(s[1]);

                    Direction dir2 = Direction.values()[Integer.parseInt(s[2].substring(7, 8))];
                    int len2 = Integer.parseInt(s[2].substring(2, 7), 16);


//                    return new Leg(dir1, len1, s[2]);
                    return new Leg(dir2, len2, s[2]);
                })
                .toList();

        var points = new ArrayList<Xy>();
        var p = new Xy(0, 0);
        points.add(p);

        var len = 0L;
        for (var leg : legs) {
            long x = p.x + leg.len * leg.dir.x;
            long y = p.y + leg.len * leg.dir.y;
            p = new Xy(x, y);
            points.add(p);

            len += leg.len;
        }

        long area = 0;
        int n = points.size();
        for (var i = 0; i < n - 1; i++) {
            var last = points.get((i + n - 1) % n);
            var point = points.get(i);
            var next = points.get((i + 1) % n);

            area += (point.x * next.y) - (point.y * next.x);
//            area += (point.x+next.x) * (point.y-next.y);
        }

        System.out.println(abs(area / 2) + (len / 2) + 1);

        // 952408144115
        // 1072888051

        System.exit(0);

    }
/*
            int[] bounds = getBounds(legs);
            int xmax = bounds[2] - bounds[0] + 3;
            int ymax = bounds[3] - bounds[1] + 3;
            int x0 = -bounds[0] + 1;
            int y0 = -bounds[1] + 1;

            int[][] grid = new int[ymax][xmax];


            int x = x0;
            int y = y0;
            for (var leg : legs) {
                for (int i = 0; i < leg.len; i++) {
                    grid[y][x] = '#';
                    x += leg.dir.x;
                    y += leg.dir.y;
                }
            }

            dump(grid);

            flood(grid, 0, 0);

            dump(grid);

            long count = 0;
            for (var row : grid) {
                for (var cell : row) {
                    if (cell != -1) {
                        count++;
                    }
                }
            }

            System.out.println(count);
        }

        private static void dump ( int[][] grid){
            for (var row : grid) {
                for (var cell : row) {
                    System.out.print(cell == -1 ? '.' : (cell == 0 ? "-" : '#'));
                }
                System.out.println();
            }
            System.out.println();
        }

        private static void flood ( int[][] grid, int x0, int y0){

            int ymax = grid.length;
            int xmax = grid[0].length;

            var queue = new ArrayDeque<Xy>();
            queue.add(new Xy(x0, y0));

            while (!queue.isEmpty()) {
                var p = queue.remove();
                int lx = p.x, rx = p.x;
                int y = p.y;

                do {
                    grid[y][lx] = -1;
                    lx--;
                } while (lx >= 0 && grid[y][lx] == 0);

                do {
                    grid[y][rx] = -1;
                    rx++;
                } while (rx < xmax && grid[y][rx] == 0);

                if (y > 0) {
                    boolean inSpan = false;
                    for (int i = lx + 1; i <= rx - 1; i++) {
                        if (grid[y - 1][i] != 0) {
                            inSpan = false;
                        } else if (!inSpan) {
                            queue.add(new Xy(i, y - 1));
                            inSpan = true;
                        }
                    }
                }
                if (y < ymax - 1) {
                    boolean inSpan = false;
                    for (int i = lx + 1; i <= rx - 1; i++) {
                        if (grid[y + 1][i] != 0) {
                            inSpan = false;
                        } else if (!inSpan) {
                            queue.add(new Xy(i, y + 1));
                            inSpan = true;
                        }
                    }
                }

//            dump(grid);
//            System.out.println(queue);
//            System.exit(0);
            }

        }

        private static int[] getBounds (List < Leg > legs) {
            int x = 0;
            int y = 0;
            int xmax = 0, xmin = 0;
            int ymax = 0, ymin = 0;

            for (var leg : legs) {
                x += leg.dir.x * leg.len;
                y += leg.dir.y * leg.len;
                xmax = Math.max(xmax, x);
                ymax = Math.max(ymax, y);
                xmin = Math.min(xmin, x);
                ymin = Math.min(ymin, y);
            }

            return new int[]{xmin, ymin, xmax, ymax};
        }
*/
        private static Direction parseDirection (String s){
            return switch (s) {
                case "U" -> Direction.UP;
                case "D" -> Direction.DOWN;
                case "L" -> Direction.LEFT;
                case "R" -> Direction.RIGHT;
                default -> throw new IllegalArgumentException("Unknown: " + s);
            };
        }
    }
