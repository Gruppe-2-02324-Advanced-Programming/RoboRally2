package dk.dtu.compute.se.pisd.roborally.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkService {

    public static void runClient() {
        String ipAddress = JOptionPane.showInputDialog(null,
                "Indtast serverens IP-adresse",
                "IP-indtastning",
                JOptionPane.QUESTION_MESSAGE);

        if (ipAddress != null && !ipAddress.trim().isEmpty()) {
            String url = "http://" + ipAddress + ":8080/hello";
            CloseableHttpClient httpClient = HttpClients.createDefault();

            try {
                HttpGet request = new HttpGet(url);
                HttpResponse response = httpClient.execute(request);

                String responseBody = EntityUtils.toString(response.getEntity());
                JOptionPane.showMessageDialog(null, "Response: " + responseBody, "Response",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Fejl ved hentning af data fra serveren", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "IP-adressen er ikke gyldig", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void printLocalIpAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();
            System.out.println("Serveren kører på IP-adresse: " + ipAddress);
            JOptionPane.showMessageDialog(null, "Serveren kører på IP-adresse: " + ipAddress, "Server IP",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Kunne ikke hente IP-adressen", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String getLocalIpAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Kunne ikke hente IP-adressen";
        }
    }
}
