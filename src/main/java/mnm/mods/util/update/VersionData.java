package mnm.mods.util.update;

import com.google.common.primitives.Doubles;

public class VersionData {

    private String name;
    private String version;
    private String url;
    private String revision;

    public VersionData() {}

    public VersionData(String name, String version, String url, double revision) {
        this.name = name;
        this.version = version;
        this.url = url;
        this.revision = Double.toString(revision);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return this.url;
    }

    public double getRevision() {
        Double d = Doubles.tryParse(revision);
        return d != null ? d : -1;
    }
}
