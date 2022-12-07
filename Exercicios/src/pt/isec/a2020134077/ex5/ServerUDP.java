package pt.isec.a2020134077.ex5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerUDP {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(9001);
        boolean keepGoing = true;
        while (keepGoing){
            DatagramPacket datagramPacketRec = new DatagramPacket(new byte[256],256);

            datagramSocket.receive(datagramPacketRec);

            String msgRec = new String(datagramPacketRec.getData(),0,datagramPacketRec.getLength());

            System.out.println("Receive: " + msgRec + " from " + datagramPacketRec.getAddress().getHostAddress()
                    + ":" + datagramPacketRec.getPort());

            if(msgRec.equals("HORA")){
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String curTime = sdf.format(new Date());

                byte[] curTimeBytes = curTime.getBytes();
                datagramPacketRec.setData(curTimeBytes);
                datagramPacketRec.setLength(curTimeBytes.length);
                datagramSocket.send(datagramPacketRec);
            }else
                keepGoing = false;
        }
        datagramSocket.close();
    }
}
