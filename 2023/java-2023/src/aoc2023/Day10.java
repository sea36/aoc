package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class Day10 {

    record XY(int x, int y) {
        public XY add(XY o) {
            return new XY(this.x + o.x, this.y + o.y);
        }
    }

    public static void main(String[] args) throws IOException {

//        final String path = "2023/inputs/example10.txt";
//        final String path = "2023/inputs/example10b.txt";
        final String path = "2023/inputs/input10.txt";

        final String[] lines = Files.readAllLines(Path.of(path)).toArray(String[]::new);
        final XY start = findStart(lines);
        final XY first = findFirst(start, lines);

        final char[][] loop = new char[lines.length][lines[0].length()];
        for (char[] row : loop) {
            Arrays.fill(row, '.');
        }
        loop[start.y][start.x] = 'F';

        int steps = 1;
        var posn = first;
        var last = start;
        while (!posn.equals(start)) {
            loop[posn.y][posn.x] = lines[posn.y].charAt(posn.x);
            var next = findNext(posn, last, lines);
            last = posn;
            posn = next;
            steps++;
        }

        System.out.println(steps + " " + steps / 2);

        flood1(loop);


        var inside = 0;
        for (var row : loop) {
            System.out.println(new String(row));
            for (var c : row) {
                if (c == '.') {
                    inside++;
                }
            }
        }

        System.out.println(inside);
    }

    private static void flood1(char[][] l) {

        int yn = l.length;
        int xn = l[0].length;
        char[][] loop = new char[yn * 2][xn * 2];
        for (var row : loop) {
            Arrays.fill(row, '.');
        }
        for (int j = 0; j < yn; j++) {
            for (int i = 0; i < xn; i++) {
                char ch = l[j][i];
                loop[2 * j][2 * i] = ch;
                if (ch == '-' || ch == 'F') {
                    loop[2 * j][2 * i + 1] = '-';
                }
                if (ch == '-' || ch == 'F' || ch == 'L') {
                    loop[2 * j][2 * i + 1] = '-';
                }
                if (ch == '|' || ch == 'F' || ch == '7') {
                    loop[2 * j + 1][2 * i] = '|';
                }
            }
        }


        var xmax = loop[0].length - 1;
        var ymax = loop.length - 1;

        var frontier = new ArrayList<XY>();
        frontier.add(new XY(0, 0));
        loop[0][0] = 'O';
        while (!frontier.isEmpty()) {
            var next = new ArrayList<XY>();
            for (var p : frontier) {
                if (p.x > 0 && loop[p.y][p.x - 1] == '.') {
                    loop[p.y][p.x - 1] = 'O';
                    next.add(new XY(p.x - 1, p.y));
                }
                if (p.x < xmax && loop[p.y][p.x + 1] == '.') {
                    loop[p.y][p.x + 1] = 'O';
                    next.add(new XY(p.x + 1, p.y));
                }
                if (p.y > 0 && loop[p.y - 1][p.x] == '.') {
                    loop[p.y - 1][p.x] = 'O';
                    next.add(new XY(p.x, p.y - 1));
                }
                if (p.y < ymax && loop[p.y + 1][p.x] == '.') {
                    loop[p.y + 1][p.x] = 'O';
                    next.add(new XY(p.x, p.y + 1));
                }
            }
            frontier = next;
        }

        for (int j = 0; j < yn; j++) {
            for (int i = 0; i < xn; i++) {
                l[j][i] = loop[2 * j][2 * i];
            }
        }
    }

    private static void flood2(char[][] loop) {
        var xmax = loop[0].length;

        int inside = 0;

        for (var row : loop) {
            var outside = true;
            for (int i = 0; i < xmax; i++) {
                var pipe = row[i];
                if (pipe == '.') {
                    row[i] = outside ? 'O' : 'I';
                    if (!outside) {
                        inside++;
                    }
                }
                if (pipe == '|' || pipe == 'L' || pipe == 'J') {
                    outside = !outside;
                }
            }
        }


        System.out.println(inside);
    }


    private static XY findFirst(XY p, String[] lines) {
        char w = lines[p.y].charAt(p.x - 1);
        char e = lines[p.y].charAt(p.x + 1);
        char n = lines[p.y - 1].charAt(p.x);
        char s = lines[p.y + 1].charAt(p.x);

        if (n == '|' || n == 'F' || n == '7') {
            return new XY(p.x, p.y - 1);
        }
        if (s == '|' || s == 'L' || s == 'J') {
            return new XY(p.x, p.y + 1);
        }
        if (w == '-' || w == 'L' || w == 'F') {
            return new XY(p.x - 1, p.y);
        }
        if (e == '-' || e == 'J' || e == '7') {
            return new XY(p.x + 1, p.y);
        }
        throw new RuntimeException();
    }

    private static XY findNext(XY p, XY last, String[] lines) {

        char pipe = lines[p.y].charAt(p.x);
        XY[] exits = getExits(pipe);

        for (var exit : exits) {
            var next = p.add(exit);
            if (!next.equals(last)) {
                return next;
            }
        }

        throw new RuntimeException();
    }

    private static XY[] getExits(char pipe) {
        return switch (pipe) {
            case '|' -> new XY[]{new XY(0, 1), new XY(0, -1)};
            case '-' -> new XY[]{new XY(1, 0), new XY(-1, 0)};
            case 'F' -> new XY[]{new XY(1, 0), new XY(0, 1)};
            case 'L' -> new XY[]{new XY(1, 0), new XY(0, -1)};
            case 'J' -> new XY[]{new XY(-1, 0), new XY(0, -1)};
            case '7' -> new XY[]{new XY(-1, 0), new XY(0, 1)};
            default -> throw new RuntimeException();
        };


    }

    private static XY findStart(String[] lines) {
        for (int j = 0; j < lines.length; j++) {
            for (int i = 0; i < lines[j].length(); i++) {
                if (lines[j].charAt(i) == 'S') {
                    return new XY(i, j);
                }
            }
        }
        throw new RuntimeException();
    }

}
