package com.dwcloud.arithmetic.a;

import java.util.ArrayList;
import java.util.List;

/**
 * 经纬度轨迹相似度计算
 */
public class LCSS {
    private List<Coordinate> l1;
    private List<Coordinate> l2;
    private List<Coordinate> lcs = new ArrayList<>();

    public boolean isNearby(Coordinate a, Coordinate b) {
        double distance = LocationUtils.getDistance(a, b);
        //阈值：米
        int f = 5;
        if (distance < f) {
            System.out.println("满足要求，两点距离:" + distance + "米" + ",阈值:" + f + "米" + ", 坐标:" + a.x + "," + a.y + ";" + b.x + "," + b.y);
            return true;
        }
        System.out.println("不满足要求，两点距离:" + distance + "米" + ",阈值:" + f + "米" + ", 坐标:" + a.x + "," + a.y + ";" + b.x + "," + b.y);
        return false;
    }

    public void printLcs(int[][] flag, List<Coordinate> a, int i, int j) {
        if (i == 0 || j == 0) return;
        if (flag[i][j] == 1) {
            printLcs(flag, a, i - 1, j - 1);
            lcs.add(a.get(i - 1));
        } else if (flag[i][j] == 2) {
            printLcs(flag, a, i, j - 1);
        } else {
            printLcs(flag, a, i - 1, j);
        }
    }

    public double lcs(List<Coordinate> l1, List<Coordinate> l2) {
        int len1 = l1.size();
        int len2 = l2.size();
        int[][] c = new int[len1 + 1][len2 + 1];
        int[][] flag = new int[len1 + 1][len2 + 1];
        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                if (isNearby(l1.get(i), l2.get(j))) {
                    c[i + 1][j + 1] = c[i][j] + 1;
                    flag[i + 1][j + 1] = 1;//1='ok'
                } else if (c[i + 1][j] > c[i][j + 1]) {
                    c[i + 1][j + 1] = c[i + 1][j];
                    flag[i + 1][j + 1] = 2;//2='left'
                } else {
                    c[i + 1][j + 1] = c[i][j + 1];
                    flag[i + 1][j + 1] = 3;//3='up'
                }
            }
        }
        printLcs(flag, l1, len1, len2);
        //归一化处理
        double similarity = (lcs.size() * 1.0 / Math.min(len1, len2));
        return Double.parseDouble(String.format("%.3f", similarity));
    }

    public static void main(String[] args) {
        List<Coordinate> l1 = new ArrayList<>();
        //这是右转
        l1.add(new Coordinate(112.5752143, 26.82763782));
        l1.add(new Coordinate(112.5752174, 26.82768459));
        l1.add(new Coordinate(112.5752361, 26.82774228));
        l1.add(new Coordinate(112.5752579, 26.82779684));
        l1.add(new Coordinate(112.5752953, 26.82788258));
        l1.add(new Coordinate(112.5753452, 26.82790909));
        l1.add(new Coordinate(112.5753702, 26.8279465));
        l1.add(new Coordinate(112.5754107, 26.82797145));
        List<Coordinate> l2 = new ArrayList<>();
        //这是直行
        l2.add(new Coordinate(112.5751799, 26.82768615));
        l2.add(new Coordinate(112.5751612, 26.82775007));
        l2.add(new Coordinate(112.5751425, 26.82780307));
        l2.add(new Coordinate(112.5751253, 26.82784828));
        l2.add(new Coordinate(112.5751035, 26.82789973));
        l2.add(new Coordinate(112.5750801, 26.82791844));
        l2.add(new Coordinate(112.5750458, 26.82796053));
        l2.add(new Coordinate(112.5750287, 26.82798859));
        l2.add(new Coordinate(112.5750115, 26.82802601));
        l2.add(new Coordinate(112.5750069, 26.82806342));
        //这是右转-1
//        l2.add(new Coordinate(112.5751924, 26.82766432));
//        l2.add(new Coordinate(112.5751987, 26.82769706));
//        l2.add(new Coordinate(112.575208, 26.82774539));
//        l2.add(new Coordinate(112.5752252, 26.82779216));
//        l2.add(new Coordinate(112.5752891, 26.82780619));
//        l2.add(new Coordinate(112.5752969, 26.82784361));
//        l2.add(new Coordinate(112.5753218, 26.82786699));
//        l2.add(new Coordinate(112.5753218, 26.82792));
//        l2.add(new Coordinate(112.5753514, 26.82796053));
//        l2.add(new Coordinate(112.5753982, 26.82799015));
//        l2.add(new Coordinate(112.5754497, 26.82799015));
        //这是右转-3
//        l2.add(new Coordinate(112.5752204915, 26.82762067));
//        l2.add(new Coordinate(112.5752189325, 26.82766588));
//        l2.add(new Coordinate(112.5752220504, 26.82769394));
//        l2.add(new Coordinate(112.5752282864, 26.82772045));
//        l2.add(new Coordinate(112.5752173735, 26.82776254));
//        l2.add(new Coordinate(112.5752407583, 26.82778280));
//        l2.add(new Coordinate(112.5752688201, 26.82781710));
//        l2.add(new Coordinate(112.5753015588, 26.82783269));
//        l2.add(new Coordinate(112.5753327386, 26.82788258));
//        l2.add(new Coordinate(112.5753389745, 26.82794962));
//        l2.add(new Coordinate(112.5753701543, 26.82799639));
//        l2.add(new Coordinate(112.5754309548, 26.82801042));
//        l2.add(new Coordinate(112.5754839603, 26.82804160));


        LCSS lcss = new LCSS();
//        System.out.println(lcss.lcs(l1, l2));
        double similarity = lcss.lcs(l1, l2);
        System.out.printf("Similarity: %.3f%n", similarity);

    }
}