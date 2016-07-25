package mnm.mods.util.update;

import com.google.gson.annotations.SerializedName;

public class UpdateResponse {

    @SerializedName("@MCVERSION@")
    public Version mcversion;

    public class Version {
        public double revision;
        public String version;
        public String changes;
    }
}
