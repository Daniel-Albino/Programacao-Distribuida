package pt.isec.a2020134077.ex9.ex8;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    public static void main(String[] args) throws IOException {
        boolean keepGoing = true;
        ServerSocket serverSocket = new ServerSocket(9001);
        while (keepGoing) {
            Socket cliSocket = serverSocket.accept();

            InputStream inputStream = cliSocket.getInputStream();
            OutputStream outputStream = cliSocket.getOutputStream();

            byte[] msgRecBytes = new byte[256];
            int nBytes = inputStream.read(msgRecBytes);

            String msgRec = new String(msgRecBytes,0,nBytes);

            byte[] chunk = new byte[4000];
            int bytes = 0;

            FileInputStream fileInputStream = new FileInputStream(msgRec);

            if(!msgRec.equals("FIM")) {
                do {
                    bytes = fileInputStream.read(chunk);
                    if (bytes > -1) {
                        outputStream.write(chunk, 0, bytes);
                    } else
                        outputStream.write(new byte[0], 0, 0);
                }while (bytes > -1);
            }else
                keepGoing = false;
            cliSocket.close();
        }
        serverSocket.close();
    }
}
