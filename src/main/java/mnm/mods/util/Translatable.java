package mnm.mods.util;

public interface Translatable {

    String getUnlocalized();

    String translate(Object... params); // TODO default method
}
