package aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Day6 {

    public static void main(String[] args) throws Exception {
//        final String path = "2023/inputs/example6.txt";
        final String path = "2023/inputs/input6.txt";

        final List<String> lines = Files.readAllLines(Path.of(path));
        int[] times = Arrays.stream(lines.get(0).split(" +")).skip(1).mapToInt(Integer::parseInt).toArray();
        int[] dists = Arrays.stream(lines.get(1).split(" +")).skip(1).mapToInt(Integer::parseInt).toArray();

        int result = 1;
        for (int i = 0; i < times.length; i++) {
            result *= simulateRace(times[i], dists[i]);
        }

        System.out.println(result);

        long time = Long.parseLong(Arrays.stream(times).mapToObj(String::valueOf).collect(Collectors.joining()));
        long dist = Long.parseLong(Arrays.stream(dists).mapToObj(String::valueOf).collect(Collectors.joining()));

        System.out.println(simulateRace2(time, dist));


    }

    private static int simulateRace(int time, int dist) {
        int wins = 0;
        for (int i = 0; i <= time; i++) {
            int race = i * (time - i);
            if (race > dist) {
                wins++;
            }
        }
        return wins;
    }

    private static long simulateRace2(long time, long dist) {
        long wins = 0;
        for (long i = 0; i <= time; i++) {
            long race = i * (time - i);
            if (race > dist) {
                wins++;
            }
        }
        return wins;
    }

}
