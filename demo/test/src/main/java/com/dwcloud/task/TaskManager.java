package com.dwcloud.task;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskManager {
    private final Map<String, DataProcessingTask> tasks = new ConcurrentHashMap<>();
    private final Map<String, Thread> taskThreads = new ConcurrentHashMap<>();

    public synchronized void startTask(String taskId) {
        if (!tasks.containsKey(taskId)) {
            DataProcessingTask task = new DataProcessingTask(taskId);
            tasks.put(taskId, task);
            Thread taskThread = new Thread(task);
            taskThreads.put(taskId, taskThread);
            taskThread.start();
        }
    }

    public synchronized void stopTask(String taskId) {
        DataProcessingTask task = tasks.get(taskId);
        if (task != null) {
            task.stop();
            Thread taskThread = taskThreads.get(taskId);
            if (taskThread != null && taskThread.isAlive()) {
                try {
                    taskThread.join(); // 等待线程结束
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            tasks.remove(taskId);
            taskThreads.remove(taskId);
        }
    }
}
