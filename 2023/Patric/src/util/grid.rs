use crate::util::point::Point2D;
use nom::character::complete::{anychar, line_ending, not_line_ending};
use nom::multi::{many1, separated_list1};
use nom::{IResult, Parser};
use std::convert::identity;
use std::ops::{Index, IndexMut};

#[derive(Debug, Eq, PartialEq, Hash)]
pub struct Grid<T> {
    pub data: Vec<T>,
    pub width: usize,
    pub height: usize,
}

impl Grid<char> {
    #[inline]
    #[allow(clippy::missing_errors_doc)]
    pub fn parse(input: &str) -> IResult<&str, Self> {
        Self::parse_transform(input, identity)
    }
}

impl<T> Grid<T> {
    #[allow(clippy::missing_errors_doc)]
    pub fn parse_transform<F>(input: &str, transform: F) -> IResult<&str, Self>
    where
        F: FnMut(char) -> T,
    {
        let (rem, grid) =
            separated_list1(line_ending, not_line_ending.and_then(many1(anychar)))(input)?;
        let width = grid[0].len();
        let height = grid.len();
        let data = grid.into_iter().flatten().map(transform).collect();

        Ok((
            rem,
            Self {
                data,
                width,
                height,
            },
        ))
    }

    pub fn new(width: usize, height: usize, default: T) -> Self
    where
        T: Clone,
    {
        Self {
            width,
            height,
            data: vec![default; width * height],
        }
    }

    #[inline]
    #[must_use]
    pub fn contains(&self, position: &Point2D<usize>) -> bool {
        position.x < self.width && position.y < self.height
    }
}

impl<T> Index<&Point2D<usize>> for Grid<T> {
    type Output = T;

    fn index(&self, index: &Point2D<usize>) -> &Self::Output {
        &self.data[self.width * index.y + index.x]
    }
}

impl<T> IndexMut<&Point2D<usize>> for Grid<T> {
    fn index_mut(&mut self, index: &Point2D<usize>) -> &mut Self::Output {
        &mut self.data[self.width * index.y + index.x]
    }
}
