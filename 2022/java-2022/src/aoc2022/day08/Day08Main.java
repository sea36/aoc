package aoc2022.day08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

public class Day08Main {

    public static void main(String[] args) throws IOException {
//        final String input = "2022/inputs/test08.txt";
        final String input = "2022/inputs/input08.txt";


        final List<String> lines = Files.lines(Paths.get(input)).collect(Collectors.toList());
        int rows = lines.size();
        int cols = lines.get(0).length();

        BitSet[] visible = new BitSet[rows];
        int[][] heights = new int[rows][cols];
        for (int j = 0; j < rows; j++) {
            visible[j] = new BitSet();
            visible[j].set(0);
            visible[j].set(cols - 1);
            for (int i = 0; i < cols; i++) {
                heights[j][i] = Integer.parseInt(lines.get(j).substring(i, i + 1));
            }
        }
        visible[0].set(0, cols);
        visible[rows - 1].set(0, cols);

        for (int j = 1; j < rows - 1; j++) {
            int max = heights[j][0];
            for (int i = 1; i < cols - 1; i++) {
                int h = heights[j][i];
                if (h > max) {
                    visible[j].set(i);
                }
                max = Math.max(max, h);
            }

            max = heights[j][cols - 1];
            for (int i = cols - 2; i > 0; i--) {
                int h = heights[j][i];
                if (h > max) {
                    visible[j].set(i);
                }
                max = Math.max(max, h);
            }
        }

        for (int i = 1; i < cols - 1; i++) {
            int max = heights[0][i];
            for (int j = 1; j < rows - 1; j++) {
                int h = heights[j][i];
                if (h > max) {
                    visible[j].set(i);
                }
                max = Math.max(max, h);
            }

            max = heights[rows-1][i];
            for (int j = rows - 2; j > 0; j--) {
                int h = heights[j][i];
                if (h > max) {
                    visible[j].set(i);
                }
                max = Math.max(max, h);
            }
        }

//        for (BitSet b : visible) {
//            System.out.println(b);
//        }

        System.out.println(Arrays.stream(visible).mapToInt(BitSet::cardinality).sum());


        int maxScore = 0;

        for (int j = 1; j < rows - 2; j++) {
            for (int i = 1; i < cols - 2; i++) {
                if (visible[j].get(i)) {
                    int score = calcScore(i, j, heights, rows, cols);
                    maxScore = Math.max(score, maxScore);
                }
            }
        }

        System.out.println(maxScore);
    }

    private static int calcScore(int x, int y, int[][] heights, int rows, int cols) {
        int west = x, east = x;
        int north = y, south = y;

        int h = heights[y][x];

        do {
            west--;
        } while (heights[y][west] < h && west > 0);
        do {
            east++;
        } while (heights[y][east] < h && east < cols - 1);

        do {
            north--;
        } while (heights[north][x] < h && north > 0);
        do {
            south++;
        } while (heights[south][x] < h && south < rows - 1);

        return (x - west) * (east - x) * (y - north) * (south - y);
    }
}