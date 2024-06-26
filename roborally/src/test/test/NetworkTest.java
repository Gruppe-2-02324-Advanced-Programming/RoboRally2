import dk.dtu.compute.se.pisd.roborally.network.Network;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NetworkTest {


    /**
     * Test if the IP address is not null, not empty and matches the IPv4 format
     * @author Christoffer s205449
     */
    @Test
    void getIPv4Address() {
        String ip = Network.getIPv4Address();
        assertNotNull(ip, "IP address should not be null");
        assertFalse(ip.isEmpty(), "IP address should not be empty");
        assertTrue(ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"), "IP address should match IPv4 format");
    }
}