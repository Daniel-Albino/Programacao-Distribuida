package pt.isec.a2020134077.ex15.b;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Worker {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        MulticastSocket multicastSocket = new MulticastSocket(5001);

        DatagramPacket datagramPacketRec = new DatagramPacket(new byte[256],256);
        multicastSocket.receive(datagramPacketRec);

        int portServerSocket = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(portServerSocket);

        //System.out.println(datagramPacketRec.getAddress().getHostAddress() + ":" + datagramPacketRec.getPort());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(args[0]);
        byte[] portBytes = byteArrayOutputStream.toByteArray();
        datagramPacketRec.setData(portBytes);
        datagramPacketRec.setLength(portBytes.length);
        multicastSocket.send(datagramPacketRec);

        multicastSocket.close();
        objectOutputStream.close();

        boolean keepGoing = true;
        while (keepGoing){
            Socket cliSocket = serverSocket.accept();
            System.out.println("Connected to Client -> " + cliSocket.getInetAddress().getHostAddress() + ":" + cliSocket.getPort());

            if(true){
                ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(cliSocket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(cliSocket.getInputStream());

                SendInfo sendInfo = (SendInfo) objectInputStream.readObject();

                Double pi = PiCalculator.getPartialPiValue(sendInfo.getIdWorker(),sendInfo.getnWorkers(),sendInfo.getnIntervals());
                System.out.println("Value: " + pi);
                objectOutputStream1.writeObject(pi);
            }else
                keepGoing = false;
            cliSocket.close();
        }
        serverSocket.close();

    }
}
