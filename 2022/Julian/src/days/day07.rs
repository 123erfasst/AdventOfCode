use std::collections::HashMap;

use crate::helper::solution::{Solution, SolutionPair};

pub fn solve() -> SolutionPair {
    let input = include_str!("./input_day07.txt");

    (solve_part_1(input), solve_part_2(input))
}
#[derive(Debug)]
struct Folder {
    name: String,
    subfolder: Vec<String>,
    size: u32,
}

impl Folder {
    fn calculate_size(&self, folders: &HashMap<String, Folder>) -> u32 {
        self.size
            + self
                .subfolder
                .iter()
                .map(|folder| {
                    folders
                        .get(&(self.name.to_string() + &folder.to_string()))
                        .map(|f| f.calculate_size(&folders))
                        .unwrap_or(0)
                })
                .sum::<u32>()
    }
}

fn solve_part_1(input: &'static str) -> Option<Solution> {
    let folders = get_folder_structure(input);
    let result = folders
        .values()
        .map(|value| value.calculate_size(&folders))
        .filter(|size| size <= &100000)
        .sum();

    Some(Solution::U32(result))
}

fn solve_part_2(input: &'static str) -> Option<Solution> {
    let folders = get_folder_structure(input);
    let total_size = 70000000;
    let used_space = folders
        .get("/")
        .map(|root| root.calculate_size(&folders))
        .unwrap_or(0);

    folders
        .values()
        .map(|value| value.calculate_size(&folders))
        .filter(|size| size >= &&(30000000 - (total_size - used_space)))
        .min()
        .map(|val| Solution::U32(val))
}

fn get_folder_structure(input: &str) -> HashMap<String, Folder> {
    let mut folders: HashMap<String, Folder> = HashMap::new();
    let mut current_directory: Vec<String> = vec![];
    let mut sub_directories: Vec<String> = vec![];
    let mut folder_size: u32 = 0;
    for line in input
        .lines()
        .map(|line| line.trim_start_matches("$ ").split_once(" "))
    {
        match line {
            Some(("cd", "..")) => {
                if current_directory.len() > 0 && !folders.contains_key(&current_directory.join(""))
                {
                    folders.insert(
                        current_directory.join(""),
                        Folder {
                            name: current_directory.join(""),
                            subfolder: sub_directories.clone(),
                            size: folder_size,
                        },
                    );
                    sub_directories = vec![];
                    folder_size = 0;
                }
                current_directory.pop();
                continue;
            }
            Some(("cd", dir)) => {
                if current_directory.len() > 0 && !folders.contains_key(&current_directory.join(""))
                {
                    folders.insert(
                        current_directory.join(""),
                        Folder {
                            name: current_directory.join(""),
                            subfolder: sub_directories.clone(),
                            size: folder_size,
                        },
                    );
                    sub_directories = vec![];
                    folder_size = 0;
                }

                current_directory.push(dir.to_string());
                continue;
            }
            Some(("dir", sub)) => {
                sub_directories.push(sub.to_string());
                continue;
            }
            Some((val, _)) => {
                let size = val.parse::<u32>().unwrap_or(0);
                folder_size += size;
            }
            _ => continue,
        }
    }
    if current_directory.len() > 0 && !folders.contains_key(&current_directory.join("")) {
        folders.insert(
            current_directory.join(""),
            Folder {
                name: current_directory.join(""),
                subfolder: sub_directories.clone(),
                size: folder_size,
            },
        );
    }

    folders
}
