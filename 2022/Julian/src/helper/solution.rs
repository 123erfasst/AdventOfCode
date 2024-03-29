use std::fmt::{Display, Formatter, Result};
use Solution::*;

pub type SolutionPair = (Option<Solution>, Option<Solution>);

#[allow(dead_code)]
pub enum Solution {
    I32(i32),
    I64(i64),
    I128(i128),
    U32(u32),
    U64(u64),
    U128(u128),
    Str(String),
    Usize(usize),
}

impl Display for Solution {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result {
        match self {
            I32(x) => x.fmt(f),
            I64(x) => x.fmt(f),
            I128(x) => x.fmt(f),
            U32(x) => x.fmt(f),
            U64(x) => x.fmt(f),
            U128(x) => x.fmt(f),
            Str(x) => x.fmt(f),
            Usize(x) => x.fmt(f),
        }
    }
}
