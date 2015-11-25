package mnm.mods.util.config;

public abstract class ValueObject {

    public static <T> Value<T> value(T t) {
        return new Value<T>(t);
    }

    public static <T> ValueList<T> list() {
        return new ValueList<T>();
    }

    public static <T> ValueMap<T> map() {
        return new ValueMap<T>();
    }

}
