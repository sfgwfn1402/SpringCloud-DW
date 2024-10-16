package com.dwcloud.task;

import java.util.concurrent.atomic.AtomicBoolean;

public class DataProcessingTask implements Runnable {
    private final String taskId;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public DataProcessingTask(String taskId) {
        this.taskId = taskId;
    }

    @Override
 public void run() {
        running.set(true);
        while (running.get()) {
            // 模拟数据处理
            System.out.println("任务 " + taskId + " 正在处理数据...");
            try {
                Thread.sleep(1000); // 模拟处理时间
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("任务 " + taskId + " 数据处理已停止");
    }

    public void stop() {
        running.set(false);
    }

    public String getTaskId() {
        return taskId;
    }
}
