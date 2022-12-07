package pt.isec.a2020134077.ex5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientUDP {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();

        byte[] msg = "HORA".getBytes();

        InetAddress ipServer = InetAddress.getByName("127.0.0.1");
        int serverPort = 9001;

        DatagramPacket datagramPacketSend = new DatagramPacket(msg, msg.length,ipServer,serverPort);
        datagramSocket.send(datagramPacketSend);

        DatagramPacket datagramPacketRec = new DatagramPacket(new byte[256],256);
        datagramSocket.receive(datagramPacketRec);
        String msgRec = new String(datagramPacketRec.getData(),0,datagramPacketRec.getLength());

        System.out.println("[" + datagramPacketRec.getAddress().getHostAddress() + ":" + datagramPacketRec.getPort() + "] Server Time: " + msgRec);

        datagramSocket.close();

    }
}
