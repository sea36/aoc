package aoc2022.day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day15Main {

    static final Pattern PATTERN = Pattern.compile("Sensor at x=(.*), y=(.*): closest beacon is at x=(.*), y=(.*)");

    record Line(int p0, int p1) {

        boolean overlaps(Line o) {
            return (p0 <= o.p1 && p1 >= o.p0) || p0 == o.p1 + 1 || p1 == o.p0 - 1;
        }

        Line merge(Line o) {
            return new Line(Math.min(p0, o.p0), Math.max(p1, o.p1));
        }

        public int length() {
            return p1 - p0 + 1;
        }

        public Line limit(int min, int max) {
            int q0 = Math.min(Math.max(min, p0), max);
            int q1 = Math.min(Math.max(min, p1), max);
            return new Line(q0, q1);
        }
    }

    record Sensor(int x, int y, int beaconx, int beaxony) {

        public int manhattanDistance() {
            return Math.abs(beaconx - x) + Math.abs(beaxony - y);
        }

        Line sensorRange(int row) {
            int md = manhattanDistance();

//            System.out.printf("Sensor %d,%d   Beacon %d,%d%n", x, y, beaconx, beaxony);
//            System.out.println("  md: " + md);
//            System.out.println("  y: " + (y - md) + "-" + (y + md));


            if (row > y - md && row < y + md) {
                int len = md - Math.abs(row - y);
                Line line = new Line(x - len, x + len);
//                System.out.println("  " + line);
                return line;
            }
            return null;
        }
    }


    public static void main(String[] args) throws IOException {

//        final var input = "2022/inputs/test15.txt";
        final var input = "2022/inputs/input15.txt";

//        int maxd = 20;
        int maxd = 4000000;

        var lines = Files.lines(Paths.get(input)).collect(Collectors.toList());
        var sensors = lines.stream().map(Day15Main::parse).collect(Collectors.toList());

        for (int target = 0; target <= maxd; target++) {

            List<Line> ranges = new ArrayList<>();
            for (var s : sensors) {
                var line = s.sensorRange(target);
                if (line != null) {
                    line = line.limit(0, maxd);
                    ranges.add(line);
                }
            }

            for (int i = 0; i < ranges.size(); i++) {
                for (int j = i + 1; j < ranges.size(); j++) {
                    var l0 = ranges.get(i);
                    var l1 = ranges.get(j);
                    boolean overlaps = l0.overlaps(l1);
                    if (overlaps) {
                        ranges.set(i, l0.merge(l1));
                        ranges.remove(j);
                        i = 0;
                        j = 0;
                    }
                }
            }

            int n = 0;
            for (var r : ranges) {
                n += r.length();
            }

            var xs = new HashSet<Integer>();
            for (var s : sensors) {
                if (s.beaxony == target) {
                    if (xs.add(s.beaconx)) {
                        n--;
                    }
                }
            }

//            System.out.println(target + "\t" + n);

            if (ranges.size() != 1) {
                System.out.println(target + "\t" + ranges);
                long x = (ranges.get(0).p0 == 0 ? ranges.get(0).p1 + 1 : ranges.get(1).p1 + 1);
                long y = target;
                long freq = (x * 4000000) + y;
                System.out.println(x + "," + y + "\t" + freq);
            }
        }
    }

    private static Sensor parse(String line) {
        var m = PATTERN.matcher(line);
        m.find();
        return new Sensor(
                Integer.parseInt(m.group(1)),
                Integer.parseInt(m.group(2)),
                Integer.parseInt(m.group(3)),
                Integer.parseInt(m.group(4))
        );
    }

}