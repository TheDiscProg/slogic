# Scala Logic (SLogic)
As set of logical operators for Scala.

## XOR
A simple logical Exclusive OR Container for Scala.

It's signature is: `Xor[A, B]` where:
* `A` is the first type;
* `B` is the second type.

`Xor` can only contain one type or the other, not both nor neither - it must contain one and only one type.
Both left hand side and right hand side values are of equal importance (a departure from `Either[A, B]` where
the RHS value holds the required value and LHS the error).

It is used in functions or methods where it is possible for it two return one of two values, both equally acceptable.
For example, some methods can return a single value or a list of values, such as in `Xor[A, List[A]]`. It could be
argued that in such cases, why not return a List, which can contain one or more items. However, this may not
be acceptable in some situations. An example would be transforming between a Scala class and JSON object. Consider
the following JSON transformation to a Scala class:
```json
{
  "value" : "some value"
}
```
or
```json
{
  "value" : [
    "value 1",
    "value 2"
  ]
}
```
In such situations, using `Xor` can make the code easier to read and transform.

### Using `Xor`
1. To create, use `Xor.applyLeft` or `Xor.applyRight`
2. To fold, `fold[C](fa: A => C, fb: B => C)` giving a `C`
3. To execute side effect, `foreach[U](fa: A => Unit, fb: B => Unit)`
4. `getLeft` and `getRight` will return an `Option`
5. `isLeft` and `isRight` will be either true or false (a `Boolean`)
6. You can map it, `map[A1, B1](fa: A => A1, fb: B => B1)` giving a `Xor[A1, B1]`
7. An if you know whether it is Left or Right, use `mapLeft` and `mapRight`



