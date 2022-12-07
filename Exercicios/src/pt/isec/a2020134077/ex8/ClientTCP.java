package pt.isec.a2020134077.ex8;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTCP {
    //private static String FILENAME = "./ServerFiles/isec.jpg";
    //private static String FILENAME = "./ServerFiles/text.txt";
    private static String FILENAME = "./ServerFiles/music.mp3";
    //private static String FILENAME = "FIM";

    //private static String ORIGEM = "./ClientFiles/isec.png";
    //private static String ORIGEM = "./ClientFiles/text.txt";
    private static String ORIGEM = "./ClientFiles/music.mp3";


    public static void main(String[] args) throws IOException {
        Socket cliSocket = new Socket("localhost",9001);
        System.out.println("Connected to Server -> " +
                cliSocket.getInetAddress().getHostAddress() +
                ":" + cliSocket.getPort());
        InputStream is = cliSocket.getInputStream();
        OutputStream os = cliSocket.getOutputStream();

        os.write(FILENAME.getBytes());

        FileOutputStream fileInputStream = new FileOutputStream(ORIGEM);
        int nBytes;
        int i = 1;
        do{
            byte[] msgBuffer = new byte[4000];
            nBytes = is.read(msgBuffer);
            if(nBytes > 0){
                fileInputStream.write(msgBuffer,0,nBytes);
            }
            System.out.println("Bytes read " + i +" :" + nBytes);
            OutputStream os2 = cliSocket.getOutputStream();
            os2.write(new byte[0]);
            i++;
        }while(nBytes > 0);
        cliSocket.close();

    }
}
