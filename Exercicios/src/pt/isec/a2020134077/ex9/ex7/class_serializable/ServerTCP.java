package pt.isec.a2020134077.ex9.ex7.class_serializable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

public class ServerTCP {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        boolean keepGoing = true;
        ServerSocket serverSocket = new ServerSocket(9001);
        while (keepGoing){
            Socket cliSocket = serverSocket.accept();
            System.out.println("Connected to Client -> " + cliSocket.getInetAddress().getHostAddress() + ":" + cliSocket.getPort());

            /*InputStream inputStream = cliSocket.getInputStream();
            OutputStream outputStream = cliSocket.getOutputStream();*/

            /*byte[] msgRecBytes = new byte[256];
            int nBytes = inputStream.read(msgRecBytes);
            String msgRec = new String(msgRecBytes,0,nBytes);*/

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(cliSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(cliSocket.getInputStream());
            String msgRec = (String) objectInputStream.readObject();

            System.out.println("Received '" + msgRec + "'...");

            if(msgRec.equals("HORA")){
                /*SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String curTime = sdf.format(new Date());*/

                Calendar calendar = Calendar.getInstance();

                ServerCurrentTime serverCurrentTime = new ServerCurrentTime(
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND)
                );

                /*byte[] currentTime = curTime.getBytes();
                outputStream.write(currentTime);*/
                objectOutputStream.writeObject(serverCurrentTime);
            }else
                keepGoing = false;
            cliSocket.close();
        }
        serverSocket.close();
    }
}
