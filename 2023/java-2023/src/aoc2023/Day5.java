package aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Day5 {

    record Range(long start, long length) {

        long end() {
            return start + length - 1;
        }
    }


    record Mapping(long dest, long src, long length) {

        long srcStart() {
            return src;
        }

        long srcEnd() {
            return src + length - 1;
        }

        long destEnd() {
            return dest + length - 1;
        }

        boolean contains(long n) {
            return n >= src && n <= (src + length - 1);
        }

        boolean overlaps(Range r) {
            return r.start() <= srcEnd() && r.end() >= srcStart();
        }

        long map(long n) {
            return dest + (n - src);
        }

        public Range map(Range r, Consumer<Range> remainder) {
            /*
                 56789                 2345678
                     ###################
                 ------------
                                  ------------

             */

            long start = Math.max(srcStart(), r.start());
            long end = Math.min(r.end(), srcEnd());
            long len = end - start + 1;
            long preLen = start - r.start();
            long postLen = r.end() - end;

            if (preLen > 0) {
                remainder.accept(new Range(r.start(), preLen));
            }
            if (postLen > 0) {
                remainder.accept(new Range(end + 1, postLen));
            }

            return new Range(map(start), len);
        }
    }

    public static void main(String[] args) throws Exception {
//        final String path = "2023/inputs/example5.txt";
        final String path = "2023/inputs/input5.txt";

        final List<String> lines = Files.readAllLines(Path.of(path));

        String[] seedVals = lines.get(0).substring(7).split(" ");
        long[] seeds = Arrays.stream(seedVals)
                .mapToLong(Long::parseLong)
                .toArray();

        Range[] seedRanges = readSeedRanges(seedVals);

        Mapping[] seedToSoil = readRange(lines, "seed-to-soil");
        Mapping[] soilToFertilizer = readRange(lines, "soil-to-fertilizer");
        Mapping[] fertilizerToWater = readRange(lines, "fertilizer-to-water");
        Mapping[] waterToLight = readRange(lines, "water-to-light");
        Mapping[] lightToTemperature = readRange(lines, "light-to-temperature");
        Mapping[] temperatureToHumidity = readRange(lines, "temperature-to-humidity");
        Mapping[] humidityToLocation = readRange(lines, "humidity-to-location");

        long[] locations1 = findLocations(seeds, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation);
        Range[] locations2 = findLocations(seedRanges, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation);


        long minLocation1 = Arrays.stream(locations1).min().getAsLong();
        long minLocation2 = Arrays.stream(locations2).mapToLong(r -> r.start).min().getAsLong();

        System.out.println(minLocation1);
        System.out.println(minLocation2);

    }

    private static Range[] readSeedRanges(String[] s) {
        List<Range> ranges = new ArrayList<>();
        for (int i = 0; i < s.length; i += 2) {
            ranges.add(new Range(Long.parseLong(s[i]), Long.parseLong(s[i + 1])));
        }
        return ranges.toArray(Range[]::new);
    }

    private static long[] findLocations(long[] seeds, Mapping[]... mappings) {
        long[] locations = new long[seeds.length];
        for (int i = 0; i < seeds.length; i++) {
            locations[i] = findLocation(seeds[i], mappings);
        }
        return locations;
    }

    private static Range[] findLocations(Range[] seeds, Mapping[]... mappings) {
        List<Range> locations = new ArrayList<>();
        for (int i = 0; i < seeds.length; i++) {
            locations.addAll(findLocation(seeds[i], mappings));
        }
        return locations.toArray(Range[]::new);
    }

    private static long findLocation(long seed, Mapping[][] mappings) {
        long dest = seed;

        for (Mapping[] mapping : mappings) {

            for (Mapping m : mapping) {
                if (m.contains(dest)) {
                    dest = m.map(dest);
                    break;
                }
            }
        }

        return dest;
    }

    private static Mapping[] readRange(List<String> lines, String s) {
        List<Mapping> mappings = new ArrayList<>();

        int i = lines.indexOf(String.format("%s map:", s)) + 1;
        while (i < lines.size() && !lines.get(i).isBlank()) {
            long[] vals = Arrays.stream(lines.get(i).split(" ")).mapToLong(Long::parseLong).toArray();
            mappings.add(new Mapping(vals[0], vals[1], vals[2]));
            i++;
        }

        return mappings.toArray(Mapping[]::new);
    }

    private static List<Range> findLocation(Range seed, Mapping[][] mappings) {
        List<Range> srcs = new ArrayList<>();
        srcs.add(seed);

        for (Mapping[] stage : mappings) {

            List<Range> dests = new ArrayList<>();
            for (int i = 0; i < srcs.size(); i++) {
                Range src = srcs.get(i);

                boolean hit = false;
                for (Mapping m : stage) {
                    if (m.overlaps(src)) {
                        dests.add(m.map(src, srcs::add));
                        hit = true;
                        break;
                    }
                }

                if (!hit) {
                    dests.add(src);
                }
            }

            srcs = dests;
        }

        return srcs;
    }
}
