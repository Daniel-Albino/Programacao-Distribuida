package pt.isec.a2020134077.ex9.ex5.class_serializable;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Calendar;

public class ServerUDP {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DatagramSocket datagramSocket = new DatagramSocket(9001);
        boolean keepGoing = true;
        while (keepGoing){
            DatagramPacket datagramPacketRec = new DatagramPacket(new byte[256],256);

            datagramSocket.receive(datagramPacketRec);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacketRec.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            String msgRec = (String) objectInputStream.readObject();
            //String msgRec = new String(datagramPacketRec.getData(),0,datagramPacketRec.getLength());

            System.out.println("Receive: " + msgRec + " from " + datagramPacketRec.getAddress().getHostAddress()
                    + ":" + datagramPacketRec.getPort());

            if(msgRec.equals("HORA")){
                //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                //String curTime = sdf.format(new Date());

                Calendar calendar = Calendar.getInstance();
                ServerCurrentTime curTime = new ServerCurrentTime(
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND)
                );

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(curTime);
                byte[] curTimeBytes = byteArrayOutputStream.toByteArray();
                //byte[] curTimeBytes = curTime.getBytes();
                datagramPacketRec.setData(curTimeBytes);
                datagramPacketRec.setLength(curTimeBytes.length);
                datagramSocket.send(datagramPacketRec);
            }else
                keepGoing = false;
        }
        datagramSocket.close();
    }
}
