package marco.farinetti.taskmanager.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.SortingParam;
import marco.farinetti.taskmanager.model.Task;
import marco.farinetti.taskmanager.spi.TaskManager;
import org.junit.jupiter.api.Test;

class TaskManagerPriorityImplTest {

  @Test
  void testAddWithPriority() {
    TaskManager tmPrio = new TaskManagerPriorityImpl();

    Task t1 = tmPrio.add(Priority.HIGH);
    Task t2 = tmPrio.add(Priority.MEDIUM);
    Task t3 = tmPrio.add(Priority.MEDIUM);
    Task t4 = tmPrio.add(Priority.LOW);
    Task t5 = tmPrio.add(Priority.HIGH);

    assertThat(
        ((TaskManagerPriorityImpl) tmPrio).getSortedList(SortingParam.CREATION),
        containsInAnyOrder(t1, t3, t5));
  }

}