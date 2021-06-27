# Task Manager
Coding exercise for iptiQ EMEA P&C application

The application uses plain Java and Maven for the build. I did not use any framework except Junit for unit testing.

## Solution
The solution offers three implementations of the TaskManager interface, which provides methods to add, list and kill processes.  
The methods `list()`, `killByPid()`, `killByPrio()` and `killAll()` offer the same behavior for all implementations, while the behavior of the `add()` method changes according to the implementation (default, FIFO or priority-based).  
### Thread-safety and considerations
The TaskManager stores the processes by using a BlockingQueue.
Thread safety is guaranteed for `add()` and `kill*()` operations by either using atomic operations or by locking (synchronizing) the methods.
The `list()` method is not thread-safe, hence why, if concurrent modifications to the list are ongoing, it might reflect a transient state.
The reasoning behind this decision is that a `list()` method should not be used for program control but rather for monitoring purposes.
The method could be made thread-safe by synchronizing it but it would come at the cost of a moderate-to-high performance impact, depending on how often the method is called.
The reason why the synchronization would have such an impact is because it would also require the synchronization of `add()` and `kill*()` methods.
