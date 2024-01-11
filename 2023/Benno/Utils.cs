namespace Benno;

public static class Utils
{
    public static TResult Pipe<TValue, TResult>(this TValue source, Func<TValue, TResult> selector) => selector(source);

    public static IEnumerable<(T? previous, T current, T? next)> Window<T>(this IEnumerable<T> source)
    {
        using var enumerator = source.GetEnumerator();
        if (!enumerator.MoveNext())
            yield break;

        var next = enumerator.Current;
        T? current = default;

        while (enumerator.MoveNext())
        {
            var previous = current;
            current = next;
            next = enumerator.Current;
            yield return (
                previous,
                current,
                next
            );
        }

        yield return (
            previous: current,
            current: next!,
            next: default
        );
    }
}