package aoc2022.day16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day16Main {

    // Valve HH has flow rate=22; tunnel leads to valve GG
    static final Pattern PATTERN = Pattern.compile("Valve (.*) has flow rate=(.*); tunnels? leads? to valves? (.*)");

    static class Valve {

        final String name;
        final int flowRate;
        final String[] neighbourNames;
        Valve[] neighbours;

        Valve(String name, int flowRate, String[] neighbourNames) {
            this.name = name;
            this.flowRate = flowRate;
            this.neighbourNames = neighbourNames;
        }

        @Override
        public boolean equals(Object o) {
            return this == o;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(this);
        }
    }

    public static void main(String[] args) throws IOException {

        final var input = "2022/inputs/test16.txt";
//        final var input = "2022/inputs/input16.txt";

        var lines = Files.lines(Paths.get(input)).collect(Collectors.toList());
        var valves = parseInput(lines);

        var start = valves.get("AA");
        var remaining = 30;

        var maxFlow = new int[1];

        search(start, remaining, 0, new HashSet<>(), flow -> maxFlow[0] = Math.max(flow, maxFlow[0]), new ArrayList<>());

        System.out.println(maxFlow[0]);
    }

    private static void search(Valve current, int remaining, int flow, Set<Valve> opened, IntConsumer maxFlow, List<Valve> pathOpened) {

        if (remaining > 1) {
            for (var valve : current.neighbours) {
                search(valve, remaining - 1, flow, opened, maxFlow, pathOpened);
            }
        }

        if (current.flowRate != 0 && !opened.contains(current)) {
            remaining--;
            flow += current.flowRate * remaining;
            maxFlow.accept(flow);
            pathOpened.add(current);
        } else {
            pathOpened.add(null);
        }

        if (remaining > 1) {
            for (var valve : current.neighbours) {
                search(valve, remaining - 1, flow, opened, maxFlow, pathOpened);
            }
        }

        if (pathOpened.remove(pathOpened.size() - 1) == current) {
            opened.remove(current);
        }
    }

    private static Map<String, Valve> parseInput(List<String> lines) {
        var valves = new HashMap<String, Valve>();
        for (var line : lines) {
            var m = PATTERN.matcher(line);
            if (m.find()) {
                var label = m.group(1);
                var flowRate = Integer.parseInt(m.group(2));
                var neighbours = m.group(3).split(", ");
                valves.put(label, new Valve(label, flowRate, neighbours));
            } else System.out.println(line);
        }

        for (var valve : valves.values()) {
            valve.neighbours = Stream.of(valve.neighbourNames).map(valves::get).toArray(Valve[]::new);
        }

        return valves;
    }
}