package pt.isec.a2020134077.ex9.ex5.string;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerUDP {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DatagramSocket datagramSocket = new DatagramSocket(9001);
        boolean keepGoing = true;
        while (keepGoing){
            DatagramPacket datagramPacketRec = new DatagramPacket(new byte[256],256);

            datagramSocket.receive(datagramPacketRec);

            //String msgRec = new String(datagramPacketRec.getData(),0,datagramPacketRec.getLength());

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacketRec.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            String msgRec = (String) objectInputStream.readObject();

            System.out.println("Receive: " + msgRec + " from " + datagramPacketRec.getAddress().getHostAddress()
                    + ":" + datagramPacketRec.getPort());

            if(msgRec.equals("HORA")){
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String curTime = sdf.format(new Date());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(curTime);

                //byte[] curTimeBytes = curTime.getBytes();
                byte[] curTimeBytes = byteArrayOutputStream.toByteArray();
                datagramPacketRec.setData(curTimeBytes);
                datagramPacketRec.setLength(curTimeBytes.length);
                datagramSocket.send(datagramPacketRec);
            }else
                keepGoing = false;
        }
        datagramSocket.close();
    }
}
