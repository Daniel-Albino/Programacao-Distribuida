package pt.isec.a2020134077.ex9.ex5.class_serializable;

import java.io.*;
import java.net.*;

public class ClientUDP {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DatagramSocket datagramSocket = new DatagramSocket();

        //byte[] msg = "HORA".getBytes();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject("HORA");
        byte[] msg = byteArrayOutputStream.toByteArray();

        InetAddress ipServer = InetAddress.getByName("127.0.0.1");
        int serverPort = 9001;

        DatagramPacket datagramPacketSend = new DatagramPacket(msg, msg.length,ipServer,serverPort);
        datagramSocket.send(datagramPacketSend);

        DatagramPacket datagramPacketRec = new DatagramPacket(new byte[256],256);
        datagramSocket.receive(datagramPacketRec);
        //String msgRec = new String(datagramPacketRec.getData(),0,datagramPacketRec.getLength());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacketRec.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ServerCurrentTime serverCurrentTime = (ServerCurrentTime) objectInputStream.readObject();

        System.out.println("[" + datagramPacketRec.getAddress().getHostAddress() + ":" + datagramPacketRec.getPort() + "] Server Time: " + serverCurrentTime);

        datagramSocket.close();

    }
}
