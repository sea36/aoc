use std::fs;
use std::path::Path;

fn main() {
    let path = Path::new("../inputs/input02.txt");
    let input = fs::read_to_string(path).expect("load input");

    let games: Vec<_> = input.lines()
        .map(|game| {
            match game {
                "A X" => [1 + 3, 3 + 0],
                "A Y" => [2 + 6, 1 + 3],
                "A Z" => [3 + 0, 2 + 6],
                "B X" => [1 + 0, 1 + 0],
                "B Y" => [2 + 3, 2 + 3],
                "B Z" => [3 + 6, 3 + 6],
                "C X" => [1 + 6, 2 + 0],
                "C Y" => [2 + 0, 3 + 3],
                "C Z" => [3 + 3, 1 + 6],
                _ => [-1, -1]
            }
        })
        .collect();

    println!("{}", games.iter().map(|r| {r[0]}).sum::<i32>());
    println!("{}", games.iter().map(|r| {r[1]}).sum::<i32>());

}
