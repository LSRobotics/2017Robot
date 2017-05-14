package frc.team5181.robot.network;

import java.net.*;

/**
 * Created by LSExplorers on 2/18/2017.
 */

public class Server {
    public boolean debug = true;
    DatagramSocket server = null;
    public String id = "abcd";
    public int port = 12001;
    public int recvSize = 4096;
    private byte[] receivedData = new byte[recvSize + id.length()];
    private byte[] recentData = new byte[recvSize];
    private long receivedPackets = 0;

    public Server(String ID, int PORT, int RECV_SIZE, boolean DEBUG) throws Throwable {
        id = ID;
        port = PORT;
        recvSize = RECV_SIZE;
        debug = DEBUG;
        System.out.println(" [*] Program Started");
        StartListening();
    }

    public void StartListening() throws Throwable {
        // Bind Socket
        try {
            server = new DatagramSocket(port);
            System.out.println(String.format(" [*] Program Bound To Port %s", port));
        }catch (SocketException se){
            System.out.println(String.format(" [*] Program Has Encountered A Fatal Error!\r\n [*] Error - %s", se.getMessage()));
            System.exit(1);
        }

        // Set Packet Thats Recieving
        DatagramPacket packet = new DatagramPacket(receivedData, receivedData.length);

        // Constantly Receive Packets
        System.out.println(" [*] Awaiting Packets!");
        while (true) {
            // Get this packet number
            long thisPacket = ++receivedPackets;

            // Verify Packet
            server.receive(packet);

            //Debug
            if (debug) {
                System.out.println(String.format(" [DEBUG] Received Packet %s", thisPacket));
            }

            String packetdata = new String(packet.getData());

            // Verify Packet Is Not Malicious
            if (packetdata.substring(0, id.length()).equals(id)) {
                //Debug
                if (debug) {
                    System.out.println(String.format(" [DEBUG] Packet %s Is Good", thisPacket));
                }

                // Set most recent working packet
                recentData = packetdata.substring(id.length() + 1, packetdata.length()).getBytes();

            } else {
                //Debug
                if (debug) {
                    System.out.println(String.format(" [DEBUG] Packet %s Is Bad", thisPacket));
                }
            }
        }
    }

    public byte[] RecentBytes(){
        return recentData;
    }
}