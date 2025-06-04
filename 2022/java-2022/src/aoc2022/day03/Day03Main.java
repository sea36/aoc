package aoc2022.day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class Day03Main {

    public static void main(String[] args) throws IOException {
//        final String input = "2022/inputs/test03.txt";
        final String input = "2022/inputs/input03.txt";


        MisplacedItems games = new MisplacedItems();
        Files.lines(Path.of(input))
                .forEach(games);

        System.out.println(games.totalPriority);

        BadgeFinder badgeFinder = new BadgeFinder();
        Files.lines(Path.of(input))
                .forEach(badgeFinder);

        System.out.println(badgeFinder.totalPriority);



    }

    private static class MisplacedItems implements Consumer<String> {

        private int totalPriority;

        @Override
        public void accept(String s) {
            int totalItems = s.length();
            int pocketItems = totalItems / 2;

            Set<Character> items = new HashSet<>();
            for (int i = 0; i < pocketItems; i++) {
                char item = s.charAt(i);
                items.add(item);
            }

            for (int i = 0; i < pocketItems; i++) {
                char item = s.charAt(pocketItems + i);
                if (items.contains(item)) {
                    int priority = getPriority(item);
//                    System.out.println(item + "\t" + priority);
                    totalPriority += priority;
                    break;
                }
            }
        }

    }

    private static class BadgeFinder implements Consumer<String> {

        private int totalPriority;
        private int elfCount;
        private BitSet[] elves = new BitSet[3];
        @Override
        public void accept(String s) {

            BitSet elfItems = new BitSet(52);
            s.chars().forEach(i -> {
                char c = (char) i;
                int priorty = getPriority(c);
                elfItems.set(priorty - 1);
            });

            elves[elfCount] = elfItems;
            elfCount++;

            if (elfCount == 3) {
                BitSet commonItems = elves[0];
                commonItems.and(elves[1]);
                commonItems.and(elves[2]);

                int badge = commonItems.nextSetBit(0) + 1;
                totalPriority += badge;

                elfCount = 0;
            }

        }

    }

    private static int getPriority(char item) {
        if (item >= 'a' && item <= 'z') {
            return 1 + (item - 'a');
        }
        if (item >= 'A' && item <= 'Z') {
            return 27 + (item - 'A');
        }
        throw new IllegalArgumentException("Invalid: " + item);
    }
}