package marco.farinetti.taskmanager.model;

public class Task {

  private final int pid;
  private final Priority priority;
  private final long creationTime = System.currentTimeMillis();

  public Task(int pid, Priority priority) {
    this.pid = pid;
    this.priority = priority;
  }

  public int getPid() {
    return pid;
  }

  public Priority getPriority() {
    return priority;
  }

  public long getCreationTime() {
    return creationTime;
  }

  @Override
  public String toString() {
    return "Task{" +
        "pid=" + pid +
        ", priority=" + priority +
        "}";
  }
}
