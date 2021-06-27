package marco.farinetti.taskmanager.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import marco.farinetti.taskmanager.model.Priority;
import marco.farinetti.taskmanager.model.SortingParam;
import marco.farinetti.taskmanager.model.Task;
import marco.farinetti.taskmanager.spi.TaskManager;
import org.junit.jupiter.api.Test;

class TaskManagerFIFOImplTest {

  @Test
  void testAddWithFIFO() {
    TaskManager tmFifo = new TaskManagerFIFOImpl();

    Task t1 = tmFifo.add(Priority.LOW);
    Task t2 = tmFifo.add(Priority.LOW);
    Task t3 = tmFifo.add(Priority.LOW);
    Task t4 = tmFifo.add(Priority.LOW);

    assertThat(
        ((TaskManagerFIFOImpl) tmFifo).getSortedList(SortingParam.CREATION),
        containsInAnyOrder(t2, t3, t4));
  }

}