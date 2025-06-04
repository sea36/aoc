package aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Day1 {

    public static void main(String[] args) throws Exception {
//        final String path = "2023/inputs/example1.txt";
//        final String path = "2023/inputs/example1b.txt";
        final String path = "2023/inputs/input1.txt";

        var sum = Files.lines(Path.of(path))
                .map(Day1::resolveNumbers)
                .mapToInt(line ->
                {
                    char[] digits = line.chars()
                            .mapToObj(c -> (char) c)
                            .filter(Character::isDigit)
                            .collect(new Collector<Character, char[], char[]>() {
                                @Override
                                public Supplier<char[]> supplier() {
                                    return () -> new char[2];
                                }

                                @Override
                                public BiConsumer<char[], Character> accumulator() {
                                    return (digits, character) -> {
                                        digits[0] = digits[0] == '\0' ? character : digits[0];
                                        digits[1] = character;
                                    };
                                }

                                @Override
                                public BinaryOperator<char[]> combiner() {
                                    return (left, right) -> {
                                        left[1] = right[1];
                                        return right;
                                    };
                                }

                                @Override
                                public Function<char[], char[]> finisher() {
                                    return chars -> chars;
                                }

                                @Override
                                public Set<Characteristics> characteristics() {
                                    return Collections.singleton(Characteristics.IDENTITY_FINISH);
                                }
                            });
                    return Integer.parseInt(new String(digits));
                }).sum();

        System.out.println(sum);
    }

    private static String resolveNumbers(String s) {
        return s.replace("one", "o1e")
                .replace("two", "t2o")
                .replace("three", "t3e")
                .replace("four", "f4r")
                .replace("five", "f5e")
                .replace("six", "s6x")
                .replace("seven", "s7n")
                .replace("eight", "e8t")
                .replace("nine", "n9e");
    }

}
