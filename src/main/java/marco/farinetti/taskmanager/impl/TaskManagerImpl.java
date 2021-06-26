package marco.farinetti.taskmanager.impl;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.SortingParam;
import marco.farinetti.taskmanager.model.Task;
import marco.farinetti.taskmanager.spi.TaskManager;

public class TaskManagerImpl implements TaskManager {

  protected final static int CAPACITY = 2;
  protected final AtomicInteger pidGenerator = new AtomicInteger(0);
  protected BlockingQueue<Task> tasks = new LinkedBlockingQueue<>(CAPACITY);

  @Override
  public void add(Priority priority) {
    Integer pid = pidGenerator.getAndIncrement();
    Task task = new Task(pid, priority);
    addTaskWithMaxCapacity(task);
  }

  //May reflect transient state
  @Override
  public void list(SortingParam param) {
    switch (param) {
      case CREATION:
//        System.out.println(tasks);
        tasks.stream().sorted(Comparator.comparingLong(Task::getCreationTime))
            .forEachOrdered(System.out::println);
        break;
      case PRIORITY:
        System.out.println(tasks.stream().sorted(Comparator.comparing(Task::getPriority))
            .collect(Collectors.toList()));
        break;
      case PID:
//        System.out.println(tasks);
        tasks.stream().sorted(Comparator.comparingInt(Task::getPid))
            .forEachOrdered(System.out::println);
        break;
    }

  }

  @Override
  public void killByPid(int pid) {
    tasks.removeIf(task -> task.getPid() == pid);
  }

  @Override
  public void killByPrio(Priority priority) {
    tasks.stream()
        .filter(e -> e.getPriority() == priority)
        .forEach(e -> tasks.remove(e));
  }

  //For aggregate operations such as putAll and clear, concurrent retrievals may reflect insertion or removal of only some entries.
  @Override
  public void killAll() {
    tasks.clear();
  }

  //The results of aggregate status methods are useful only when a map is not undergoing concurrent updates in other threads.
  //Otherwise the results of these methods reflect transient states.
  //size() is used in this case
  //if any kill action is ongoing, size() might return a larger value than the actual size
  //a possibly larger value will not have an impact on the capacity constraint
  private void addTaskWithMaxCapacity(Task task) {
//    if (tasks.size() < CAPACITY) {
//      try {
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
    if (!tasks.offer(task)) {
      System.out.println("Could not add new task" + task + " - maximum capacity reached");
    }
//      System.out.println("Tasks size is " + tasks.size());
//    } else {
//      System.out.println("Maximum capacity reached - kill a process before adding a new one");
//    }

  }

}
