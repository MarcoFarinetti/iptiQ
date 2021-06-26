package marco.farinetti.taskmanager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import marco.farinetti.taskmanager.impl.TaskManagerImpl;
import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.SortingParam;
import marco.farinetti.taskmanager.spi.TaskManager;

public class Application {

  public static void main(String[] args) {
    ExecutorService threadpool = Executors.newCachedThreadPool();

    TaskManager tm = new TaskManagerImpl();
    tm.add(Priority.HIGH);
    tm.add(Priority.MEDIUM);
    tm.add(Priority.LOW);
    tm.add(Priority.HIGH);
    Future<?> submit1 = threadpool.submit(() -> tm.add(Priority.HIGH));
    Future<?> submit2 = threadpool.submit(() -> tm.add(Priority.HIGH));

    tm.list(SortingParam.CREATION);
    System.out.println();
    tm.list(SortingParam.PID);
    System.out.println();
    tm.list(SortingParam.PRIORITY);

    try {
      submit1.get();
      submit2.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    tm.list(SortingParam.CREATION);
    threadpool.shutdown();
  }
}
