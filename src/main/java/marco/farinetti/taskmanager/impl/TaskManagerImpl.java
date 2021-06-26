package marco.farinetti.taskmanager.impl;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.SortingParam;
import marco.farinetti.taskmanager.model.Task;
import marco.farinetti.taskmanager.spi.TaskManager;

public class TaskManagerImpl implements TaskManager {

  private final static int CAPACITY = 5;
  private final AtomicInteger pidGenerator = new AtomicInteger(0);
  private ConcurrentHashMap<Integer, Task> tasks = new ConcurrentHashMap<>(CAPACITY);

  @Override
  public void add(Priority priority) {
    Integer pid = getPid();
    Task task = new Task(pid, priority);
    addTaskWithMaxCapacity(task);
  }

  //May reflect transient state
  @Override
  public void list(SortingParam param) {
    switch (param) {
      case CREATION:
        tasks.values().stream().sorted(Comparator.comparingLong(Task::getCreationTime))
            .forEachOrdered(System.out::println);
        break;
      case PRIORITY:
        tasks.values().stream().sorted(Comparator.comparing(Task::getPriority))
            .forEachOrdered(System.out::println);
        break;
      case PID:
        tasks.values().stream().sorted(Comparator.comparingInt(Task::getPid))
            .forEachOrdered(System.out::println);
        break;
    }

  }

  @Override
  public void killByPid(int pid) {
    tasks.remove(pid);
  }

  @Override
  public void killByPrio(Priority priority) {
    tasks.entrySet().stream().filter(kv -> kv.getValue().getPriority() == priority)
        .forEachOrdered(kv -> tasks.remove(kv.getKey()));
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
  private synchronized void addTaskWithMaxCapacity(Task task) {
    if (tasks.size() < CAPACITY) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      tasks.put(task.getPid(), task);
      System.out.println("Tasks size is " + tasks.size());
    } else {
      System.out.println("Maximum capacity reached - kill a process before adding a new one");
    }
  }

  Integer getPid() {
    return pidGenerator.getAndIncrement();
  }
}
