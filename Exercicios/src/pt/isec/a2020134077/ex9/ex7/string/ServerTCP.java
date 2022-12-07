package pt.isec.a2020134077.ex9.ex7.string;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerTCP {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        boolean keepGoing = true;
        ServerSocket serverSocket = new ServerSocket(9001);
        while (keepGoing){
            Socket cliSocket = serverSocket.accept();
            System.out.println("Connected to Client -> " + cliSocket.getInetAddress().getHostAddress() + ":" + cliSocket.getPort());

            //InputStream inputStream = cliSocket.getInputStream();
            //OutputStream outputStream = cliSocket.getOutputStream();

            //byte[] msgRecBytes = new byte[256];
            //int nBytes = inputStream.read(msgRecBytes);
            //String msgRec = new String(msgRecBytes,0,nBytes);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(cliSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(cliSocket.getInputStream());
            //Criamos ao ObjectOutputStream 1º pois se for ao contrário fica preso! (DeadLock)

            String msgRec = (String) objectInputStream.readObject();

            System.out.println("Received '" + msgRec + "'...");

            if(msgRec.equals("HORA")){
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String curTime = sdf.format(new Date());

                //byte[] currentTime = curTime.getBytes();
                //outputStream.write(currentTime);
                objectOutputStream.writeObject(curTime);
            }else
                keepGoing = false;
            cliSocket.close();
        }
        serverSocket.close();
    }
}
