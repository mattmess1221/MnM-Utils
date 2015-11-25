package mnm.mods.util.config;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class ValueList<T> extends Value<List<T>> implements Iterable<T> {

    public ValueList() {
        set(Lists.<T> newArrayList());
    }

    public void add(T value) {
        this.get().add(value);
    }

    public void add(int index, T value) {
        this.get().add(index, value);
    }

    public void remove(Object value) {
        this.get().remove(value);
    }

    public void remove(int index) {
        this.get().remove(index);
    }

    public void clear() {
        this.get().clear();
    }

    public T get(int index) {
        return this.get().get(index);
    }

    @Override
    public void set(List<T> val) {
        super.set(Lists.newArrayList(val));
    }

    @Override
    public Iterator<T> iterator() {
        return get().iterator();
    }
}
