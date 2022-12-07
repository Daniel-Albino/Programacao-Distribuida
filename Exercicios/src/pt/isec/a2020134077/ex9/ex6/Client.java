package pt.isec.a2020134077.ex9.ex6;


import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



public class Client {
    private static String FILENAME1 = "./ServerFiles/isec.jpg";
    private static String FILENAME2 = "./ServerFiles/text.txt";
    private static String FILENAME3 = "./ServerFiles/music.mp3";

    private static String CLIENT1 = "./ClientFiles/isec.jpg";
    private static String CLIENT2 = "./ClientFiles/text.txt";
    private static String CLIENT3 = "./ClientFiles/music.mp3";
    private static final int MAX_DATA = 4000;
    private static final int MAX_DATA_DP = 6000;




    public static void main(String [] args) throws IOException, ClassNotFoundException {
        boolean keepGoing = true;
        DatagramSocket ds = new DatagramSocket();
        //ds.setSoTimeout(3000);

        byte[] msgBytes = "teste.png".getBytes();
        InetAddress ipServer = InetAddress.getByName("127.0.0.1");
        int portServer = 9001;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(FILENAME3);
        byte[] sendBytes = baos.toByteArray();

        DatagramPacket dpSend = new DatagramPacket(
                sendBytes,
                sendBytes.length,
                ipServer,
                portServer);

        ds.send(dpSend);
        int length = MAX_DATA;

        FileOutputStream file = new FileOutputStream(CLIENT3);

        do{
            DatagramPacket dpRec = new DatagramPacket(new byte[MAX_DATA_DP], MAX_DATA_DP);
            ds.receive(dpRec);
            //length = dpRec.getLength();
            //System.out.println("Received " + dpRec.getLength() + " bytes");
            ByteArrayInputStream bais = new ByteArrayInputStream(dpRec.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            DataBlock db = (DataBlock) ois.readObject();
            if(db.last){ keepGoing = false;}
            else {
                file.write(db.block, 0, db.nBytes);
            }
            DatagramPacket dpSend1 = new DatagramPacket(new byte[256],256, ipServer, portServer);
            ds.send(dpSend1);

        }while(keepGoing);

        file.close();





        //System.out.println("[" + dpRec.getAddress().getHostAddress() + ":" +
        //        dpRec.getPort() + "] --> Server Time: " + msgRec);

        ds.close();
    }

}
