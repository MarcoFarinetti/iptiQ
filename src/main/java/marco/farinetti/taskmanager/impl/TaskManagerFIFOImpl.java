package marco.farinetti.taskmanager.impl;

import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.Task;

public class TaskManagerFIFOImpl extends TaskManagerImpl {

  @Override
  public Task add(Priority priority) {
    Integer pid = pidGenerator.getAndIncrement();
    Task task = new Task(pid, priority);
    addTaskFIFO(task);
    return task;
  }

  private synchronized void addTaskFIFO(Task task) {
    if (!tasks.offer(task)) {
      System.out.println("Maximum capacity reached - removing oldest task");
      tasks.remove();
      tasks.offer(task);
    }
  }

}
