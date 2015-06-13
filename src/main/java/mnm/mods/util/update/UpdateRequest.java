package mnm.mods.util.update;

public class UpdateRequest {

    private static final String DEFAULT_URL = "https://raw.githubusercontent.com/killjoy1221/Version/master";

    private final String url;
    private final String modId;

    public UpdateRequest(String modid) {
        this(DEFAULT_URL, modid);
    }

    public UpdateRequest(String url, String modid) {
        this.url = url;
        this.modId = modid;
    }

    public String getUrl() {
        return url;
    }

    public String getModId() {
        return modId;
    }

}
