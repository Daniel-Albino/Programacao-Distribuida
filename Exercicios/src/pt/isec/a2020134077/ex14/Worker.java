package pt.isec.a2020134077.ex14;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        boolean keepGoing = true;
        while (keepGoing){
            Socket cliSocket = serverSocket.accept();
            System.out.println("Connected to Client -> " + cliSocket.getInetAddress().getHostAddress() + ":" + cliSocket.getPort());

            if(true){
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(cliSocket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(cliSocket.getInputStream());

                SendInfo sendInfo = (SendInfo) objectInputStream.readObject();

                Double pi = PiCalculator.getPartialPiValue(sendInfo.getIdWorker(),sendInfo.getnWorkers(),sendInfo.getnIntervals());
                System.out.println("Value: " + pi);
                objectOutputStream.writeObject(pi);
                //cliSocket.setSoTimeout(3000);
            }else
                keepGoing = false;
            cliSocket.close();
        }
        serverSocket.close();
    }
}
