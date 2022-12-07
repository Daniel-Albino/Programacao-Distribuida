package pt.isec.a2020134077.ex7;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerTCP {
    public static void main(String[] args) throws IOException {
        boolean keepGoing = true;
        ServerSocket serverSocket = new ServerSocket(9001);
        while (keepGoing){
            Socket cliSocket = serverSocket.accept();
            System.out.println("Connected to Client -> " + cliSocket.getInetAddress().getHostAddress() + ":" + cliSocket.getPort());

            InputStream inputStream = cliSocket.getInputStream();
            OutputStream outputStream = cliSocket.getOutputStream();

            byte[] msgRecBytes = new byte[256];
            int nBytes = inputStream.read(msgRecBytes);
            String msgRec = new String(msgRecBytes,0,nBytes);

            System.out.println("Received '" + msgRec + "'...");

            if(msgRec.equals("HORA")){
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String curTime = sdf.format(new Date());

                byte[] currentTime = curTime.getBytes();
                outputStream.write(currentTime);
            }else
                keepGoing = false;
            cliSocket.close();
        }
        serverSocket.close();
    }
}
