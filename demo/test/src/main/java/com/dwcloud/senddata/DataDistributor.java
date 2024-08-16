package com.dwcloud.senddata;

import cn.hutool.core.date.DateUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class DataDistributor {

    // 模拟缓存中的数据点
    private LinkedBlockingQueue<Integer> cache = new LinkedBlockingQueue<>(100);
    private static final int THRESHOLD_PERCENTAGE = 50; // 50% threshold

    // 当前处理的位置
    private static int currentFrame = 0;

    // 是否暂停标志
    private static boolean paused = false;

    // 上一次分发数据的时间戳
    private static long lastDispatchTime = 0;

    // 当前批次的数据数量
    private static final int BATCH_SIZE = 10;

    // 当前批次的数量
    private static int currentBatch = 0;
    public static LinkedBlockingQueue<DataPoint> queue = new LinkedBlockingQueue<>(100);
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        // 模拟外部控制
        simulateExternalControl();
    }

    private static void addData(LinkedBlockingQueue<DataPoint> queue) {
        for (int i = 0; i < BATCH_SIZE; i++) {
            try {
                queue.put(new DataPoint(i, i));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Data addition interrupted.");
            }
        }
    }


    public static void start(int startPercentage, int frameRate) {
        // 添加初始数据
        addData(queue);

        // 计算每帧之间的延迟时间（毫秒）
        long frameInterval = 1000 / frameRate;
        // 主线程休眠一段时间以模拟业务运行
        try {
            while (!paused) {
                long currentTime = System.currentTimeMillis();
                // 计算从上一次分发到现在经过的时间
                long elapsedTime = currentTime - lastDispatchTime;

                // 如果已经达到了下一帧的时间间隔
                if (elapsedTime >= frameInterval) {
                    DataPoint point = queue.take();
                    System.out.println("每秒帧率: " + frameRate + ",时间: " + DateUtil.format(DateUtil.date(), "HH:mm:ss.SSS") + ", 正在分发: " + point);
                    if (queue.size() <= (BATCH_SIZE * THRESHOLD_PERCENTAGE / 100.0)) {
                        // 触发添加更多数据
                        System.out.println("当前缓存数据已经是: " + queue.size() + "条了，所以要加新数据进来了");
                        executor.submit(() -> addData(queue));
                    }
                    // 更新上一次分发的时间
                    lastDispatchTime = currentTime;
                }

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread interrupted.");
        }
    }

    // 分发数据的方法
//    public static void distributeData(int startPercentage, int frameRate) {
//        int totalFrames = cache.size(); // 总共的数据点数量
//        //根据当前开始位置，查询指定位置开始的数据，加入到缓存
//        // 计算每帧之间的延迟时间（毫秒）
//        long frameInterval = 1000 / frameRate;
//
//        while (!paused && currentFrame < totalFrames) {
//            long currentTime = System.currentTimeMillis();
//            // 计算从上一次分发到现在经过的时间
//            long elapsedTime = currentTime - lastDispatchTime;
//
//            // 如果已经达到了下一帧的时间间隔
//            if (elapsedTime >= frameInterval) {
//                DataPoint point = cache.get(currentFrame);
//                System.out.println("每秒帧率: " + frameRate + ",时间: " + DateUtil.format(DateUtil.date(), "HH:mm:ss.SSS") + ", 正在分发: " + point);
//
//                // 更新当前处理的位置
//                currentFrame++;
//
//                // 更新上一次分发的时间
//                lastDispatchTime = currentTime;
//
//                // 检查是否需要添加新的数据批次
//                checkAddNextBatch();
//            }
//        }
//    }

    // 模拟外部控制
    private static void simulateExternalControl() {
        // 假设外部发送的信号
        int startPercentage = 25; // 从25%的位置开始
        start(startPercentage, 50);

        // 模拟外部暂停信号
//        paused = true;
//
//        // 模拟外部恢复信号
//        paused = false;
//        start(startPercentage, 50);
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
