package mnm.mods.util;

import java.io.File;

import com.mumfrey.liteloader.LiteMod;

public class LiteModMnmUtils implements LiteMod {

    @Override
    public String getName() {
        return "Mnm Utils";
    }

    @Override
    public String getVersion() {
        return "@VERSION@";
    }

    @Override
    public void init(File arg0) {}

    @Override
    public void upgradeSettings(String arg0, File arg1, File arg2) {}

}
