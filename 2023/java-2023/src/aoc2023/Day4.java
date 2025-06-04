package aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Day4 {

    public static void main(String[] args) throws Exception {
//        final String path = "2023/inputs/example4.txt";
        final String path = "2023/inputs/input4.txt";

        List<String> lines = Files.readAllLines(Path.of(path));
        var result1 = lines.stream()
                .mapToInt(Day4::countMatches)
                .map(Day4::calculateScore)
                .sum();

        int[] copies = new int[lines.size()];
        Arrays.fill(copies, 1);

        for (int i = 0; i < lines.size(); i++) {
            int matches = countMatches(lines.get(i));

            for (int j = i + 1; j <= Math.min(i + matches, copies.length - 1); j++) {
                copies[j] += copies[i];
            }
        }

        int result2 = Arrays.stream(copies).sum();

        System.out.println(result1);
        System.out.println(result2);
    }

    private static int countMatches(String s) {
        var split0 = s.split(": ");
        var split1 = split0[1].split(" +\\| +");

        var winning = new HashSet<>(Arrays.asList(split1[0].split(" +")));
        var numbers = new ArrayList<>(Arrays.asList(split1[1].split(" +")));
        numbers.retainAll(winning);

        return numbers.size();
    }

    private static int calculateScore(int matches) {
        return matches == 0 ? 0 : 1 << (matches - 1);
    }

}
