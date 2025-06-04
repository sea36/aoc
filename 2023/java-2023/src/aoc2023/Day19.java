package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class Day19 {

    static final Pattern P_FILTER = Pattern.compile("(\\w+)([<>=])(\\d+):(\\w+)");
    static final Pattern P_PART = Pattern.compile("x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)");

    enum Operation {
        LT,
        GT;

        public static Operation of(String val) {
            return switch (val) {
                case "<" -> LT;
                case ">" -> GT;
                default -> throw new IllegalArgumentException("error: " + val);
            };
        }
    }

    record Parts(int x0, int x1, int m0, int m1, int a0, int a1, int s0, int s1) {
        public Parts limit(String param, int v0, int v1) {
            return new Parts(
                    "x".equals(param) ? v0 : x0,
                    "x".equals(param) ? v1 : x1,
                    "m".equals(param) ? v0 : m0,
                    "m".equals(param) ? v1 : m1,
                    "a".equals(param) ? v0 : a0,
                    "a".equals(param) ? v1 : a1,
                    "s".equals(param) ? v0 : s0,
                    "s".equals(param) ? v1 : s1
            );
        }
    }

    record Result(Optional<Parts> success, Optional<Parts> failure) {}

    record Filter(String param, Operation op, int value, String target) {

        boolean test(int x, int m, int a, int s) {

            var val = switch (param) {
                case "x" -> x;
                case "m" -> m;
                case "a" -> a;
                case "s" -> s;
                default -> throw new IllegalArgumentException("error: " + param);
            };

            boolean r = switch (op) {
                case GT -> val > this.value;
                case LT -> val < this.value;
            };

//            System.out.println(val + " " + op + " " + value + " = " + r);
            return r;
        }

        public Result mask(Parts p) {
            int[] v = getVals(p);
            return switch (op) {
                case LT -> new Result(
                        (v[0] < value) ? Optional.of(p.limit(param, v[0], value - 1)) : Optional.empty(),
                        (v[1] >= value) ? Optional.of(p.limit(param, value, v[1])) : Optional.empty());
                case GT -> new Result(
                        (v[1] > value) ? Optional.of(p.limit(param, value + 1, v[1])) : Optional.empty(),
                        (v[0] <= value) ? Optional.of(p.limit(param, v[0], value )) : Optional.empty());
            };
        }

        private int[] getVals(Parts p) {
            return switch (param) {
                case "x" -> new int[]{p.x0, p.x1};
                case "m" -> new int[]{p.m0, p.m1};
                case "a" -> new int[]{p.a0, p.a1};
                case "s" -> new int[]{p.s0, p.s1};
                default -> throw new IllegalArgumentException("error: " + p);
            };
        }
    }

    record Workflow(String name, Filter[] filters, String finalTarget, String text) {
    }

    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example19.txt";
        final String path = "2023/inputs/input19.txt";

        var workflows = new HashMap<String, Workflow>();
        var lines = Files.readAllLines(Path.of(path));

        var it = lines.iterator();
        while (it.hasNext()) {
            var line = it.next();
            if (line.isBlank()) {
                break;
            }

            var workflow = parseWorkflow(line);
            workflows.put(workflow.name, workflow);
        }

        var result1 = 0L;
        while (it.hasNext()) {
            var part = it.next();
            var matcher = P_PART.matcher(part);
            if (!matcher.find()) {
                throw new IllegalArgumentException("error: " + part);
            }

            var x = Integer.parseInt(matcher.group(1));
            var m = Integer.parseInt(matcher.group(2));
            var a = Integer.parseInt(matcher.group(3));
            var s = Integer.parseInt(matcher.group(4));

//            System.out.println();
//            System.out.println(part);

            var next = "in";
            while (true) {
                if (next.equals("A")) {
                    int r = x + m + a + s;
                    result1 += r;
//                    System.out.println(part + "\tA\t" + String.format("%d %d %d %d  =  %d", x, m, a, s, r));
                    break;
                }
                if (next.equals("R")) {
//                    System.out.println(part + "\tR");
                    break;
                }

                var workflow = workflows.get(next);
//                System.out.println(next + " -> " + workflow.text);
                next = workflow.finalTarget;
                for (var filter : workflow.filters) {
                    if (filter.test(x, m, a, s)) {
                        next = filter.target;
                        break;
                    }
                }
            }

        }

        System.out.println(result1);


        var parts = new ArrayList<Parts>();
        var part = new Parts(1, 4000, 1, 4000, 1, 4000, 1, 4000);
        gather(part, workflows, "in", 0, parts);

        var result2 = parts.stream()
                .mapToLong(p -> (p.x1 - p.x0 + 1L) * (p.m1 - p.m0 + 1L) * (p.a1 - p.a0 + 1L) * (p.s1 - p.s0 + 1L))
                .sum();
        System.out.println(result2);
    }

    private static void gather(Parts part, HashMap<String, Workflow> workflows, String next, int ix, ArrayList<Parts> parts) {

        if ("A".equals(next)) {
            parts.add(part);
            return;
        }
        if ("R".equals(next)) {
            return;
        }

        var workflow = workflows.get(next);
        var filters = workflow.filters;
        if (ix == filters.length) {
            gather(part, workflows, workflow.finalTarget, 0, parts);
            return;
        }

        var filter = filters[ix];
        var result = filter.mask(part);
        result.success.ifPresent(p -> gather(p, workflows, filter.target, 0, parts));
        result.failure.ifPresent(p -> gather(p, workflows, next, ix+1, parts));

    }

    private static Workflow parseWorkflow(String line) {
        var i0 = line.indexOf('{');

        var name = line.substring(0, i0);
        var steps = line.substring(i0 + 1, line.length() - 1).split(",");
        var filters = new Filter[steps.length - 1];
        for (var i = 0; i < filters.length; i++) {
            var m = P_FILTER.matcher(steps[i]);
            if (!m.find()) {
                throw new IllegalArgumentException("error: " + steps[i]);
            }
            filters[i] = new Filter(
                    m.group(1),
                    Operation.of(m.group(2)),
                    Integer.parseInt(m.group(3)),
                    m.group(4)
            );
        }

        var finalTarget = steps[steps.length - 1];
        return new Workflow(name, filters, finalTarget, line);
    }
}
