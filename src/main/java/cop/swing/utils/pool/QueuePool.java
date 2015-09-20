package cop.swing.utils.pool;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public abstract class QueuePool<T> extends AbstractPool<T> {
    private final Queue<T> queue;

    protected QueuePool() {
        this(new LinkedList<T>());
    }

    protected QueuePool(Queue<T> queue) {
        this.queue = queue;
    }

    // ========== AbstractPool ==========

    @Override
    protected T pop() {
        return queue.poll();
    }

    @Override
    protected boolean push(T obj) {
        return obj != null && queue.offer(obj);
    }

    // ========== Pool ==========

    @Override
    public int size() {
        return queue.size();
    }

    // ========== Object ==========

    @Override
    public String toString() {
        return String.format("%s: %s", queue.getClass().getSimpleName(), super.toString());
    }
}
