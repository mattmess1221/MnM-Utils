package mnm.mods.util.update;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;

import mnm.mods.util.MnmUtils;
import mnm.mods.util.text.ChatBuilder;
import mnm.mods.util.text.IChatBuilder;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.IChatComponent;

/**
 * Update checker for several mods.
 *
 * @author Matthew
 */
public class UpdateChecker extends Thread {

    private static final Logger logger = LogManager.getLogger("Updates");

    private VersionData[] data;

    public UpdateChecker(VersionData[] data) {
        super("Update Checker");
        this.data = data;
    }

    @Override
    public void run() {
        for (VersionData data : this.data) {
            runCheck(data);
        }

    }

    private void runCheck(VersionData data) {
        InputStream in = null;
        Reader reader = null;
        UpdateResponse response;
        try {
            URL url = new URL(data.getUpdateUrl());
            in = url.openStream();
            reader = new InputStreamReader(in);
            response = new Gson().fromJson(reader, UpdateResponse.class);
        } catch (IOException e) {
            // failure
            logger.warn("Update check failed.", e);
            return;
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
        }

        if (data.isOutdated(response.update.revision)) {
            notifyUser(data, response);
        } else {
            logger.info("Update check for " + data.getName() + " finished. None found.");
        }
    }

    private void notifyUser(VersionData data, UpdateResponse response) {
        IChatBuilder builder = new ChatBuilder()
                .translation("update.available")
                .text(data.getName()).next();
        if (data.getUrl() != null)
            builder = builder
                    .translation("update.clickhere").end()
                    .click(new ClickEvent(ClickEvent.Action.OPEN_URL, data.getUrl())).next();
        IChatComponent msg = builder
                .text(response.update.version).next()
                .text(response.update.changes).end().build();
        LogManager.getLogger("Updates").info(msg.getUnformattedText());
        MnmUtils.getInstance().getChatProxy().addToChat("Updates", msg);
    }

    public static void runUpdateChecks() {

        List<VersionData> list = Lists.newArrayList();
        for (LiteMod mod : LiteLoader.getInstance().getLoadedMods()) {
            VersionData data = VersionData.fromLiteMod(mod);
            if (data != null)
                list.add(data);
        }
        if (!list.isEmpty()) {
            new UpdateChecker(list.toArray(new VersionData[0])).start();
        }
    }
}
