package mnm.mods.util.config;

class SettingSubtype<T, V> extends SettingValue<T> {

    private final Class<V> type;

    public SettingSubtype(Class<V> type, T defaultValue) {
        super(defaultValue);
        this.type = type;
    }

    public Class<V> getType() {
        return type;
    }

}
