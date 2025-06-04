package aoc2022.day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day14Main {

    record Point(int x, int y) {}

    record Dimensions(int xmin, int ymin, int xmax, int ymax) {
        public int width() {
            return xmax - xmin + 1;
        }

        public int height() {
            return ymax - ymin + 1;
        }
    }

    public static void main(String[] args) throws IOException {

//        final var input = "2022/inputs/test14.txt";
        final var input = "2022/inputs/input14.txt";


        var lines = Files.lines(Paths.get(input)).collect(Collectors.toList());
        var dim = getDimensions(lines);
        var width = dim.width();
        var height = dim.height();

        char[][] grid = new char[height][width];
        for (char[] row : grid) {
            Arrays.fill(row, ' ');
        }

        for (var line : lines) {
            addLine(grid, line, dim);
        }
        Arrays.fill(grid[grid.length-1], '#');

        dump(grid);

        int n = 0;
        while (addSand(grid, dim)) {
            n++;
        }

        dump(grid);

        System.out.println(n);

    }

    private static boolean addSand(char[][] grid, Dimensions dim) {
        if (grid[0][500 - dim.xmin] != ' ') {
            return false;
        }

        var p = new Point(500, 0);
        while (true) {
            if (p.y + 1 > dim.ymax || p.x - 1 < dim.xmin || p.x + 1 > dim.xmax) {
                return false;
            }

            if (grid[p.y + 1][p.x - dim.xmin] == ' ') {
                p = new Point(p.x, p.y +1);
            }
            else if (grid[p.y + 1][p.x - 1 - dim.xmin] == ' ') {
                p = new Point(p.x -1, p.y + 1);
            }
            else if (grid[p.y + 1][p.x + 1 - dim.xmin] == ' ') {
                p = new Point(p.x + 1, p.y + 1);
            }
            else {
                grid[p.y][p.x - dim.xmin] = 'o';
                return true;
            }
        }
    }

    private static void dump(char[][] grid) {
        System.out.println("====================");
        for (var row : grid) {
            System.out.println(new String(row));
        }
    }

    private static void addLine(char[][] grid, String line, Dimensions dim) {
        var points = Arrays.stream(line.split(" -> ")).map(Day14Main::point).toList();
        Point p0 = null;
        for (var p : points) {
            if (p0 != null) {
                addLine(grid, p0, p, dim);
            }
            p0 = p;
        }
    }

    private static void addLine(char[][] grid, Point p0, Point p1, Dimensions dim) {
        if (p0.x == p1.x) {
            for (int y = Math.min(p0.y, p1.y); y <= Math.max(p0.y, p1.y); y++) {
                grid[y - dim.ymin][p0.x - dim.xmin] = '#';
            }
        }
        else if (p0.y == p1.y) {
            for (int x = Math.min(p0.x, p1.x); x <= Math.max(p0.x, p1.x); x++) {
                grid[p0.y - dim.ymin][x - dim.xmin] = '#';
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    private static Dimensions getDimensions(List<String> lines) {
        int minx = Integer.MAX_VALUE, miny = 0;
        int maxx = 0, maxy = 0;

        for (var line : lines) {
            for (var pos : line.split(" -> ")) {
                var p = point(pos);

                minx = Math.min(minx, p.x);
                maxx = Math.max(maxx, p.x);
                maxy = Math.max(maxy, p.y);
            }
        }

        var height = maxy + 2;
        minx = Math.min(minx, 500 - height - 1);
        maxx = Math.max(maxx, 500 + height + 1);
        maxy = height;

        return new Dimensions(minx, miny, maxx, maxy);
    }

    private static Point point(String p) {
        var xy = p.split(",");
        return new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
    }
}