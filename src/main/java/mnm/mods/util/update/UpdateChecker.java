package mnm.mods.util.update;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import mnm.mods.util.LogHelper;
import mnm.mods.util.MnmUtils;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

/**
 * Update checker for several mods.
 *
 * @author Matthew
 */
public class UpdateChecker extends Thread {

    private final UpdateRequest request;
    private final double version;
    private UpdateResponse response;

    public UpdateChecker(UpdateRequest request, double version) {
        this.request = request;
        this.version = version;
    }

    @Override
    public void run() {
        InputStream in = null;
        Reader reader = null;
        try {
            URL url = new URL(request.getUrl() + "/" + request.getModId() + ".json");
            in = url.openStream();
            reader = new InputStreamReader(in);
            response = new Gson().fromJson(reader, UpdateResponse.class);
        } catch (IOException e) {
            // failure
            LogHelper.getLogger().warn("Update check failed.", e);
            response = new UpdateResponse(false);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
        }

        if (isOutdated()) {
            notifyUser();
        } else {
            LogHelper.getLogger("Updates").info("Update check for " + request.getModId() + " finished. None found.");
        }
    }

    private boolean isOutdated() {
        return response.isSuccess() && response.update.revision > version;
    }

    private void notifyUser() {
        String channel = "Updates";
        String message = "A new version of " + request.getModId() + " is available.  "
                + response.update.version + " - " + response.update.changes;
        LogHelper.getLogger(channel).info(message);
        MnmUtils.getInstance().getChatProxy().addToChat(channel, message);
    }
}
