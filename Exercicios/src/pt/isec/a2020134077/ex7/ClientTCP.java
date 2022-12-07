package pt.isec.a2020134077.ex7;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTCP {
    public static void main(String[] args) throws IOException {
        Socket cliSocket = new Socket("localhost",9001);
        System.out.println("Connected to Server -> " +
                cliSocket.getInetAddress().getHostAddress() +
                ":" + cliSocket.getPort());

        InputStream inputStream = cliSocket.getInputStream();
        OutputStream outputStream = cliSocket.getOutputStream();

        outputStream.write("HORA".getBytes());

        byte[] msgRecBytes = new byte[256];
        int nBytes = inputStream.read(msgRecBytes);

        if(nBytes > -1){
            String msgRec = new String(msgRecBytes,0,nBytes);
            System.out.println("Current Time: " + msgRec);
        }else
            System.out.println("Socket is closed.");
        cliSocket.close();
    }
}
