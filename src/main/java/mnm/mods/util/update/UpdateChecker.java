package mnm.mods.util.update;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;

import mnm.mods.util.MnmUtils;

/**
 * Update checker for several mods.
 *
 * @author Matthew
 */
public class UpdateChecker extends Thread {

    private static final Logger logger = LogManager.getLogger("Updates");

    private VersionData data;
    private UpdateResponse response;

    @Deprecated
    public UpdateChecker(UpdateRequest request, double version) {
        this.data = new VersionData(request.getModId(), "old", request.getUrl(), version);
    }

    public UpdateChecker(VersionData data) {
        this.data = data;
    }

    @Override
    public void run() {
        InputStream in = null;
        Reader reader = null;
        try {
            URL url = new URL(data.getUrl());
            in = url.openStream();
            reader = new InputStreamReader(in);
            response = new Gson().fromJson(reader, UpdateResponse.class);
        } catch (IOException e) {
            // failure
            logger.warn("Update check failed.", e);
            response = new UpdateResponse(false);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
        }

        if (isOutdated()) {
            notifyUser();
        } else {
            logger.info("Update check for " + data.getName() + " finished. None found.");
        }
    }

    private boolean isOutdated() {
        return response.isSuccess() && response.update.revision > data.getRevision();
    }

    private void notifyUser() {
        String channel = data.getName();
        String message = "A new version is available.  "
                + response.update.version + " - " + response.update.changes;
        LogManager.getLogger(channel).info(message);
        MnmUtils.getInstance().getChatProxy().addToChat(channel, message);
    }

    public static void runUpdateChecks() {
        try {
            Enumeration<URL> urls = ClassLoader.getSystemResources("update.json");
            runChecks(urls);
        } catch (IOException e) {
            logger.warn("Unable to run update checker");
        }
    }

    private static void runChecks(Enumeration<URL> urls) {
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url == null) {
                continue;
            }
            try {
                runCheck(Resources.toString(url, Charsets.UTF_8));
            } catch (Exception io) {
                logger.warn("Error while parsing url " + url, io);
            }
        }
    }

    private static void runCheck(String update) {
        VersionData data = new Gson().fromJson(update, VersionData.class);
        if (data.getRevision() == 0) {
            logger.debug(data.getName() + " had invalid revision");
            return;
        }
        new UpdateChecker(data).start();
    }
}
