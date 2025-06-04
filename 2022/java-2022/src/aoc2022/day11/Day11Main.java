package aoc2022.day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Day11Main {

    public static void main(String[] args) throws IOException {
//        final String input = "2022/inputs/test11.txt";
        final String input = "2022/inputs/input11.txt";


        final var monkeys = new ArrayList<Monkey>();
        Files.lines(Paths.get(input))
                .forEach(new Consumer<>() {

                    private Monkey monkey;

                    @Override
                    public void accept(String s) {
                        if (s.startsWith("Monkey")) {
                            monkey = new Monkey(s);
                            monkeys.add(monkey);
                        } else if (s.startsWith("  Starting items: ")) {
                            monkey.items.addAll(Arrays.stream(s.substring(18).split(", ")).map(Long::parseLong).collect(Collectors.toList()));
                        } else if (s.startsWith("  Operation: new = ")) {
                            monkey.operation = parseOperation(s);
                        } else if (s.startsWith("  Test: divisible by ")) {
                            monkey.testDivisor = Integer.parseInt(s.substring(21));
                        } else if (s.startsWith("    If true: throw to monkey ")) {
                            monkey.trueTarget = Integer.parseInt(s.substring(29));
                        } else if (s.startsWith("    If false: throw to monkey ")) {
                            monkey.falseTarget = Integer.parseInt(s.substring(30));
                        }
                    }
                });

        long mod = 1;
        for (final Monkey monkey : monkeys) {
            mod *= monkey.testDivisor;
        }


        for (int i = 0; i < 10000; i++) {

            if (i == 1 || i == 20 || i == 1000 || i == 2000) {
                System.out.println("== After round " + i + " ==");
                for (Monkey monkey : monkeys) {
                    System.out.println(monkey.name + " inspected " + monkey.itemsInspected);
                }

            }

            for (Monkey monkey : monkeys) {
                monkey.takeTurn(monkeys, mod);
            }

//            System.out.println("Round " + (i + 1));
//            for (Monkey monkey : monkeys) {
//                System.out.println(monkey.name + " " + monkey.items);
//            }
//
//            System.out.println();
        }


//        for (Monkey monkey : monkeys) {
//            System.out.println(monkey.name + " inspected " + monkey.itemsInspected);
//        }

        monkeys.sort(Comparator.comparing(Monkey::getItemsInspected).reversed());

        long m0 = monkeys.get(0).getItemsInspected();
        long m1 = monkeys.get(1).getItemsInspected();
        System.out.println(m0 * m1);
    }

    private static Operation parseOperation(String s) {
        if (s.equals("  Operation: new = old * old")) {
            return new Square();
        }
        if (s.startsWith("  Operation: new = old * ")) {
            return new Multiply(Integer.parseInt(s.substring(25)));
        }
        if (s.startsWith("  Operation: new = old + ")) {
            return new Add(Integer.parseInt(s.substring(25)));
        }
        throw new IllegalArgumentException("Unknown op: " + s);
    }

    static class Monkey {
        private final String name;
        private List<Long> items = new ArrayList<>();
        private Operation operation;
        private int testDivisor;
        private int trueTarget;
        private int falseTarget;

        private int itemsInspected;

        public Monkey(String name) {
            this.name = name;
        }

        public void takeTurn(List<Monkey> monkeys, long mod) {

            itemsInspected += items.size();

            for (long item : items) {
                long newWorry = operation.apply(item);
                newWorry = newWorry % mod;
//                System.out.printf("  Monkey inspects an item with a worry level of %d.%n", item);
//                System.out.printf("  New worry level is %d%n", newWorry);
                if (newWorry % testDivisor == 0) {
                    monkeys.get(trueTarget).items.add(newWorry);
                } else {
                    monkeys.get(falseTarget).items.add(newWorry);
                }
            }

            items.clear();
        }

        public int getItemsInspected() {
            return itemsInspected;
        }
    }


    sealed interface Operation permits Add, Multiply, Square {
        long apply(long v);
    }

    static final class Add implements Operation {
        final int x;

        Add(int x) {
            this.x = x;
        }

        @Override
        public long apply(long v) {
            return v + x;
        }
    }

    static final class Multiply implements Operation {
        final int x;

        Multiply(int x) {
            this.x = x;
        }

        @Override
        public long apply(long v) {
            return v * x;
        }
    }

    static final class Square implements Operation {

        @Override
        public long apply(long v) {
            return v * v;
        }
    }
}