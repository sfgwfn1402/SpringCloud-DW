package com.dwcloud.arithmetic.a;


/**
 * 坐标工具
 *
 * @author linyang
 * @since 2019.11.25
 */
public class LocationUtils {

    /**
     * 地球半径
     */
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两点间距离( 单位：米 )
     *
     * @return
     */
    public static double getDistance(Coordinate p, Coordinate q) {
        double lat1 = p.y;
        double lng1 = p.x;
        double lat2 = q.y;
        double lng2 = q.x;
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }


    /**
     * 点到直线的最短距离的判断 点（x0,y0） 到由两点组成的线段（x1,y1） ,( x2,y2 )    <br>
     * ( 单位：米 )
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x0
     * @param y0
     * @return
     */
    public static double pointToLine(double x1, double y1, double x2, double y2, double x0, double y0) {
        double space;
        double a, b, c;
        a = getDistance(y1, x1, y2, x2);// 线段的长度
        b = getDistance(y1, x1, y0, x0);// (x1,y1)到点的距离
        c = getDistance(y2, x2, y0, x0);// (x2,y2)到点的距离
        if (c <= 0.000001 || b <= 0.000001) {
            space = 0;
            return space;
        }
        if (a <= 0.000001) {
            space = b;
            return space;
        }
        if (c * c >= a * a + b * b) {
            space = b;
            return space;
        }
        if (b * b >= a * a + c * c) {
            space = c;
            return space;
        }
        double p = (a + b + c) / 2;// 半周长
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
        space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
        return space;
    }

//    /**
//     * 判断点是否在两点组成的直线之间
//     * @param p1
//     * @param p2
//     * @param p3  第三点
//     * @return
//     */
//    public static boolean checkPointInTwoPoint(Point p1, Point p2, Point p3){
//        return getDistance(p1.getLat(),p1.getLon(), p3.getLat(), p3.getLon()) + getDistance(p2.getLat(), p2.getLon(), p3.getLat(), p3.getLon()) == getDistance(p1.getLat(), p1.getLon(), p2.getLat(), p2.getLon());
//    }
//
    public static void main(String[] args) {
        System.out.println( getDistance( 26.8279465, 112.5753702, 26.82775007, 112.5751612 ) );
//        double v = pointToLine(116.73446059459238, 40.19924629523467, 116.73335111881217, 40.19925026752042, 116.7356380,40.1998646);
//        System.out.println("v = " + v);
//        double v1 = pointToLine(116.73335111881217, 40.19925026752042, 116.73446059459238, 40.19924629523467, 116.73348284, 40.19925215);
//        System.out.println("Double.isNaN(v1) = " + Double.isNaN(v1));
//        System.out.println( pointToLine( 116.73446059459238,40.19924629523467,  116.73335111881217,40.19925026752042, 116.73348284,40.19925215 ) );
//        System.out.println( pointToLine( 116.73335111881217,40.19925026752042,116.73446059459238,40.19924629523467,   116.73348284,40.19925215 ) );

//        boolean b = checkPointInTwoPoint(new Point(112.5766432, 26.81934331), new Point(112.57682878, 26.81947632), new Point(112.57672340, 26.81942750));
//        System.out.println("b = " + b);


    }


}