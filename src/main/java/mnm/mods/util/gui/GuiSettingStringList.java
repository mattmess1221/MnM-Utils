package mnm.mods.util.gui;

import java.util.List;

import mnm.mods.util.SettingList;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class GuiSettingStringList extends GuiSetting<List<String>> {

    public GuiSettingStringList(SettingList<String> setting, char split) {
        super(setting, new GuiStringList(split));
    }

    public GuiSettingStringList(SettingList<String> setting) {
        this(setting, ' ');
    }

    @Override
    public List<String> getValue() {
        return getList().getValue();
    }

    @Override
    public void setValue(List<String> value) {
        getList().setValue(value);
    }

    public GuiStringList getList() {
        return (GuiStringList) getInput();
    }

    public static class GuiStringList extends GuiComponent implements IGuiInput<List<String>> {

        private GuiText text;
        private char split;

        public GuiStringList(char split) {
            this.text = new GuiText();
            this.split = split;
            wrap(text);
        }

        @Override
        public List<String> getValue() {
            return Splitter.on(split).omitEmptyStrings().splitToList(text.getValue());
        }

        @Override
        public void setValue(List<String> value) {
            text.setValue(Joiner.on(split).skipNulls().join(value));
        }
    }
}
