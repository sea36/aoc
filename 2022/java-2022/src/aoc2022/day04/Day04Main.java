package aoc2022.day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class Day04Main {

    record Range(int min, int max) { }

    public static void main(String[] args) throws IOException {
//        final String input = "2022/inputs/test04.txt";
        final String input = "2022/inputs/input04.txt";


        CleaningRota rota = new CleaningRota();
        Files.lines(Path.of(input))
                .forEach(rota);

        System.out.println(rota.completelyContains);
        System.out.println(rota.overlaps);

    }

    private static Range getRange(String range) {
        String[] vals = range.split("-");
        return new Range(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
    }

    private static class CleaningRota implements Consumer<String> {
        public int completelyContains;
        public int overlaps;

        @Override
        public void accept(String s) {
            final String[] elfSections = s.split(",");
            final Range elf1Range = getRange(elfSections[0]);
            final Range elf2Range = getRange(elfSections[1]);

            if (completelyContains(elf1Range, elf2Range)) {
                completelyContains++;
            }

            if (overlaps(elf1Range, elf2Range)) {
                overlaps++;
            }
        }

        private boolean overlaps(Range r1, Range r2) {
            return r1.min <= r2.max && r1.max >= r2.min;
        }

        private boolean completelyContains(Range r1, Range r2) {

            return (r1.min <= r2.min && r1.max >= r2.max) ||
                    (r2.min <= r1.min && r2.max >= r1.max);

        }
    }
}