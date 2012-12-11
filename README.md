## Graham Scan

A Java implementation of the Graham Scan algorithm to find the convex hull of a set of points.

A demo of the implementaion is deployed in Appspot: [computational-geometry.appspot.com/graham-scan](http://computational-geometry.appspot.com/graham-scan)

### How to use it

The implmentation is pretty straight forward: everything resides in a single class. Simply copy the class in 
your project, and invoke either `GrahamScan#getConvexHull(int[], int[])`:

```java
// x coordinates
int[] xs = {3, 5, -1, 8, -6, 23, 4};

// y coordinates
int[] ys = {9, 2, -4, 3, 90, 3, -11};

// find the convex hull
List<java.awt.Point> convexHull = GrahamScan.getConvexHull(xs, ys);

for(java.awt.Point p : convexHull) {
    System.out.println(p);
}
```
or the method `GrahamScan#getConvexHull(List<java.awt.Point>)`:

```java
// the same points as the previous example
List<java.awt.Point> points = Arrays.asList(
        new java.awt.Point(3, 9),
        new java.awt.Point(5, 2),
        new java.awt.Point(-1, -4),
        new java.awt.Point(8, 3),
        new java.awt.Point(-6, 90),
        new java.awt.Point(23, 3),
        new java.awt.Point(4, -11)
);

// find the convex hull
List<java.awt.Point> convexHull = GrahamScan.getConvexHull(points);

for(java.awt.Point p : convexHull) {
    System.out.println(p);
}
```
both of which will print the following:

```
java.awt.Point[x=4,y=-11]
java.awt.Point[x=23,y=3]
java.awt.Point[x=-6,y=90]
java.awt.Point[x=-1,y=-4]
java.awt.Point[x=4,y=-11]
```

And/or have a look at the [unit tests](https://github.com/bkiers/GrahamScan/blob/master/src/test/cg/GrahamScanTest.java).