package mnm.mods.util.update;

import com.google.common.base.Optional;
import com.google.common.primitives.Doubles;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;

public class VersionData {

    private String name;
    private String updateUrl;
    private String url;
    private double revision;

    public VersionData() {}

    private VersionData(String name, String updateUrl, String url, Double revision) {
        this.name = name;
        this.updateUrl = updateUrl;
        this.url = url;
        this.revision = Optional.fromNullable(revision).or(Double.MAX_VALUE);
    }

    public String getName() {
        return name;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public String getUrl() {
        return this.url;
    }

    public double getRevision() {
        return this.revision;
    }

    public int compareTo(double arg0) {
        return Double.compare(revision, arg0);
    }

    boolean isOutdated(double d) {
        return compareTo(d) < 0;
    }

    public static VersionData fromLiteMod(LiteMod litemod) {
        String updateurl = LiteLoader.getInstance().getModMetaData(litemod, "updateUrl", null);
        String url = LiteLoader.getInstance().getModMetaData(litemod, "url", null);
        String rev = LiteLoader.getInstance().getModMetaData(litemod, "revision", null);
        if (updateurl == null || rev == null)
            return null;
        return new VersionData(litemod.getName(), updateurl, url, Doubles.tryParse(rev));
    }
}
