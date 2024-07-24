package com.dwcloud.arithmetic;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.io.WKTReader;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MapUtils {
    public static final class MapU {
        public static final double EARTH_RADIUS = 6378.137;
        public static final int N_2 = 2;
        public static final int N_1000 = 1000;
        public static final double N_180 = 180.0;
    }

    public static void main(String[] args) throws Exception {
        // 测试--点是否在电子围栏中
        String fenceWkt = "POLYGON((112.575583508555255 26.828531479972828,112.576006633831994 26.828509210221419,112.575699311262582 26.828380045663259,112.57595318642862 26.828375591712977))";
        double lon = 112.5757839;
        double lat = 26.8284513;
        boolean isInFence = isPointInFence(lon, lat, fenceWkt);
        System.out.println(isInFence);
    }

    /**
     * 判断点是否在多边形图形地图中
     *
     * @param lon
     * @param lat
     * @param fenceWkt
     * @return
     * @throws Exception
     */
    public static boolean isPointInFence(double lon, double lat, String fenceWkt) throws Exception {
        GeometryFactory gf = new GeometryFactory();
        WKTReader reader = new WKTReader(gf);

        // 解析电子围栏的边界点列表
        org.locationtech.jts.geom.Coordinate[] coords = parseCoordinates(fenceWkt);
        LinearRing ring = new LinearRing(new CoordinateArraySequence(coords), gf);
        Polygon fence = new Polygon(ring, null, gf);

        // 创建目标点
        Point point = gf.createPoint(new org.locationtech.jts.geom.Coordinate(lon, lat));

        // 判断点是否在围栏内
        return fence.contains(point);
    }

    // 解析WKT格式的坐标串
    private static org.locationtech.jts.geom.Coordinate[] parseCoordinates(String wkt) throws Exception {
        WKTReader reader = new WKTReader();
        Geometry geom = reader.read(wkt);
        return geom.getCoordinates();
    }

    /**
     * 判断一个点是否在圆形区域内
     *
     * @param pointLon  判断点经度
     * @param pointLat  判断点维度
     * @param centerLon 圆心经度
     * @param centerLat 圆心维度
     * @param radius    半径
     * @return boolean
     */
    public static boolean isInCircle(double pointLon, double pointLat, double centerLon, double centerLat, double radius) {

        return getDistance(pointLon, pointLat, centerLon, centerLat) < radius;
    }

    /**
     * 判断是否在多边形区域内
     *
     * @param pointLon 要判断的点的经度
     * @param pointLat 要判断的点的维度
     * @param lon      区域各顶点的经度数组
     * @param lat      区域各顶点的维度数组
     * @return
     */
    public static boolean isInPolygon(double pointLon, double pointLat, double[] lon, double[] lat) {
        // 将要判断的横纵坐标组成一个点
        Point2D.Double point = new Point2D.Double(pointLon, pointLat);
        // 将区域各顶点的横纵坐标放到一个点集合里面
        List<Point2D.Double> pointList = new ArrayList<>();
        double polygonPoint_x, polygonPoint_y;
        for (int i = 0; i < lon.length; i++) {
            polygonPoint_x = lon[i];
            polygonPoint_y = lat[i];
            Point2D.Double polygonPoint = new Point2D.Double(polygonPoint_x, polygonPoint_y);
            pointList.add(polygonPoint);
        }
        return areaCheck(point, pointList);
    }

    /**
     * 判断是否在多边形区域内
     *
     * @param point   要判断的点
     * @param polygon 区域点集合
     * @return
     */
    public static boolean areaCheck(Point2D.Double point, List<Point2D.Double> polygon) {
        java.awt.geom.GeneralPath generalPath = new java.awt.geom.GeneralPath();
        Point2D.Double first = polygon.get(0);
        // 通过移动到指定坐标（以双精度指定），将一个点添加到路径中
        generalPath.moveTo(first.x, first.y);
        polygon.remove(0);
        for (Point2D.Double d : polygon) {
            // 通过绘制一条从当前坐标到新指定坐标（以双精度指定）的直线，将一个点添加到路径中。
            generalPath.lineTo(d.x, d.y);
        }
        // 将几何多边形封闭
        generalPath.lineTo(first.x, first.y);
        generalPath.closePath();
        // 测试指定的 Point2D 是否在 Shape 的边界内。
        return generalPath.contains(point);
    }

    /**
     * 获取两个点间的距离
     *
     * @param lonA1 A1点的经度
     * @param latA1 A1点的纬度
     * @param lonA2 A2点的经度
     * @param latA2 A2点的纬度
     * @return
     */
    public static double getDistance(double lonA1, double latA1, double lonA2, double latA2) {
        // 单位(米)
        double lon1 = lonA1 * Math.PI / MapU.N_180;
        double lat1 = latA1 * Math.PI / MapU.N_180;
        double lon2 = lonA2 * Math.PI / MapU.N_180;
        double lat2 = latA2 * Math.PI / MapU.N_180;
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / MapU.N_2), MapU.N_2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / MapU.N_2), MapU.N_2);
        double c = MapU.N_2 * Math.asin(Math.sqrt(a));
        return c * MapU.EARTH_RADIUS * MapU.N_1000;
    }
}
