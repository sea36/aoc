package aoc2022.day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class Day01Main {

    public static void main(String[] args) throws IOException {
        final String input = "2022/inputs/input01.txt";

        final CalorieCounter calorieCounter = new CalorieCounter();

        Files.lines(Path.of(input))
                .forEach(calorieCounter);

        calorieCounter.endOfElf();;

        calorieCounter.elves.sort(Comparator.reverseOrder());

        System.out.println(calorieCounter.elves.subList(0, 3).stream().mapToInt(Integer::intValue).sum());

    }

    private static class CalorieCounter implements Consumer<String> {
        
        private int totalCalories;
        private List<Integer> elves = new ArrayList<>();

        @Override
        public void accept(String s) {
            if (s.isEmpty()) {
                endOfElf();
            } else {
                totalCalories += Integer.parseInt(s);
            }
        }

        private void endOfElf() {
            elves.add(totalCalories);
            totalCalories = 0;
        }
    }
}
