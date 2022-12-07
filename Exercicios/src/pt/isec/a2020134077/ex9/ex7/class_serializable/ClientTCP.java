package pt.isec.a2020134077.ex9.ex7.class_serializable;

import java.io.*;
import java.net.Socket;

public class ClientTCP {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket cliSocket = new Socket("localhost",9001);
        System.out.println("Connected to Server -> " +
                cliSocket.getInetAddress().getHostAddress() +
                ":" + cliSocket.getPort());

        /*InputStream inputStream = cliSocket.getInputStream();
        OutputStream outputStream = cliSocket.getOutputStream();

        outputStream.write("HORA".getBytes());

        byte[] msgRecBytes = new byte[256];
        int nBytes = inputStream.read(msgRecBytes);*/

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(cliSocket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(cliSocket.getInputStream());

        objectOutputStream.writeObject("HORA");

        ServerCurrentTime serverCurrentTime = (ServerCurrentTime) objectInputStream.readObject();

        if(serverCurrentTime != null){
            System.out.println("Current Time: " + serverCurrentTime);
        }else
            System.out.println("Socket is closed.");
        cliSocket.close();
    }
}
