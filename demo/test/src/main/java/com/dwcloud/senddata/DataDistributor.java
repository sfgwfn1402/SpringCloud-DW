package com.dwcloud.senddata;

import cn.hutool.core.date.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class DataDistributor {

    // 模拟缓存中的数据点
    private static List<DataPoint> cache = new ArrayList<>();

    // 当前处理的位置
    private static int currentFrame = 0;

    // 是否暂停标志
    private static boolean paused = false;

    static {
        // 初始化缓存，添加100个数据点
        for (int i = 0; i < 100; i++) {
            cache.add(new DataPoint(i, i));
        }
    }

    public static void main(String[] args) {
        // 模拟外部控制
        simulateExternalControl();
    }

    // 分发数据的方法
    public static void distributeData(int startPercentage, int frameRate) {
        int totalFrames = 100; // 总共的数据点数量

        // 计算每帧之间的延迟时间（毫秒）
        long delay = 1000 / frameRate;

        // 计算从哪个位置开始
        currentFrame = (int) (totalFrames * startPercentage / 100.0);

        while (!paused && currentFrame < totalFrames) {
            if (currentFrame < totalFrames) {
                DataPoint point = cache.get(currentFrame);
                System.out.println("每秒帧率: " + frameRate + ",时间: " + DateUtil.format(DateUtil.date(), "HH:mm:ss.SSS") + ", 正在分发: " + point);

                try {
                    // 线程暂停指定的时间
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("线程被中断");
                }

                // 更新当前处理的位置
                currentFrame++;
            }
        }
    }

    // 模拟外部控制
    private static void simulateExternalControl() {
        // 假设外部发送的信号
        int startPercentage = 25; // 从25%的位置开始
        distributeData(startPercentage, 10);

        // 模拟外部暂停信号
        paused = true;

        // 模拟外部恢复信号
        paused = false;
        distributeData(startPercentage, 50);
    }

    // 数据点类
    static class DataPoint {
        double longitude;
        double latitude;

        public DataPoint(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        @Override
        public String toString() {
            return "DataPoint{" + "longitude=" + longitude + ", latitude=" + latitude + '}';
        }
    }
}
