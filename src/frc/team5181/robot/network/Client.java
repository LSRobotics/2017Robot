package frc.team5181.robot.network;

import java.net.*;

/**
 * Created by LSExplorers on 2/18/2017.
 */

public class Client {
    private DatagramSocket client;
    private String id = "";
    private InetAddress ip = null;
    private int port = 10001;

    public Client(String IP,  String ID, int PORT) throws Throwable {
        // Set Socket, IP, ID, and PORT
        client = new DatagramSocket();
        ip = InetAddress.getByName(IP);
        id = ID;
        port = PORT;
    }

    public void sendBytes(byte[] data) throws Throwable {
        // Make Final Packet
        byte[] finalpacket = (id + new String(data)).getBytes();
        // Make Datagram Instructions
        DatagramPacket packet = new DatagramPacket(finalpacket, finalpacket.length, ip, port);
        // Send Packet to target server
        client.send(packet);
    }
}
