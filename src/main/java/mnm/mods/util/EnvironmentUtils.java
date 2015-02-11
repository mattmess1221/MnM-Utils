package mnm.mods.util;

/**
 * Class to determine what kind of environment we are in.
 */
public final class EnvironmentUtils {

    // Environments
    public static final String OBF = "obfuscated";
    public static final String SRG = "searge names";
    public static final String MCP = "mod coder pack";

    private EnvironmentUtils() {}

    /**
     * Gets the current Minecraft environment we are in. What determines the
     * environment is the names of vanilla Minecraft classes. If
     * Minecraft.getMinecraft() exists, we are in an "mod coder pack"
     * environment. If Minecraft exists, but there is no getMinecraft(), we are
     * in "searge names." If the Minecraft class doesn't exist, we are in an
     * "obfuscated" environment.
     *
     * @return The environment we are in
     * @see EnvironmentUtils#OBF
     * @see EnvironmentUtils#SRG
     * @see EnvironmentUtils#MCP
     */
    public static String getEnvironment() {
        String env = OBF;
        try {
            // TODO Check for server class
            Class<?> cl = Class.forName("net.minecraft.client.Minecraft");
            env = SRG;

            try {
                cl.getMethod("getMinecraft");
                env = MCP;
            } catch (Exception e) {}

        } catch (ClassNotFoundException e) {}
        return env;
    }

}
