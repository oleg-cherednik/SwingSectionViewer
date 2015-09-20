package cop.swing.utils.pool;

import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public abstract class AbstractPool<T> implements Pool<T> {
    protected AbstractPool() {
    }

    @NotNull
    protected abstract T create();

    protected abstract T pop();

    protected abstract boolean push(T obj);

    // ========== Pool ==========

    @Override
    public final T acquire() {
        T resource = pop();

        if (resource == null)
            resource = create();

        if (resource == null)
            throw new NullPointerException("Can't acquire resource");

        return activate(resource);
    }

    @Override
    public final Collection<T> acquire(int count) {
        if (count <= 0)
            return Collections.emptyList();

        List<T> resources = new ArrayList<T>(count);

        while (count > 0 && size() > 0) {
            T resource = pop();

            if (resource == null)
                throw new NullPointerException("Expect not empty resource");

            resources.add(resource);
            count--;
        }

        while (count > 0) {
            resources.add(create());
            count--;
        }

        for (T resource : resources)
            activate(resource);

        return resources;
    }

    @Override
    public final boolean release(T resource) {
        return resource != null && push(passivate(resource));
    }

    @Override
    public final int release(Collection<T> resources) {
        if (CollectionUtils.isEmpty(resources))
            return 0;

        boolean success = true;
        int count = 0;
        Iterator<T> it = resources.iterator();

        while (it.hasNext() && success) {
            T resource = it.next();
            passivate(resource);
            success = push(resource);

            if (success)
                count++;
        }

        return count;
    }

    // ========== Object ==========

    @Override
    public String toString() {
        return "size=" + size();
    }

    // ========== static ==========

    private static <T> T activate(T obj) {
        if (obj instanceof Poolable)
            ((Poolable)obj).activate();
        return obj;
    }

    private static <T> T passivate(T obj) {
        if (obj instanceof Poolable)
            ((Poolable)obj).passivate();
        return obj;
    }
}
