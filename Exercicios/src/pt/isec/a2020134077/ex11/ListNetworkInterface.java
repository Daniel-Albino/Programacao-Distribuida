package pt.isec.a2020134077.ex11;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ListNetworkInterface {
    public static void listNetworkInterface() throws SocketException {
        Enumeration<NetworkInterface> allNIs = NetworkInterface.getNetworkInterfaces();

        while(allNIs.hasMoreElements()){
            NetworkInterface ni = allNIs.nextElement();
            System.out.println(ni.getName());


            Enumeration<InetAddress> allPs = ni.getInetAddresses();
            while (allPs.hasMoreElements()){
                System.out.println(allPs.nextElement().getHostAddress());
            }
            System.out.println("--------------------------------");
        }
    }
}
