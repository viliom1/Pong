package Pong.Network;

import java.io.IOException;
import java.net.*;

public class NetGameServer extends Thread {

    private DatagramSocket socket;
    private NetMultiPlayerHost game;

    public NetGameServer(NetMultiPlayerHost game) {
        this.game = game;
        try {
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String(packet.getData());
            System.out.println("SERVER: Client ["+packet.getAddress().getHostAddress()+":"+packet.getPort()+"] > " + message);
            if (message.trim().equalsIgnoreCase("ping")) {
                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
            }
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
