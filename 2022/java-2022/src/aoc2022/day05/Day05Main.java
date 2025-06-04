package aoc2022.day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05Main {

    private static final Pattern PATTERN = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    static boolean preserveOrder = true;

    public static void main(String[] args) throws IOException {
//        final String input = "2022/inputs/test05.txt";
        final String input = "2022/inputs/input05.txt";


        Crane crane = new Crane();
        Files.lines(Path.of(input))
                .forEach(crane);

        for (int i = 0; i < 9; i++) {
            if (!crane.stacks[i].isEmpty()) {
                System.out.print(crane.stacks[i].get(0));
            }
            else {
                System.out.print(".");
            }
        }
        System.out.println();

    }

    private static class Crane implements Consumer<String> {
        public List<Character>[] stacks;

        private Crane() {
            stacks = new List[9];
            for (int i = 0; i < 9; i++) {
                stacks[i] = new ArrayList<>();
            }
        }

        @Override
        public void accept(String s) {
            if (s.startsWith("move")) {
                handleMove(s);
            }
            else if (s.contains("[")) {
                stackCrates(s);
            }
        }

        private void stackCrates(String s) {
            for (int i = 0; i < s.length(); i += 4) {
                char crate = s.charAt(i + 1);
                if (crate != ' ') {
                    stacks[i/4].add(crate);
                }
            }
        }

        private void handleMove(String s) {
            Matcher m = PATTERN.matcher(s);
            if (m.find()) {
                int moves = Integer.parseInt(m.group(1));
                int fromIx = Integer.parseInt(m.group(2));
                int toIx = Integer.parseInt(m.group(3));

                System.out.printf("move %d from %d to %d%n", moves, fromIx, toIx);

                List<Character> from = stacks[fromIx - 1];
                List<Character> to = stacks[toIx - 1];

                if (preserveOrder) {
                    for (int i = 0; i < moves && !from.isEmpty(); i++) {
                        to.add(i, from.remove(0));
                    }
                } else {
                    for (int i = 0; i < moves && !from.isEmpty(); i++) {
                        to.add(0, from.remove(0));
                    }
                }
            }
        }
    }
}