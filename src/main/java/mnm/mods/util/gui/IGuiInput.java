package mnm.mods.util.gui;

public interface IGuiInput<T> {

    T getValue();

    void setValue(T value);
}
