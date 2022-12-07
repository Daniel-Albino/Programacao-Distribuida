package pt.isec.a2020134077.ex6;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientUDP {

    private static String FILENAME1 = "./ServerFiles/isec.jpg";
    private static String FILENAME2 = "./ServerFiles/text.txt";
    private static String FILENAME3 = "./ServerFiles/music.mp3";

    private static String CLIENT1 = "./ClientFiles/isec.jpg";
    private static String CLIENT2 = "./ClientFiles/text.txt";
    private static String CLIENT3 = "./ClientFiles/music.mp3";


    public static void main(String[] args) throws IOException {


        DatagramSocket ds = new DatagramSocket();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the file you want to download: ");
        System.out.println("1 - isec.jpg\n2 - text.txt\n3 - music.mp3");
        System.out.print("R: ");
        int n = scanner.nextInt();

        //byte[] msgBytes = FILENAME3.getBytes();
        byte[] msgBytes = null;
        switch (n){
            case 1 -> msgBytes = FILENAME1.getBytes();
            case 2 -> msgBytes = FILENAME2.getBytes();
            case 3 -> msgBytes = FILENAME3.getBytes();
        }

        InetAddress ipServer = InetAddress.getByName("127.0.0.1");
        int portServer = 9001;

        if(msgBytes != null) {
            DatagramPacket dpSend = new DatagramPacket(msgBytes, msgBytes.length, ipServer, portServer);

            ds.send(dpSend);
            byte[] chunk = new byte[4000];
            int nbytes;
            //FileOutputStream fileInputStream = new FileOutputStream(CLIENT3);
            FileOutputStream fileInputStream = null;
            switch (n){
                case 1 -> fileInputStream = new FileOutputStream(CLIENT1);
                case 2 -> fileInputStream = new FileOutputStream(CLIENT2);
                case 3 -> fileInputStream = new FileOutputStream(CLIENT3);
            }

            do {
                DatagramPacket dpRec = new DatagramPacket(chunk, chunk.length);
                ds.receive(dpRec);
                System.out.println(dpRec.getPort() + " " + dpRec.getLength());
                nbytes = dpRec.getLength();
                if (nbytes > 0)
                    fileInputStream.write(chunk, 0, nbytes);
                DatagramPacket dpSend1 = new DatagramPacket(msgBytes, msgBytes.length, ipServer, portServer);
                ds.send(dpSend1);
            } while (nbytes > 0);
            fileInputStream.close();
        }


        ds.close();

    }
}
