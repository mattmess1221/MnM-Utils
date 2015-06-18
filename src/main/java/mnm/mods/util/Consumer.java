package mnm.mods.util;

/**
 * Recreation of java 8's {@link java.util.function.Consumer}
 *
 * @param <T> The type to consume
 */
public interface Consumer<T> {

    void apply(T t);
}
