package com.dwcloud.task;

public class TaskEntrance {

    private TaskManager taskManager;

    public String startTask(String taskId) {
        taskManager.startTask(taskId);
        return "任务 " + taskId + " 已启动";
    }

    public String stopTask(String taskId) {
        taskManager.stopTask(taskId);
        return "任务 " + taskId + " 已停止";
    }

    public static void main(String[] args) throws InterruptedException {
        TaskEntrance controller = new TaskEntrance();
        controller.taskManager = new TaskManager();
        controller.startTask("task1");
        Thread.sleep(1000 * 60 * 1);
        controller.stopTask("task1");
    }
}
