package cop.swing.utils.pool;

import java.util.Collection;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public interface Pool<T> {
    T acquire();

    Collection<T> acquire(int count);

    boolean release(T resource);

    int release(Collection<T> resources);

    int size();
}
