package aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day2 {

    static class Game {
        int id;
        List<Sample> samples = new ArrayList<>();
    }

    static class Sample {
        int red, green, blue;
    }

    public static void main(String[] args) throws Exception {
//        final String path = "2023/inputs/example2.txt";
        final String path = "2023/inputs/input2.txt";

        List<Game> games = new ArrayList<>();
        for (String line : Files.readAllLines(Path.of(path))) {
            var game = new Game();
            games.add(game);

            var i0 = line.indexOf(':');
            game.id = Integer.parseInt(line.substring(5, i0));

            var sampleStrings = line.substring(i0 + 2).split("; ");
            for (var string : sampleStrings) {
                var sample = new Sample();
                game.samples.add(sample);

                for (var s : string.split(", ")) {
                    var parts = s.split(" ");
                    int n = Integer.parseInt(parts[0]);
                    switch (parts[1]) {
                        case "red":
                            sample.red = n;
                            break;
                        case "green":
                            sample.green = n;
                            break;
                        case "blue":
                            sample.blue = n;
                            break;
                        default:
                            throw new IllegalArgumentException(parts[1]);
                    }
                }
            }
        }


        int sum = games.stream()
                .filter(Day2::filter)
                .mapToInt(g -> g.id)
                .sum();

        int powers = games.stream()
                .mapToInt(Day2::power)
                .sum();

        System.out.println(sum);
        System.out.println(powers);


    }

    private static int power(Game game) {
        var min = new Sample();
        for (var sample : game.samples) {
            if (sample.red != 0) {
                min.red = Math.max(min.red, sample.red);
            }
            if (sample.green != 0) {
                min.green = Math.max(min.green, sample.green);
            }
            if (sample.blue != 0) {
                min.blue = Math.max(min.blue, sample.blue);
            }
        }

        return min.red * min.green * min.blue;
    }

    private static boolean filter(Game game) {
        for (var s : game.samples) {
            if (s.red > 12 || s.green > 13 || s.blue > 14) {
                return false;
            }
        }
        return true;
    }
}
