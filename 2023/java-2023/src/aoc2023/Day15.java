package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Day15 {

    static class Box {
        LinkedHashMap<String,Integer> slots = new LinkedHashMap<>();
    }

    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example15.txt";
        final String path = "2023/inputs/input15.txt";

        var input = Files.readString(Path.of(path));

        var steps = input.split(",");
        var result1 = Arrays.stream(steps)
                .mapToInt(Day15::hash)
                .sum();

        var boxes = new Box[256];
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new Box();
        }

        for (var s : steps) {
            if (s.endsWith("-")) {
                var label = s.substring(0, s.length()-1);
                var box = boxes[hash(label)];
                box.slots.remove(label);

            } else {
                var i = s.indexOf('=');
                var label = s.substring(0, i);
                var focalLength = Integer.parseInt(s.substring(i + 1));
                var box = boxes[hash(label)];
                box.slots.put(label, focalLength);

            }
        }

        var result2 = 0;
        for (int i = 0; i < 256; i++) {
            var box = boxes[i];
            int j = 1;
            for (int n : box.slots.values()) {
                result2 += (i + 1) * j * n;
                j++;
            }
        }

        System.out.println(result1);
        System.out.println(result2);
    }

    private static int hash(String s) {
        var hash = 0;
        for (var ch : s.toCharArray()) {
            hash += ch;
            hash *= 17;
            hash = hash & 0xFF;
        }
        return hash;
    }
}
