package marco.farinetti.taskmanager;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import marco.farinetti.taskmanager.impl.TaskManagerFIFOImpl;
import marco.farinetti.taskmanager.impl.TaskManagerImpl;
import marco.farinetti.taskmanager.impl.TaskManagerPriorityImpl;
import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.SortingParam;
import marco.farinetti.taskmanager.model.Task;
import marco.farinetti.taskmanager.spi.TaskManager;

public class Application {

  public static void main(String[] args) {
    ExecutorService threadpool = Executors.newCachedThreadPool();

    TaskManager tm = new TaskManagerImpl();
    TaskManager tmFifo = new TaskManagerFIFOImpl();
    TaskManager tmPrio = new TaskManagerPriorityImpl();

    tm.add(Priority.HIGH);
    tm.add(Priority.MEDIUM);
    tm.add(Priority.LOW);

    tmFifo.add(Priority.LOW);
    tmFifo.add(Priority.LOW);
    tmFifo.add(Priority.LOW);

    tmPrio.add(Priority.HIGH);
    tmPrio.add(Priority.LOW);
    tmPrio.add(Priority.MEDIUM);

    Callable<Task> taskCallable1 = () -> tm.add(Priority.HIGH);
    Future<?> submit1 = threadpool.submit(taskCallable1);
    Future<?> submit2 = threadpool.submit(taskCallable1);
    Callable<Task> taskCallable2 = () -> tmFifo.add(Priority.HIGH);
    Future<?> submit3 = threadpool.submit(taskCallable2);
    Future<?> submit4 = threadpool.submit(taskCallable2);
    Callable<Task> taskCallable3 = () -> tmPrio.add(Priority.HIGH);
    Future<?> submit5 = threadpool.submit(taskCallable3);
    Future<?> submit6 = threadpool.submit(taskCallable3);

    try {
      submit1.get();
      submit2.get();
      submit3.get();
      submit4.get();
      submit5.get();
      submit6.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    System.out.print("\nTM Default: ");
    tm.list(SortingParam.CREATION);
    System.out.print("\nTM FIFO: ");
    tmFifo.list(SortingParam.CREATION);
    System.out.print("\nTM Prio: ");
    tmPrio.list(SortingParam.CREATION);

    threadpool.shutdown();
  }
}
