package cg;

import org.junit.Test;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GrahamScanTest {

    @Test
    public void getConvexHullTest() {

        /*
            6 |       d
            5 |     b   g
            4 |   a   e   i
            3 |     c   h
            2 |       f
            1 |
            0 '------------
              0 1 2 3 4 5 6
        */
        Point a = new Point(2, 4);
        Point b = new Point(3, 5);
        Point c = new Point(3, 3);
        Point d = new Point(4, 6);
        Point e = new Point(4, 4);
        Point f = new Point(4, 2);
        Point g = new Point(5, 5);
        Point h = new Point(5, 3);
        Point i = new Point(6, 4);

        List<Point> convexHull = GrahamScan.getConvexHull(Arrays.asList(a, b, c, d, e, f, g, h, i));

        assertThat(convexHull.size(), is(5));

        assertThat(convexHull.get(0), is(f));
        assertThat(convexHull.get(1), is(i));
        assertThat(convexHull.get(2), is(d));
        assertThat(convexHull.get(3), is(a));
        assertThat(convexHull.get(4), is(f));

        /*
            6       |       d   m
            5       |     b   g
            4       |   a   e   i
            3 j     |     c   h
            2       |       f
            1       |
            0       '------------
           -1
           -2                   k
           -3
              -2 -1 0 1 2 3 4 5 6
        */
        Point j = new Point(-2, 3);
        Point k = new Point(6, -2);
        Point m = new Point(6, 6);

        convexHull = GrahamScan.getConvexHull(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k, m));

        assertThat(convexHull.size(), is(5));

        assertThat(convexHull.get(0), is(k));
        assertThat(convexHull.get(1), is(m));
        assertThat(convexHull.get(2), is(d));
        assertThat(convexHull.get(3), is(j));
        assertThat(convexHull.get(4), is(k));

        /*
            large   |                         m
            .       |
            .       |
            7  j    |
            6       |       d
            5       |     b   g
            4       |   a   e   i
            3       |     c   h
            2       |       f
            1       |
            0       '--------------------------
           -1
           -2                       k
           -3
              -2 -1 0 1 2 3 4 5 6 7 8 . . large
        */
        j = new Point(-2, 7);
        k = new Point(8, -2);
        m = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

        convexHull = GrahamScan.getConvexHull(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k, m));

        assertThat(convexHull.size(), is(4));

        assertThat(convexHull.get(0), is(k));
        assertThat(convexHull.get(1), is(m));
        assertThat(convexHull.get(2), is(j));
        assertThat(convexHull.get(3), is(k));
    }

    @Test
    public void getLowestPointTest() {

        /*
            6    |       d
            5    |     b   g
            4    |   a   e   i
            3    |     c   h
            2    |       f
            1    |
            0    '------------
           -1
           -2
              -1 0 1 2 3 4 5 6
        */
        Point a = new Point(2, 4);
        Point b = new Point(3, 5);
        Point c = new Point(3, 3);
        Point d = new Point(4, 6);
        Point e = new Point(4, 4);
        Point f = new Point(4, 2);
        Point g = new Point(5, 5);
        Point h = new Point(5, 3);
        Point i = new Point(6, 4);

        Point lowest = GrahamScan.getLowestPoint(Arrays.asList(a, b, c, d, e, f, g, h, i));

        assertThat(lowest, is(f));

        /*
            6    |       d
            5    |     b   g
            4    |   a   e   i
            3    |     c   h
            2    |       f
            1    |
            0    '------------
           -1  j             k
           -2
              -1 0 1 2 3 4 5 6
        */
        Point j = new Point(-1, -1);
        Point k = new Point(6, -1);

        lowest = GrahamScan.getLowestPoint(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k));

        assertThat(lowest, is(j));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPointsTestFail() {
        GrahamScan.getConvexHull(new int[]{1, 2, 3, 4, 5}, new int[]{1, 2, 3, 4});
    }

    @Test
    public void getSortedPointSetTest() {

        /*
            6    |
            5    |
            4    |
            3    |
            2    g   a
            1    f b
            0    c-e-d--------
           -1
           -2
              -1 0 1 2 3 4 5 6
        */
        Point a = new Point(2, 2);
        Point b = new Point(1, 1);
        Point c = new Point(0, 0);
        Point d = new Point(2, 0);
        Point e = new Point(1, 0);
        Point f = new Point(0, 1);
        Point g = new Point(0, 2);
        Point h = new Point(2, 2); // duplicate
        Point i = new Point(2, 2); // duplicate

        List<Point> points = Arrays.asList(a, b, c, d, e, f, g, h, i);

        Set<Point> set = GrahamScan.getSortedPointSet(points);
        Point[] array = set.toArray(new Point[set.size()]);

        assertThat(set.size(), is(7));

        assertThat(array[0], is(c));
        assertThat(array[1], is(e));
        assertThat(array[2], is(d));
        assertThat(array[3], is(b));
        assertThat(array[4], is(a));
        assertThat(array[5], is(f));
        assertThat(array[6], is(g));
    }

    @Test
    public void getTurnTest() {

        /*
            9       |             d
            8       |               c
            7       |                 e
            6       |
            5       |
            4       |       b
            3       |
            2       |
            1   h   | a
            0     g '------------------
           -1       f
           -2
              -2 -1 0 1 2 3 4 5 6 7 8 9
        */
        Point a = new Point(1, 1);
        Point b = new Point(4, 4);
        Point c = new Point(8, 8);
        Point d = new Point(7, 9);
        Point e = new Point(9, 7);
        Point f = new Point(0, -1);
        Point g = new Point(-1, 0);
        Point h = new Point(-2, 1);

        assertThat(GrahamScan.getTurn(a, b, c), is(GrahamScan.Turn.COLLINEAR));
        assertThat(GrahamScan.getTurn(a, c, b), is(GrahamScan.Turn.COLLINEAR));
        assertThat(GrahamScan.getTurn(b, a, c), is(GrahamScan.Turn.COLLINEAR));
        assertThat(GrahamScan.getTurn(c, b, a), is(GrahamScan.Turn.COLLINEAR));
        assertThat(GrahamScan.getTurn(e, d, c), is(GrahamScan.Turn.COLLINEAR));
        assertThat(GrahamScan.getTurn(h, f, g), is(GrahamScan.Turn.COLLINEAR));

        assertThat(GrahamScan.getTurn(a, b, e), is(GrahamScan.Turn.CLOCKWISE));
        assertThat(GrahamScan.getTurn(a, b, f), is(GrahamScan.Turn.CLOCKWISE));
        assertThat(GrahamScan.getTurn(a, c, e), is(GrahamScan.Turn.CLOCKWISE));
        assertThat(GrahamScan.getTurn(a, c, f), is(GrahamScan.Turn.CLOCKWISE));
        assertThat(GrahamScan.getTurn(c, b, g), is(GrahamScan.Turn.CLOCKWISE));
        assertThat(GrahamScan.getTurn(d, b, f), is(GrahamScan.Turn.CLOCKWISE));

        assertThat(GrahamScan.getTurn(a, b, d), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
        assertThat(GrahamScan.getTurn(a, e, d), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
        assertThat(GrahamScan.getTurn(e, c, f), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
        assertThat(GrahamScan.getTurn(b, d, a), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
        assertThat(GrahamScan.getTurn(a, g, f), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
        assertThat(GrahamScan.getTurn(f, b, a), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
    }
}
