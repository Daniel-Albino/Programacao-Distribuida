package pt.isec.a2020134077.ex9.ex7.string;

import java.io.*;
import java.net.Socket;

public class ClientTCP {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket cliSocket = new Socket("localhost",9001);
        System.out.println("Connected to Server -> " +
                cliSocket.getInetAddress().getHostAddress() +
                ":" + cliSocket.getPort());

        //InputStream inputStream = cliSocket.getInputStream();
        //OutputStream outputStream = cliSocket.getOutputStream();

        //outputStream.write("HORA".getBytes());

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(cliSocket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(cliSocket.getInputStream());
        objectOutputStream.writeObject("HORA");

        //byte[] msgRecBytes = new byte[256];
        //int nBytes = inputStream.read(msgRecBytes);

        String msgRec = (String) objectInputStream.readObject();

        /*if(nBytes > -1){
            String msgRec = new String(msgRecBytes,0,nBytes);
            System.out.println("Current Time: " + msgRec);
        }else
            System.out.println("Socket is closed.");*/

        if(msgRec != null){
            System.out.println("Current Time: " + msgRec);
        }else
            System.out.println("Socket is closed.");
        /*
            ----- OU -----
        try{
            String msgRec = (String) objectInputStream.readObject();
            System.out.println(msgRec);
        }catch (EOFException e){
            System.out.println("Socket is closed.");
        }
         */

        cliSocket.close();
    }
}
