package pt.isec.a2020134077.ex9.ex6;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class Server {
    private static final int MAX_DATA = 4000;

    public static void main(String [] args) throws IOException, ClassNotFoundException {
        boolean keepGoing = true;
        DatagramSocket ds = new DatagramSocket(9001);

        while(keepGoing){
            DatagramPacket dpRec = new DatagramPacket(new byte[MAX_DATA], MAX_DATA);
            ds.receive(dpRec);
            ByteArrayInputStream bais = new ByteArrayInputStream(dpRec.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            String cmd = (String) ois.readObject();

            System.out.println("Received: " + cmd + " from " +
                    dpRec.getAddress().getHostAddress() + ":" + dpRec.getPort());


            FileInputStream file = new FileInputStream(cmd);
            int i = 0, j=0;
            do{
                //byte[] block = new byte[MAX_DATA];
                DataBlock db = new DataBlock();
                //i = file.read(db.block);
                byte[] auxb = new byte[MAX_DATA];
                i = file.read(auxb);
                db.setBlock(auxb);
                db.setBytes(i);
                if(i>0){
                    //dpRec.setData(db.block);
                    //dpRec.setLength(i);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(db);
                    byte[] blockBytes = baos.toByteArray();
                    DatagramPacket dpSend = new DatagramPacket(blockBytes, blockBytes.length, dpRec.getAddress(), dpRec.getPort());
                    ds.send(dpSend);
                    System.out.println("Sending " + i + " bytes");
                    j++;
                }
                DatagramPacket datagramPacket = new DatagramPacket(new byte[256],256);
                ds.receive(datagramPacket);

            }while(i!=-1);
            //dpRec.setLength(0);
            DataBlock db = new DataBlock(true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(db);
            byte[] blockBytes = baos.toByteArray();
            DatagramPacket dpSend = new DatagramPacket(blockBytes, blockBytes.length, dpRec.getAddress(), dpRec.getPort());
            ds.send(dpSend);

            System.out.println(j + " blocks of data sent");
        }

        ds.close();

    }

}
