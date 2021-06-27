package marco.farinetti.taskmanager.spi;

import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.SortingParam;
import marco.farinetti.taskmanager.model.Task;

public interface TaskManager {

  Task add(Priority priority);

  void list(SortingParam param);

  void killByPid(int pid);

  void killByPrio(Priority priority);

  void killAll();

}
