package pt.isec.a2020134077.ex9.ex5.string;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientUDP {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DatagramSocket datagramSocket = new DatagramSocket();


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject("HORA");

        //byte[] msg = "HORA".getBytes();
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
        String msgRec = (String) objectInputStream.readObject();

        System.out.println("[" + datagramPacketRec.getAddress().getHostAddress() + ":" + datagramPacketRec.getPort() + "] Server Time: " + msgRec);

        datagramSocket.close();

    }
}
