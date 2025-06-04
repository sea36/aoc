package aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day7 {

    enum HandRank {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }


    record Hand(HandRank handRank, int[] cardRanks, int bid) implements Comparable<Hand> {

        @Override
        public int compareTo(Hand o) {
            if (handRank != o.handRank) {
                return handRank.ordinal() - o.handRank.ordinal();
            }
            for (int i = 0; i < 5; i++) {
                if (cardRanks[i] != o.cardRanks[i]) {
                    return cardRanks[i] - o.cardRanks[i];
                }
            }
            return 0;
        }
    }


    public static void main(String[] args) throws Exception {
//        final String path = "2023/inputs/example7.txt";
        final String path = "2023/inputs/input7.txt";

        final List<String> lines = Files.readAllLines(Path.of(path));
        List<Hand> hands = lines.stream()
                .map((String s) -> parseHand(s, true))
                .sorted()
                .toList();

        long result = scoreHands(hands);

        System.out.println(result);
    }

    private static long scoreHands(List<Hand> hands) {
        long result = IntStream.range(0, hands.size())
                .mapToLong(i -> (long) (i + 1) * hands.get(i).bid)
                .sum();
        return result;
    }

    private static Hand parseHand(String s, boolean joker) {
        String cards = s.substring(0, 5);
        int bid = Integer.parseInt(s.substring(6));

        int[] cardRanks = new int[5];
        for (int i = 0; i < 5; i++) {
            cardRanks[i] = getCardRank(cards.charAt(i), joker);
        }

        System.out.print(cards + "\t");

        HandRank handRank = getHandRank(cardRanks, cards);

        System.out.println(handRank);

        return new Hand(handRank, cardRanks, bid);
    }

    private static HandRank getHandRank(int[] cardRanks, String cards) {
        int[] cardCounts = new int[15];
        for (int card : cardRanks) {
            cardCounts[card]++;
        }

        int jokers = cardCounts[0];

        int[] countCounts = new int[6];
        for (int n : cardCounts) {
            if (n > 0) {
                countCounts[n]++;
            }
        }

        System.out.print(jokers + "\t" + Arrays.toString(countCounts) + "\t");

        if (countCounts[5] == 1 || (countCounts[4] == 1 && jokers == 1) || (countCounts[3] == 1 && jokers == 2) || (countCounts[2] == 1 && jokers == 3) || jokers == 4) {
            return HandRank.FIVE_OF_A_KIND;
        }
        if (countCounts[4] == 1 || (countCounts[3] == 1 && jokers == 1) || (countCounts[2] == 2 && jokers == 2) || jokers == 3) {
            return HandRank.FOUR_OF_A_KIND;
        }
        if ((countCounts[3] == 1 && countCounts[2] == 1) || (countCounts[2] == 2 && jokers == 1)) {
            return HandRank.FULL_HOUSE;
        }
        if (countCounts[3] == 1 || (countCounts[2] == 1 && jokers == 1) || jokers == 2) {
            return HandRank.THREE_OF_A_KIND;
        }
        if (countCounts[2] == 2) {
            return HandRank.TWO_PAIR;
        }
        if (countCounts[2] == 1 || jokers == 1) {
            return HandRank.ONE_PAIR;
        }
        return HandRank.HIGH_CARD;

    }

    private static int getCardRank(char c, boolean joker) {
        return switch (c) {
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case 'T' -> 10;
            case 'J' -> joker ? 0 : 11;
            case 'Q' -> 12;
            case 'K' -> 13;
            case 'A' -> 14;
            default -> -1;
        };
    }
}
