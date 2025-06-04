package aoc2023;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Day8 {

    public static void main(String[] args) throws IOException {

//        final String path = "2023/inputs/example8.txt";
//        final String path = "2023/inputs/example8b.txt";
        final String path = "2023/inputs/input8.txt";

        final List<String> lines = Files.readAllLines(Path.of(path));

        var directions = lines.get(0);
        var map = new HashMap<String, String[]>();

        for (int i = 2; i < lines.size(); i++) {
            // AAA = (BBB, BBB)
            // 0123456789012345

            var node = lines.get(i).substring(0, 3);
            var left = lines.get(i).substring(7, 10);
            var right = lines.get(i).substring(12, 15);
            map.put(node, new String[]{left, right});
        }

//        int step1 = navigatePart1(map, directions);
        long step2 = navigatePart2(map, directions);

//        BigInteger tot = BigInteger.ONE;
//
//        var starts = map.keySet().stream().filter(n -> n.endsWith("A")).toList();
//        for (var start : starts) {
//            int steps = navigatePart2a(start, map, directions);
//            System.out.println(steps);
//
//            tot = tot.multiply(BigInteger.valueOf(steps));
//        }
//        System.out.println(tot);


//        System.out.println(step1);
        System.out.println(step2);


    }

    private static int navigatePart1(HashMap<String, String[]> map, String directions) {
        var current = "AAA";
        int step = 0;
        while (!current.equals("ZZZ")) {
            var exits = map.get(current);
            var dir = directions.charAt(step % directions.length());
            current = dir == 'L' ? exits[0] : exits[1];
            step++;
        }
        return step;
    }

    private static int navigatePart2a(String start, HashMap<String, String[]> map, String directions) {
        var current = start;
        int step = 0;
        while (!current.endsWith("Z")) {
            var exits = map.get(current);
            var dir = directions.charAt(step % directions.length());
            current = dir == 'L' ? exits[0] : exits[1];
            step++;
        }
        return step;
    }

    private static long navigatePart2(HashMap<String, String[]> map, String directions) {

        var starts = map.keySet().stream().filter(n -> n.endsWith("A")).toList();

        var current = new ArrayList<>(starts);
        var next = new ArrayList<String>();

        var seen = new HashSet<String>();

        long step = 0;
        boolean done = false;
        while (!done) {
            done = true;

            int ix = (int) (step % directions.length());
            var dir = directions.charAt(ix);
            var dx = dir == 'L' ? 0 : 1;

            next.clear();
            for (var node : current) {
                var exits = map.get(node);
                var exit = exits[dx];
                next.add(exit);
                done &= exit.endsWith("Z");
            }

            current.clear();
            current.addAll(next);
            step++;

            if (step % 10000000 == 0) {
                System.out.println(step);
            }
        }
        return step;
    }
}

