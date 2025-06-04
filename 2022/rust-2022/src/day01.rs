use std::fs;
use std::path::Path;

fn main() {
    let path = Path::new("../inputs/input01.txt");
    let input = fs::read_to_string(path).expect("load input");

    let elves: Vec<_> = input.split("\n\n")
        .map(|bag| {
            bag.lines()
                .map(|item| item.parse::<i32>().unwrap())
                .sum::<i32>()
        }).collect();

    println!("{}", elves.iter().max().unwrap());
    println!("{}", elves.iter().take(3).sum::<i32>());
}
