package aoc2022.day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class Day02Main {

    enum Play {
        ROCK(1), PAPER(2), SCISSORS(3);

        private final int score;

        Play(int score) {
            this.score = score;
        }
    }

    enum Outcome {
        WIN(6), LOSE(0), DRAW(3);

        private final int score;

        Outcome(int score) {
            this.score = score;
        }
    }

    static final Play[] PLAYS = Play.values();

    public static void main(String[] args) throws IOException {
        final String input = "2022/inputs/input02.txt";


        RockPaperScissors games = new RockPaperScissors();
        Files.lines(Path.of(input))
                .forEach(games);

        System.out.println(games.totalScore);

    }

    private static class RockPaperScissors implements Consumer<String> {

        private int totalScore;
        @Override
        public void accept(String s) {
            Play elfPlay = getElfPlay(s.charAt(0));
//            Play yourPlay = getYourPlay(s.charAt(2));
//            Outcome outcome = getOutcome(elfPlay, yourPlay);

            Outcome outcome = getOutcome(s.charAt(2));
            Play yourPlay = getYourPlay(elfPlay, outcome);

            int score = yourPlay.score + outcome.score;
            totalScore += score;
        }

        private Outcome getOutcome(Play elfPlay, Play yourPlay) {
            if (elfPlay == yourPlay) {
                return Outcome.DRAW;
            }
            return switch (elfPlay) {
                case ROCK -> yourPlay == Play.PAPER ? Outcome.WIN : Outcome.LOSE;
                case PAPER -> yourPlay == Play.SCISSORS ? Outcome.WIN : Outcome.LOSE;
                case SCISSORS -> yourPlay == Play.ROCK ? Outcome.WIN : Outcome.LOSE;
            };
        }

        private Play getElfPlay(char c) {
            switch (c) {
                case 'A': return Play.ROCK;
                case 'B': return Play.PAPER;
                case 'C': return Play.SCISSORS;
            }
            throw new IllegalArgumentException("Unknown " + c);
        }

        private Play getYourPlay(char c) {
            switch (c) {
                case 'X': return Play.ROCK;
                case 'Y': return Play.PAPER;
                case 'Z': return Play.SCISSORS;
            }
            throw new IllegalArgumentException("Unknown " + c);
        }

        private Outcome getOutcome(char c) {
            switch (c) {
                case 'X': return Outcome.LOSE;
                case 'Y': return Outcome.DRAW;
                case 'Z': return Outcome.WIN;
            }
            throw new IllegalArgumentException("Unknown " + c);
        }

        private Play getYourPlay(Play elfPlay, Outcome outcome) {
            if (outcome == Outcome.DRAW) {
                return elfPlay;
            }
            int offset = outcome == Outcome.WIN ? 1 : 2;
            return PLAYS[(elfPlay.ordinal() + offset) % 3];
        }
    }
}