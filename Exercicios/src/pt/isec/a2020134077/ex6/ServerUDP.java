package pt.isec.a2020134077.ex6;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerUDP {

    public static void main(String[] args) throws IOException {
        boolean keepGoing = true;
        DatagramSocket ds = new DatagramSocket(9001);
        while (keepGoing) {

            DatagramPacket dpRec = new DatagramPacket(new byte[256], 256);
            ds.receive(dpRec);

            String msgRec = new String(dpRec.getData(), 0, dpRec.getLength());

            System.out.println("Receive: " + msgRec + " from " + dpRec.getAddress().getHostAddress()
                    + ":" + dpRec.getPort()); //Print com a mensagem, ip e porto

            FileInputStream fileInputStream = new FileInputStream(msgRec);

            byte[] chunk = new byte[4000];
            int nbytes;

            InetAddress ipCliente = dpRec.getAddress();
            int portClient = dpRec.getPort();

            if (!msgRec.equals("FIM")) {
                do{
                    nbytes = fileInputStream.read(chunk);
                    System.out.println("Bytes read: " + nbytes);
                    if(nbytes > -1){
                        DatagramPacket dpSend = new DatagramPacket(chunk,
                                nbytes, ipCliente, portClient);
                        ds.send(dpSend);
                    }else{
                        DatagramPacket dpSend = new DatagramPacket(new byte[0],
                                0, ipCliente, portClient);
                        ds.send(dpSend);
                    }
                    DatagramPacket dpRec1 = new DatagramPacket(new byte[256], 256);
                    ds.receive(dpRec1);
                }while(nbytes > -1);
            }else{
                keepGoing = false;
            }
        }
        ds.close();
    }

    public static void main1(String[] args) throws IOException{
        FileInputStream fileInputStream = new FileInputStream("./ServerFiles/isec.png");

        byte[] chunk = new byte[4000];
        int nbytes;

        do{
            nbytes = fileInputStream.read(chunk);
            System.out.println("Bytes read: " + nbytes);
            if(nbytes > -1){
                //DatagramPacket dp = new DatagramPacket(chunk,nbytes,ipClient,portClient);
                //Send to
            }
        }while(nbytes > -1);
    }
}
