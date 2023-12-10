use std::ops::{Add, AddAssign, Mul, Sub, SubAssign};

#[allow(clippy::module_name_repetitions)]
#[derive(Debug, Eq, PartialEq, Copy, Clone)]
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
