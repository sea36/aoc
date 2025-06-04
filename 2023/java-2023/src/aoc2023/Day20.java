package aoc2023;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day20 {

    enum Type {
        BROADCASTER,
        FLIP_FLOP,
        CONJUNCTION,
        OUTPUT
    }

    enum PulseType {
        LOW, HIGH
    }

    record Pulse(String source, PulseType type, String destination) {
    }

    static class Module {

        private final String name;
        private final Type type;
        private final Map<String, PulseType> connections = new HashMap<>();
        private final List<String> destinations;
        private boolean isOn = false;

        public Module(String name, Type type, List<String> destinations) {
            this.name = name;
            this.type = type;
            this.destinations = destinations;
        }

        public void pulse(Pulse pulse, ArrayDeque<Pulse> pulseQueue) {
            if (type == Type.BROADCASTER) {
                for (var dest : destinations) {
                    pulseQueue.add(new Pulse(name, pulse.type, dest));
                }
            }
            else if (type == Type.FLIP_FLOP) {
                if (pulse.type == PulseType.LOW) {
                    isOn = !isOn;
                    var pulseType = isOn ? PulseType.HIGH : PulseType.LOW;
                    for (var dest : destinations) {
                        pulseQueue.add(new Pulse(name, pulseType, dest));
                    }
                }
            }
            else if (type == Type.CONJUNCTION) {
                connections.put(pulse.source, pulse.type);

                var allHigh = isAllHigh();
                var pulseType = allHigh ? PulseType.LOW : PulseType.HIGH;
                for (var dest : destinations) {
                    pulseQueue.add(new Pulse(name, pulseType, dest));
                }
            }
            // ignore OUTPUT types
        }

        private boolean isAllHigh() {
            return connections.values().stream().allMatch(p -> p == PulseType.HIGH);
        }

        public void reset() {
            isOn = false;
            for (var entry : connections.entrySet()) {
                entry.setValue(PulseType.LOW);
            }
        }
    }


    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example20.txt";
//        final String path = "2023/inputs/example20a.txt";
        final String path = "2023/inputs/input20.txt";

        var lines = Files.readAllLines(Path.of(path));

        var modules = new HashMap<String, Module>();
        modules.put("rx", new Module("rx", Type.OUTPUT, Collections.emptyList()));
        for (var line : lines) {
            var parts = line.split(" -> ");
            var dests = Arrays.asList(parts[1].split(", "));
            Module module;
            if ("broadcaster".equals(parts[0])) {
                module = new Module("broadcaster", Type.BROADCASTER, dests);
            } else if (parts[0].startsWith("&")) {
                module = new Module(parts[0].substring(1), Type.CONJUNCTION, dests);
            } else if (parts[0].startsWith("%")) {
                module = new Module(parts[0].substring(1), Type.FLIP_FLOP, dests);
            } else {
                throw new IllegalArgumentException("error: " + line);
            }
            modules.put(module.name, module);
        }

        // build links
        for (var module : modules.values()) {
            for (var dest : module.destinations) {
                var mod = modules.get(dest);
                if (mod == null) {
                    System.err.println("UNKNONWN: " + dest);
                } else {
                    mod.connections.put(module.name, PulseType.LOW);
                }
            }
        }

        long result1 = part1(modules);

        for (var module : modules.values()) {
            module.reset();
        }

        long result2 = part2(modules);

        System.out.println(result1);
        System.out.println(result2);

    }

    private static long part1(HashMap<String, Module> modules) {
        long low = 0, high = 0;
        var pulseQueue = new ArrayDeque<Pulse>();
        for (int i = 1; i <= 1000; i++) {
            pulseQueue.add(new Pulse("button", PulseType.LOW, "broadcaster"));

            while (!pulseQueue.isEmpty()) {
                var pulse = pulseQueue.remove();

                if (pulse.type == PulseType.LOW) {
                    ++low;
                } else {
                    ++high;
                }

                var module = modules.get(pulse.destination);
                if (module != null) {
                    module.pulse(pulse, pulseQueue);
                }
            }
        }

        return low * high;
    }

    private static long part2(Map<String, Module> modules) {

        /*
            Has structure:

            rx <- &lb <- [ &fk , &rz , &lf , &br ]

            find LCM of the cycle times of the 4 conjunction modules
         */

        var rx = modules.get("rx");     // rx has single parent
        var parent = modules.get(rx.connections.keySet().iterator().next());
        var roots = new HashSet<>(parent.connections.keySet());
        var cycleMap = new HashMap<String, Integer>();

        var pulseQueue = new ArrayDeque<Pulse>();
        for (int i = 1; cycleMap.size() < roots.size(); i++) {
            pulseQueue.add(new Pulse("button", PulseType.LOW, "broadcaster"));

            while (!pulseQueue.isEmpty()) {
                var pulse = pulseQueue.remove();
                var module = modules.get(pulse.destination);
                if (module != null) {
                    module.pulse(pulse, pulseQueue);

                    if (roots.contains(module.name) && !cycleMap.containsKey(module.name) && !module.isAllHigh()) {
                        cycleMap.put(module.name, i);
                    }

                }
            }
        }

        int[] cycleLengths = cycleMap.values().stream().mapToInt(Integer::intValue).toArray();
        return lcm(cycleLengths);
    }

    public static long lcm(int[] vals) {
        BigInteger lcm = BigInteger.valueOf(vals[0]);

        for (int i = 1; i < vals.length; i++) {
            BigInteger next = BigInteger.valueOf(vals[i]);
            lcm = next.multiply(lcm).divide(lcm.gcd(next));
        }

        return lcm.longValue();
    }
}
