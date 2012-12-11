/*
 * Copyright (c) 2010, Bart Kiers
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package cg;

import java.awt.Point;
import java.util.*;

public final class GrahamScan {

    protected static enum Turn { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR }

    public static List<Point> getConvexHull(int[] xs, int[] ys) throws IllegalArgumentException {

        if(xs.length != ys.length) {
            throw new IllegalArgumentException("xs and ys don't have the same size");
        }

        List<Point> points = new ArrayList<Point>();

        for(int i = 0; i < xs.length; i++) {
            points.add(new Point(xs[i], ys[i]));
        }

        return getConvexHull(points);
    }

    public static List<Point> getConvexHull(List<Point> points) throws IllegalArgumentException {

        List<Point> sorted = new ArrayList<Point>(getSortedPointSet(points));

        if(sorted.size() < 3) {
            throw new IllegalArgumentException("can only create a convex hull of 3 or more unique points");
        }

        Stack<Point> stack = new Stack<Point>();
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        for (int i = 2; i < sorted.size(); i++) {

            Point head = sorted.get(i);
            Point middle = stack.pop();
            Point tail = stack.pop();

            Turn turn = getTurn(tail, middle, head);

            stack.push(tail);

            if(turn == Turn.COUNTER_CLOCKWISE) {
                stack.push(middle);
                stack.push(head);
            }
            else if(turn == Turn.COLLINEAR) {
                stack.push(head);
            }
            else {
                i--;
            }
        }

        // close the hull
        stack.push(sorted.get(0));

        return new ArrayList<Point>(stack);
    }

    protected static Point getLowestPoint(List<Point> points) {

        Point lowest = points.get(0);

        for(int i = 1; i < points.size(); i++) {

            Point temp = points.get(i);

            if(temp.y < lowest.y || (temp.y == lowest.y && temp.x < lowest.x)) {
                lowest = temp;
            }
        }

        return lowest;
    }

    protected static Set<Point> getSortedPointSet(List<Point> points) {

        final Point lowest = getLowestPoint(points);

        TreeSet<Point> set = new TreeSet<Point>(new Comparator<Point>() {
            @Override
            public int compare(Point a, Point b) {

                if(a == b || a.equals(b)) {
                    return 0;
                }

                // use longs to guard against int-underflow
                double thetaA = Math.atan2((long)a.y - lowest.y, (long)a.x - lowest.x);
                double thetaB = Math.atan2((long)b.y - lowest.y, (long)b.x - lowest.x);

                if(thetaA < thetaB) {
                    return -1;
                }
                else if(thetaA > thetaB) {
                    return 1;
                }
                else {
                    // collinear with the 'lowest' point, let the point closest to it come first

                    // use longs to guard against int-over/underflow
                    double distanceA = Math.sqrt((((long)lowest.x - a.x) * ((long)lowest.x - a.x)) +
                                                (((long)lowest.y - a.y) * ((long)lowest.y - a.y)));
                    double distanceB = Math.sqrt((((long)lowest.x - b.x) * ((long)lowest.x - b.x)) +
                                                (((long)lowest.y - b.y) * ((long)lowest.y - b.y)));

                    if(distanceA < distanceB) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });

        set.addAll(points);

        return set;
    }

    protected static Turn getTurn(Point a, Point b, Point c) {

        // use longs to guard against int-over/underflow
        long crossProduct = (((long)b.x - a.x) * ((long)c.y - a.y)) -
                            (((long)b.y - a.y) * ((long)c.x - a.x));

        if(crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        }
        else if(crossProduct < 0) {
            return Turn.CLOCKWISE;
        }
        else {
            return Turn.COLLINEAR;
        }
    }
}
