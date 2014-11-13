package mnm.mods.util;

public final class EnvironmentUtils {

    // Environments
    public static final String OBF = "obfuscated";
    public static final String SRG = "searge names";
    public static final String MCP = "mod coder pack";

    private EnvironmentUtils() {
    }

    public static String getEnvironment() {
        String env = OBF;
        try {
            // TODO Check for server class
            Class<?> cl = Class.forName("net.minecraft.client.Minecraft");
            env = SRG;

            try {
                cl.getMethod("getMinecraft");
                env = MCP;
            } catch (Exception e) {
            }

        } catch (ClassNotFoundException e) {
        }
        return env;
    }

}
