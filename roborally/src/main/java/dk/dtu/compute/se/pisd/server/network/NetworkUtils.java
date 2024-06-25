package dk.dtu.compute.se.pisd.server.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utility class for network-related operations.
 * This class provides methods to retrieve network information such as the IP
 * address.
 * 
 * @author Marcus Jagd Hansen, s214962
 */
public class NetworkUtils {

    /**
     * Retrieves the IP address of the local host.
     * 
     * @return The IP address as a String. If the IP address cannot be determined,
     *         returns "Unknown IP Address".
     * @author Marcus Jagd Hansen, s214962
     */
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
