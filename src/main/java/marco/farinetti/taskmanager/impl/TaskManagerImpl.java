package marco.farinetti.taskmanager.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.SortingParam;
import marco.farinetti.taskmanager.model.Task;
import marco.farinetti.taskmanager.spi.TaskManager;

public class TaskManagerImpl implements TaskManager {

  protected final static int CAPACITY = 3;
  protected final AtomicInteger pidGenerator = new AtomicInteger(0);
  protected BlockingQueue<Task> tasks;

  public TaskManagerImpl() {
    this.tasks = new LinkedBlockingQueue<>(CAPACITY);
  }

  //For testing purposes
  TaskManagerImpl(int capacity) {
    this.tasks = new LinkedBlockingQueue<>(capacity);
  }

  @Override
  public Task add(Priority priority) {
    Integer pid = pidGenerator.getAndIncrement();
    Task task = new Task(pid, priority);
    addTaskWithMaxCapacity(task);
    return task;
  }

  private void addTaskWithMaxCapacity(Task task) {
    if (!tasks.offer(task)) {
      System.out.println("Could not add new task " + task + " - maximum capacity reached");
    }
  }

  //May reflect transient state
  @Override
  public void list(SortingParam param) {
    System.out.println(getSortedList(param));
  }

  List<Task> getSortedList(SortingParam param) {
    switch (param) {
      case CREATION:
        return tasks.stream()
            .sorted(Comparator.comparingLong(Task::getCreationTime))
            .collect(Collectors.toList());
      case PRIORITY:
        //sorting HIGH to LOW
        return tasks.stream()
            .sorted(Comparator.comparing(Task::getPriority).reversed())
            .collect(Collectors.toList());
      case PID:
        return tasks.stream()
            .sorted(Comparator.comparingInt(Task::getPid))
            .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  @Override
  public void killByPid(int pid) {
    tasks.removeIf(task -> task.getPid() == pid);
  }

  @Override
  public void killByPrio(Priority priority) {
    tasks.removeIf(task -> task.getPriority() == priority);
  }

  @Override
  public void killAll() {
    tasks.clear();
  }

}
