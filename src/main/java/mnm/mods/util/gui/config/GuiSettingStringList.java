package mnm.mods.util.gui.config;

import java.util.List;

import mnm.mods.util.config.SettingList;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.gui.IGuiInput;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class GuiSettingStringList extends GuiSetting<List<String>> {

    public GuiSettingStringList(SettingList<String> setting, String split, String join) {
        super(setting, new GuiStringList(split, join));
    }

    public GuiSettingStringList(SettingList<String> setting, String split) {
        this(setting, split, split);
    }

    public GuiSettingStringList(SettingList<String> setting) {
        this(setting, ",", ", ");
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
        private String split;
        private String join;

        public GuiStringList(String split, String join) {
            this.text = new GuiText();
            this.split = split;
            this.join = join;
            wrap(text);
        }

        @Override
        public List<String> getValue() {
            return Splitter.on(split).omitEmptyStrings().trimResults().splitToList(text.getValue());
        }

        @Override
        public void setValue(List<String> value) {
            text.setValue(Joiner.on(join).skipNulls().join(value));
        }
    }
}
