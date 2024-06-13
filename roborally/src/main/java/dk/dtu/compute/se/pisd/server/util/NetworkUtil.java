package dk.dtu.compute.se.pisd.server.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtil {

    public static String getIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown IP Address";
        }
    }
}
