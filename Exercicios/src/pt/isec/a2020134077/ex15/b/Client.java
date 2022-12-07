package pt.isec.a2020134077.ex15.b;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        DatagramSocket datagramSocket = new DatagramSocket();

        InetAddress ip = InetAddress.getByName("255.255.255.255");
        int port = 5001;
        DatagramPacket datagramPacket = new DatagramPacket(new byte[0],0,ip,port);
        datagramSocket.send(datagramPacket);
        List<String> ipWorkers = new ArrayList<>();
        List<Integer> portWorkers = new ArrayList<>();
        int a = 0;
        try {
            while (true) {
                datagramSocket.setSoTimeout(3000);

                DatagramPacket datagramPacketRec = new DatagramPacket(new byte[256],256);
                datagramSocket.receive(datagramPacketRec);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacketRec.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                String portTCP = (String) objectInputStream.readObject();

                portWorkers.add(Integer.valueOf(portTCP));
                ipWorkers.add(datagramPacketRec.getAddress().getHostAddress());
            }
        }catch (SocketTimeoutException e){}

        int i = portWorkers.size();
        Socket[] cliSocket = new Socket[i];
        for (int j = 0; j < i; j++) {
            Socket socket;
            try {
                socket = new Socket(ipWorkers.get(j), portWorkers.get(j));
                cliSocket[j] = socket;
                a++;
            } catch (IOException e) {
            }
        }

        ObjectOutputStream[] objectOutputStreams = new ObjectOutputStream[a];
        ObjectInputStream[] objectInputStreams = new ObjectInputStream[a];

        for (int j = 0; j < a; j++) {
            objectOutputStreams[j] = new ObjectOutputStream(cliSocket[j].getOutputStream());
            objectInputStreams[j] = new ObjectInputStream(cliSocket[j].getInputStream());
        }

        SendInfo[] sendInfos = new SendInfo[a];

        for(int j = 0;j<a;j++){
            sendInfos[j] = new SendInfo(j+1,a,100000);
        }

        for(int j = 0;j<a;j++){
            objectOutputStreams[j].writeObject(sendInfos[j]);
        }

        double pi = 0;

        try {
            for (int j = 0; j < a; j++) {
                Double msgBuffer = (Double) objectInputStreams[j].readObject();
                pi += msgBuffer;
            }
        } catch (ClassNotFoundException e) {}

        System.out.println(pi);

        for(int j = 0;j<a;j++){
            cliSocket[j].close();
        }
    }
}
