use std::ops::{Add, AddAssign, Mul, Sub, SubAssign};

#[allow(clippy::module_name_repetitions)]
#[derive(Debug, Eq, PartialEq, Copy, Clone, Hash)]
pub struct Point2D<T> {
    pub x: T,
    pub y: T,
}

impl<T> Point2D<T> {
    #[inline]
    #[must_use]
    pub const fn new(x: T, y: T) -> Self {
        Self { x, y }
    }
}

impl<T: Sub<Output = T> + Ord + Copy + Add<Output = T>> Point2D<T> {
    #[inline]
    #[must_use]
    pub fn manhattan_distance(self, rhs: &Self) -> T {
        abs_difference(self.x, rhs.x) + abs_difference(self.y, rhs.y)
    }
}

fn abs_difference<T: Sub<Output = T> + Ord + Copy>(x: T, y: T) -> T {
    if x < y {
        y - x
    } else {
        x - y
    }
}

impl<T: Mul<T, Output = T> + Sub<Output = T> + Copy> Point2D<T> {
    #[inline]
    #[must_use]
    pub fn determinant(a: Self, b: Self) -> T {
        a.x * b.y - a.y * b.x
    }
}

impl<T: Default> Default for Point2D<T> {
    fn default() -> Self {
        Point2D::new(T::default(), T::default())
    }
}

impl<T: Add<Output = T>> Add for Point2D<T> {
    type Output = Self;

    #[inline]
    #[must_use]
    fn add(self, rhs: Self) -> Self::Output {
        Point2D::new(self.x + rhs.x, self.y + rhs.y)
    }
}

impl<T: AddAssign> AddAssign for Point2D<T> {
    #[inline]
    fn add_assign(&mut self, rhs: Self) {
        self.x += rhs.x;
        self.y += rhs.y;
    }
}

impl<T: Sub<Output = T>> Sub for Point2D<T> {
    type Output = Self;

    #[inline]
    #[must_use]
    fn sub(self, rhs: Self) -> Self::Output {
        Point2D::new(self.x - rhs.x, self.y - rhs.y)
    }
}

impl<T: SubAssign> SubAssign for Point2D<T> {
    #[inline]
    fn sub_assign(&mut self, rhs: Self) {
        self.x -= rhs.x;
        self.y -= rhs.y;
    }
}

impl<T: Mul<M, Output = T>, M: Copy> Mul<M> for Point2D<T> {
    type Output = Self;

    #[inline]
    #[must_use]
    fn mul(self, rhs: M) -> Self::Output {
        Point2D::new(self.x * rhs, self.y * rhs)
    }
}
