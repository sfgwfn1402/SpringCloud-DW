package com.dwcloud.arithmetic.a;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 获取凸多边形坐标点列表
 * 提供内容：提供多个坐标点，通过工具获得一个凸多边形坐标点列表
 */
public final class ConvexHull {

    /*返回一个新的点列表，该列表表示
    给定的一组点。凸壳不包括共线点。
    该算法在O（n logn）时间内运行*/
    public static List<Point> makeHull(List<Point> points) {
        List<Point> newPoints = new ArrayList<>(points);
        Collections.sort(newPoints);
        return makeHullPresorted(newPoints);
    }

    public static void main(String[] args) {
        List<Point> l = Lists.newArrayList();
        l.add(new Point(112.5745877154, 26.828280534));
        l.add(new Point(112.5746465541, 26.828327605));
        l.add(new Point(112.5747210831, 26.828298186));
        l.add(new Point(112.5746661670, 26.828292302));
        l.add(new Point(112.5746955863, 26.828249154));
        l.add(new Point(112.5746602831, 26.828266805));
        l.add(new Point(112.5746387089, 26.828258960));
        l.add(new Point(112.5746171347, 26.828239347));
        l.add(new Point(112.5746249799, 26.828202083));
        l.add(new Point(112.5746740121, 26.828206005));
        l.add(new Point(112.5747387347, 26.828206005));
        l.add(new Point(112.5747406960, 26.828258960));
        l.add(new Point(112.5747838444, 26.828255038));

        List<Point> points = makeHull(l);
        for (int i = 0; i < points.size(); i++) {
            System.out.println(points.get(i).x + "," + points.get(i).y);
        }

    }


    // 返回凸面外壳，假设每个点[i]<=点[i+1]。在O（n）时间内运行。
    public static List<Point> makeHullPresorted(List<Point> points) {
        if (points.size() <= 1) return new ArrayList<>(points);

        // 安德鲁算法（凸包）。正y坐标对应于“向上”
        //按照数学惯例，而不是按照计算机的“向下”
        //图形约定。这并不影响结果的正确性。

        List<Point> upperHull = new ArrayList<>();
        for (Point p : points) {
            while (upperHull.size() >= 2) {
                Point q = upperHull.get(upperHull.size() - 1);
                Point r = upperHull.get(upperHull.size() - 2);
                if ((q.x - r.x) * (p.y - r.y) >= (q.y - r.y) * (p.x - r.x)) upperHull.remove(upperHull.size() - 1);
                else break;
            }
            upperHull.add(p);
        }
        upperHull.remove(upperHull.size() - 1);

        List<Point> lowerHull = new ArrayList<>();
        for (int i = points.size() - 1; i >= 0; i--) {
            Point p = points.get(i);
            while (lowerHull.size() >= 2) {
                Point q = lowerHull.get(lowerHull.size() - 1);
                Point r = lowerHull.get(lowerHull.size() - 2);
                if ((q.x - r.x) * (p.y - r.y) >= (q.y - r.y) * (p.x - r.x)) lowerHull.remove(lowerHull.size() - 1);
                else break;
            }
            lowerHull.add(p);
        }
        lowerHull.remove(lowerHull.size() - 1);

        if (!(upperHull.size() == 1 && upperHull.equals(lowerHull))) upperHull.addAll(lowerHull);
        return upperHull;
    }

}


final class Point implements Comparable<Point> {

    public final double x;
    public final double y;


    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public String toString() {
        return "Point(" + x + "," + y + ")";
    }


    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) return false;
        else {
            Point other = (Point) obj;
            return x == other.x && y == other.y;
        }
    }


    public int hashCode() {
        return Objects.hash(x, y);
    }


    public int compareTo(Point other) {
        if (x != other.x) return Double.compare(x, other.x);
        else return Double.compare(y, other.y);
    }

}