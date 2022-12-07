package pt.isec.a2020134077.ex11;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static String name;
    private static String nInterface;
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("The network ports will be listed!");
        ListNetworkInterface.listNetworkInterface();
        ChatClient chatClient = new ChatClient();
        Scanner sc = new Scanner(System.in);
        System.out.print("Insert your network inteface (You need to know your network ip): ");
        nInterface = sc.nextLine();

        chatClient.startChat();
    }

    private void startChat() throws IOException, InterruptedException {
        MulticastSocket multicastSocket = new MulticastSocket(9001);
        InetAddress ipGroup = InetAddress.getByName("224.14.14.14");

        SocketAddress socketAddress = new InetSocketAddress(ipGroup,9001);
        NetworkInterface networkInterface = NetworkInterface.getByName(nInterface);

        multicastSocket.joinGroup(socketAddress,networkInterface);

        System.out.println("Welcome to the chat!");
        Scanner sc = new Scanner(System.in);
        System.out.print("Insert your name: ");
        name = sc.nextLine();

        ReceiveMessages receiveMessages = new ReceiveMessages(multicastSocket,ipGroup);
        receiveMessages.start();

        boolean keepGoing = true;
        while (keepGoing){
            String msg = sc.nextLine();
            if(msg.equals("exit"))
                keepGoing = false;
            msg = "[" + name + "]: " + msg;
            DatagramPacket dp = new DatagramPacket(
                    msg.getBytes(),msg.getBytes().length, ipGroup, 9001);
            multicastSocket.send(dp);
        }
        multicastSocket.leaveGroup(socketAddress,networkInterface);
        multicastSocket.close();
        receiveMessages.join();

    }

    class ReceiveMessages extends Thread{
        private MulticastSocket multicastSocket;
        private InetAddress ipGroup;

        public ReceiveMessages(MulticastSocket multicastSocket, InetAddress ipGroup){
            this.multicastSocket = multicastSocket;
            this.ipGroup = ipGroup;
        }

        @Override
        public void run() {
            try {
                while (true){
                    DatagramPacket datagramPacket = new DatagramPacket(new byte[256],256);
                    multicastSocket.receive(datagramPacket);
                    String msg = new String(datagramPacket.getData(),0,datagramPacket.getLength());
                    System.out.println(msg);
                    if(msg.contains("LIST")){
                        String msgName = name;
                        DatagramPacket dp = new DatagramPacket(
                                msgName.getBytes(),msgName.getBytes().length, ipGroup, 9001);
                        multicastSocket.send(dp);
                    }
                }
            }catch (IOException e){}
        }
    }



}
