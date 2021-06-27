package marco.farinetti.taskmanager.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.SortingParam;
import marco.farinetti.taskmanager.model.Task;
import marco.farinetti.taskmanager.spi.TaskManager;
import org.junit.jupiter.api.Test;

class TaskManagerImplTest {

  @Test
  void testAddWithMaxCapacity() {
    TaskManager tm = new TaskManagerImpl();

    Task t1 = tm.add(Priority.LOW);
    Task t2 = tm.add(Priority.LOW);
    Task t3 = tm.add(Priority.LOW);
    Task t4 = tm.add(Priority.LOW);

    assertThat(
        ((TaskManagerImpl) tm).getSortedList(SortingParam.CREATION),
        containsInAnyOrder(t1, t2, t3));
  }

  @Test
  void testKills() {
    TaskManager tm = new TaskManagerImpl(5);

    Task t1 = tm.add(Priority.LOW);
    Task t2 = tm.add(Priority.MEDIUM);
    Task t3 = tm.add(Priority.HIGH);
    Task t4 = tm.add(Priority.LOW);
    Task t5 = tm.add(Priority.MEDIUM);

    tm.killByPid(t2.getPid());
    assertThat(
        ((TaskManagerImpl) tm).getSortedList(SortingParam.CREATION),
        containsInAnyOrder(t1, t3, t4, t5));

    tm.killByPrio(Priority.LOW);
    assertThat(
        ((TaskManagerImpl) tm).getSortedList(SortingParam.CREATION),
        containsInAnyOrder(t3, t5));

    tm.killAll();
    assertThat(
        ((TaskManagerImpl) tm).getSortedList(SortingParam.CREATION),
        is(empty()));

  }

}