package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day9 {

    public static void main(String[] args) throws IOException {

//        final String path = "2023/inputs/example9.txt";
        final String path = "2023/inputs/input9.txt";

        final List<String> lines = Files.readAllLines(Path.of(path));
        var result1 = lines.stream()
                .map(s -> s.split(" +"))
                .map(s -> Arrays.stream(s).mapToInt(Integer::parseInt).toArray())
                .mapToInt(Day9::extrapolate)
                .sum();

        var result2 = lines.stream()
                .map(s -> s.split(" +"))
                .map(s -> Arrays.stream(s).mapToInt(Integer::parseInt).toArray())
                .map(Day9::reverse)
                .mapToInt(Day9::extrapolate)
                .sum();

        System.out.println(result1);
        System.out.println(result2);
    }

    private static int[] reverse(int[] vals) {
        var ret = new int[vals.length];
        for (int i = 0, j = vals.length - 1; i < vals.length; i++, j--) {
            ret[j] = vals[i];
        }
        return ret;
    }

    private static int extrapolate(int[] vals) {

        var steps = new ArrayList<int[]>();
        steps.add(vals);

        //    01234
        // 5: ABCDE
        // 0: xx
        // 1:  xx
        // 2:   xx
        // 3:    xx

        while (isNonZero(vals)) {
            var diffs = new int[vals.length - 1];
            for (int i = 0; i < diffs.length; i++) {
                diffs[i] = vals[i + 1] - vals[i];
            }
            steps.add(diffs);
            vals = diffs;
        }

        int last = 0;
        Collections.reverse(steps);
        for (var step : steps) {
            var next = step[step.length - 1] + last;
            System.out.println(Arrays.toString(step) + " " + next);
            last = next;
        }
        System.out.println("==========");

        return last;
    }

    private static boolean isNonZero(int[] vals) {
        for (int v : vals) {
            if (v != 0) {
                return true;
            }
        }
        return false;
    }

}

