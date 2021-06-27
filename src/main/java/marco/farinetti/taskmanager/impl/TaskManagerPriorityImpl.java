package marco.farinetti.taskmanager.impl;

import java.util.Comparator;
import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.Task;

public class TaskManagerPriorityImpl extends TaskManagerImpl {

  @Override
  public Task add(Priority priority) {
    Integer pid = pidGenerator.getAndIncrement();
    Task task = new Task(pid, priority);
    addTaskByPriority(task);
    return task;
  }

  private synchronized void addTaskByPriority(Task task) {
    if (!tasks.offer(task)) {
      System.out.println("Maximum capacity reached - removing lowest priority task if present");
      //Priorities: HIGH 3, MEDIUM 2, LOW 1 -> remove if existing.prio < task.prio
      tasks.stream()
          .filter(t -> t.getPriority().compareTo(task.getPriority()) < 0)
          .min(Comparator.comparing(Task::getCreationTime))
          .ifPresent(e -> tasks.remove(e));
      tasks.offer(task);
    }
  }

}
