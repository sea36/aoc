package aoc2022.day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day06Main {

    public static final int WINDOW_SIZE = 14;

    public static void main(String[] args) throws IOException {
//        final String input = "2022/inputs/test05.txt";
        final String input = "2022/inputs/input06.txt";

//        final String stream = "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg";
        final String stream = Files.readString(Paths.get(input));

        char[] window = new char[WINDOW_SIZE];

        int j = 0;
        for (int i = 0; i < stream.length(); i++) {
            window[j] = stream.charAt(i);

            if (i >= WINDOW_SIZE && isUnqiue(window)) {
                System.out.println(i + 1);
                break;
            }

            j = (j + 1) % WINDOW_SIZE;
        }
    }

    private static boolean isUnqiue(char[] window) {
        for (int i = 0; i < WINDOW_SIZE; i++) {
            for (int j = i + 1; j < WINDOW_SIZE; j++) {
                if (window[i] == window[j]) {
                    return false;
                }
            }
        }
        return true;
    }

}