package mnm.mods.util;

import io.netty.util.NetUtil;

public class IPUtils {

    private static final int DEFAULT_PORT = 25565;

    private String host;
    private int port;
    private boolean ipv6;

    public IPUtils(String address, int port, boolean ipv6) {
        this.host = address;
        this.port = port;
        this.ipv6 = ipv6;
    }

    public static IPUtils parse(String ipString) {
        IPUtils result = null;
        switch (getType(ipString)) {
        case NAME:
        case IPV4:
            if (ipString.contains(":")) {
                String host = ipString.substring(0, ipString.lastIndexOf(':'));
                int port = Integer.parseInt(ipString.substring(ipString.lastIndexOf(':') + 1));
                result = new IPUtils(host, port, false);
            } else {
                result = new IPUtils(ipString, DEFAULT_PORT, false);
            }
            break;
        case IPV6:
            if (ipString.startsWith("[") && ipString.contains("]:")) {
                String host = ipString.substring(0, ipString.lastIndexOf(':'));
                int port = Integer.parseInt(ipString.substring(ipString.lastIndexOf(':') + 1));
                result = new IPUtils(host, port, true);
            } else {
                result = new IPUtils(ipString, DEFAULT_PORT, true);
            }
            break;
        }
        if (result.getHost().isEmpty()) {
            result.host = "localhost";
            result.ipv6 = false;
        }
        return result;
    }

    private static ConnectionType getType(String ipAddress) {
        ConnectionType result;
        if (NetUtil.isValidIpV4Address(ipAddress)) {
            result = ConnectionType.IPV4;
        } else if (NetUtil.isValidIpV6Address(ipAddress)) {
            result = ConnectionType.IPV6;
        } else {
            result = ConnectionType.NAME;
        }
        return result;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public boolean hasPort() {
        return this.port != DEFAULT_PORT;
    }

    public boolean isIPv6() {
        return this.ipv6;
    }

    public String getAddress() {
        return this.host + (hasPort() ? "" : ":" + port);
    }

    public String getFileSafeAddress() {
        return this.host.replace(':', '_') + (hasPort() ? "" : "(" + port + ")");
    }

    private static enum ConnectionType {
        IPV4,
        IPV6,
        NAME;
    }
}
