package aoc2022.day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class Day10Main {

    static Set<Integer> interestingCycles = new HashSet<>(Arrays.asList(20, 60, 100, 140, 180, 220));

    public static void main(String[] args) throws IOException {
//        final String input = "2022/inputs/test10.txt";
        final String input = "2022/inputs/input10.txt";


        final Cpu cpu = new Cpu();
        Files.lines(Paths.get(input))
                .forEach(s -> {
                    if (s.startsWith("addx")) {
                        cpu.addx(Integer.parseInt(s.substring(5)));
                    } else {
                        cpu.noop();
                    }
                });

        System.out.println(cpu.signalSum);

        for (boolean[] row : cpu.screen) {
            for (boolean pixel : row) {
                System.out.print(pixel ? '#' : '.');
            }
            System.out.println();
        }

    }

    static class Cpu {

        private int x = 1;
        private int cycle = 1;

        private boolean[][] screen = new boolean[6][40];

        private int signalSum;

        public void noop() {
            tick();
        }

        public void addx(int v) {
            tick();
            tick();
            x += v;
        }

        public int getSignalStrength() {
            return x * cycle;
        }

        private void tick() {
            int px = (cycle - 1) % 40;
            int py = (cycle - 1) / 40;

            if (abs(x - px) <= 1) {
                screen[py][px] = true;
            }


            if (interestingCycles.contains(cycle)) {
//                System.out.println(cycle + " " + getSignalStrength());
                signalSum += getSignalStrength();
            }
            cycle++;
        }
    }
}