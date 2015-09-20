package cop.swing.utils.pool;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public interface Poolable {
    void activate();

    void passivate();
}
