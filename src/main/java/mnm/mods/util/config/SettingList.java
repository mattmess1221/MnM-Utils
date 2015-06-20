package mnm.mods.util.config;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class SettingList<T> extends SettingSubtype<List<T>, T> implements Iterable<T> {

    public SettingList(Class<T> type, T... t) {
        this(type, Lists.newArrayList(t));
    }

    public SettingList(Class<T> type, Iterable<T> coll) {
        super(type, list_(coll));
    }

    private static <T> List<T> list_(Iterable<T> ts) {
        return Lists.newArrayList(ts);
    }

    public void add(T value) {
        this.value.add(value);
    }

    public void add(int index, T value) {
        this.value.add(index, value);
    }

    public void remove(Object value) {
        this.value.remove(value);
    }

    public void remove(int index) {
        this.value.remove(index);
    }

    public void clear() {
        this.value.clear();
    }

    public T get(int index) {
        return this.value.get(index);
    }

    /**
     * Returns an immutable list of the value.
     *
     * @return An immutable list
     */
    @Override
    public List<T> getValue() {
        // Don't modify the list manually.
        return ImmutableList.copyOf(value);
    }

    /**
     * Returns an immutable list of the defaults.
     *
     * @return an immutable list
     */
    @Override
    public List<T> getDefaultValue() {
        // don't modify the defaults
        return ImmutableList.copyOf(super.getDefaultValue());
    }

    @Override
    public Iterator<T> iterator() {
        return value.iterator();
    }
}
