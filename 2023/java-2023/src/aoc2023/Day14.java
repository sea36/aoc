package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

public class Day14 {

    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example14.txt";
        final String path = "2023/inputs/input14.txt";

        char[][] platform = Files.readAllLines(Path.of(path))
                .stream().map(String::toCharArray)
                .toArray(char[][]::new);

        var cache = new HashMap<String, String>();

        var last = toString(platform);
        for (int i = 1; i <= 1000000000; i++) {

            var next = cache.get(last);
            if (next == null) {
                tiltN(platform);
                tiltW(platform);
                tiltS(platform);
                tiltE(platform);

//            debug(platform);

                next = toString(platform);
                cache.put(last, next);


//                System.out.println(i + 1);
//                System.out.println(score(platform));
//                debug(platform);
            }

            last = next;

            if (i % 10000000 == 0) {
                System.out.println(i);
            }
        }

        debug(platform);

        platform = Arrays.stream(last.trim().split("\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        var score = score(platform);
        System.out.println(score);
    }

    private static String toString(char[][] platform) {
        var sb = new StringBuilder();
        for (var row : platform) {
            sb.append(row).append('\n');
        }
        return sb.toString();
    }

    private static void debug(char[][] platform) {
        var i = platform.length;
        for (var row : platform) {
            System.out.println(new String(row) + "  " + (i));
            i--;
        }
        System.out.println();
    }

    private static void tiltN(char[][] platform) {
        int xmax = platform[0].length;
        int ymax = platform.length;

        for (int y = 0; y < ymax; y++) {
            for (int x = 0; x < xmax; x++) {
                int j = y;
                while (j > 0 && platform[j][x] == 'O' && platform[j - 1][x] == '.') {
                    platform[j][x] = '.';
                    platform[j - 1][x] = 'O';
                    j--;
                }
            }
        }
    }

    private static void tiltS(char[][] platform) {
        int xmax = platform[0].length;
        int ymax = platform.length;

        for (int y = ymax - 1; y >= 0; y--) {
            for (int x = 0; x < xmax; x++) {
                int j = y;
                while (j < (ymax - 1) && platform[j][x] == 'O' && platform[j + 1][x] == '.') {
                    platform[j][x] = '.';
                    platform[j + 1][x] = 'O';
                    j++;
                }
            }
        }
    }

    private static void tiltW(char[][] platform) {
        int xmax = platform[0].length;
        int ymax = platform.length;

        for (int x = 0; x < xmax; x++) {
            for (int y = 0; y < ymax; y++) {
                int i = x;
                while (i > 0 && platform[y][i] == 'O' && platform[y][i - 1] == '.') {
                    platform[y][i] = '.';
                    platform[y][i - 1] = 'O';
                    i--;
                }
            }
        }
    }

    private static void tiltE(char[][] platform) {
        int xmax = platform[0].length;
        int ymax = platform.length;

        for (int x = xmax - 1; x >= 0; x--) {
            for (int y = 0; y < ymax; y++) {
                int i = x;
                while (i < (xmax - 1) && platform[y][i] == 'O' && platform[y][i + 1] == '.') {
                    platform[y][i] = '.';
                    platform[y][i + 1] = 'O';
                    i++;
                }
            }
        }
    }

    private static int score(char[][] platform) {
        int xmax = platform[0].length;
        int ymax = platform.length;

        int score = 0;
        for (int y = 0; y < ymax; y++) {
            for (int x = 0; x < xmax; x++) {
                if (platform[y][x] == 'O') {
                    score += (ymax - y);
                }
            }
        }

        return score;
    }
}
