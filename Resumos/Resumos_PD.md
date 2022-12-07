# Resumos Programação Distríbuida

## Sockets Java

### Endereços IP:

- *java.net.InetAddres*

![image](https://user-images.githubusercontent.com/84712694/196043976-a2d3082c-2097-4d6f-9252-654fa076aa16.png)

#### Exemplo:

````
try{
    // Get the local host
    InetAddress localAddress = InetAddress.getLocalHost();
    System.out.println ("IP address : " + localAddress.getHostAddress() );
}catch (UnknownHostException e){
    System.out.println ("Error - unable to resolve localhost");
}

try{
    // Resolve host and get InetAddress
    InetAddress addr = InetAddress.getByName( hostName );
    System.out.println ("IP address : " + addr.getHostAddress() );
    System.out.println ("Hostname : " + addr.getHostName() );
}catch (UnknownHostException e){
    System.out.println ("Error - unable to resolve hostname" );
}
````

### Portocolo UDP:

- *java.net.DatagramPacket*
- Contrução de um datagram UDP para envio
    *DatagramPacket(byte[] buffer, int lenght, InetAddress dest_addr, int dest_port)*

````
InetAddress addr = InetAddress.getByName("192.168.0.1");
byte[] data = new byte[128];
//Fill the array with the data to be sent
//... 
DatagramPacket packet = new DatagramPacket ( data, data.length, addr, 2000);
````

- Construção de um datagram UDP para receção
    *DatagramPacket(byte[] buffer, int lenght)*

````
DatagramPacket packet = new DatagramPacket(new byte[256], 256);
````

![image](https://user-images.githubusercontent.com/84712694/196044291-2c0450ba-f4a4-4d4b-97e9-f0adc8fa4857.png)


````
DatagramPacket packet = new DatagramPacket (new byte[256], 256);
DatagramSocket socket = new DatagramSocket(2000);
boolean finished = false;

while (!finished){
    socket.receive (packet);
    // process the packet
    //...
}

socket.close();
````

- Processar diretamente um array de bytes pode não ser a forma mais adequada/prática;
- A solução passa por usar fluxos de entrada baseados na classe java.io.InputStream ou java.io.InputStreamReader, assumindo o array como dispositivo de entrada.

![image](https://user-images.githubusercontent.com/84712694/196044356-742760be-b344-49aa-abdf-87ae89b2cffb.png)

````
ByteArrayInputStream bin = new ByteArrayInputStream(packet.getData());
DataInputStream din = new DataInputStream (bin);
// Read the contents of the UDP packet
// ...


DatagramSocket socket = new DatagramSocket();
DatagramPacket packet = new DatagramPacket(new byte[256], 256);
packet.setAddress ( InetAddress.getByName ( somehost ) );
packet.setPort( 2000 );
boolean finished = false;

while !finished ){
    // Write data to packet buffer
    // ...
    socket.send(packet);
    // Do something else, like read other packets, or check to
    // see if no more packets to send
    // ...
}

socket.close();
````

#### **Exemplo completo:**

- Cliente (envio de um datagrama)

````
import java.net.*;
import java.io.*;´

public class PacketSendDemo {

public static void main (String args[]){
    // CHECK FOR VALID NUMBER OF PARAMETERS
    int argc = args.length;

    if (argc != 1){
        System.out.println ("Syntax :");
        System.out.println ("java PacketSendDemo hostname");
        return;
    }
    
    String hostname = args[0];

        try{
            System.out.println ("Binding to a local port");
            // CREATE A DATAGRAM SOCKET, BOUND TO ANY AVAILABLE LOCAL PORT
            DatagramSocket socket = new DatagramSocket();
            System.out.println ("Bound to local port " + socket.getLocalPort());

            // CREATE A MESSAGE TO SEND USING A UDP PACKET AND GET THE CONTENTS OF OUR MESSAGE AS AN ARRAY OF BYTES

            byte[] barray = "Greetings!".getBytes();

            // CREATE A DATAGRAM PACKET, CONTAINING OUR BYTE ARRAY
            DatagramPacket packet = new DatagramPacket( barray, barray.length );
            System.out.println ("Looking up hostname " + hostname );

            // LOOKUP THE SPECIFIED HOSTNAME, AND GET AN INETADDRESS
            InetAddress addr = InetAddress.getByName(hostname);
            System.out.println ("Hostname resolved as "+addr.getHostAddress());

            // ADDRESS PACKET TO SENDER
            packet.setAddress(addr);

            // SET PORT NUMBER TO 2000
            packet.setPort(2000);
            
            // SEND THE PACKET - REMEMBER NO GUARANTEE OF DELIVERY
            socket.send(packet);
            System.out.println ("Packet sent!");
        }catch (UnknownHostException e){
            System.err.println ("Can't find host " + hostname);
        }catch (IOException e){
            System.err.println ("Error - " + e);
        }
    }
}

````


- Servidor (receção de um datagrama):

````
import java.net.*;
import java.io.*;

public class PacketReceiveDemo{
    public static void main (String args[]){
        
        try{
            System.out.println ("Binding to local port 2000");
            
            // CREATE A DATAGRAM SOCKET, BOUND TO THE SPECIFIC PORT 2000
            DatagramSocket socket = new DatagramSocket(2000);

            // CREATE A DATAGRAM PACKET WITH A MAXIMUM BUFFER OF 256 BYTES
            DatagramPacket packet = new DatagramPacket(new byte[256], 256);
            
            // RECEIVE A PACKET (BY DEFAULT, THIS IS A BLOCKING OPERATION)
            socket.receive(packet);

            // DISPLAY PACKET INFORMATION
            InetAddress remote_addr = packet.getAddress();
            System.out.println("Sent by: " + remote_addr.getHostAddress());
            System.out.println ("Sent from port: " + packet.getPort());

            // DISPLAY PACKET CONTENTS, BY READING FROM BYTE ARRAY
            String msg = new String(packet.getData(), 0, packet.getLength());
            System.out.println(msg);
            
            socket.close();
        }catch (IOException e){
            System.err.println ("Error - " + e);
        }
    }
}
````
### Protocolo TCP:

- Orientado a ligação (ligações virtuais);
- Apenas permite comunicações ponto-a-ponto;
- Dados tratados como fluxos contínuos de bytes, à semelhança de input e output streams (!= datagramas);
- Entrega de dados ordenada, sem duplicações e livre de erros;
- Na perspetiva do programador, é mais simples do que o UDP quando existem requisitos de fiabilidade;
- java.net.Socket: para transferência de bytes;
- java.net.ServerSocket: para aceitação de pedidos de ligação.


#### Construtor do socket:

````
try{
    // CONNECT TO THE SPECIFIED HOST AND PORT
    Socket mySocket = new Socket ( "www.awl.com", 80);
    // ...
}catch (Exception e){
    System.err.println ("Err – " + e);
}
````

![image](https://user-images.githubusercontent.com/84712694/196044907-847f9489-b315-4f07-8b97-3557bb7f9395.png)


#### Enviar e receber dados:

```` 
try{
    // CONNECT A SOCKET TO SOME HOST MACHINE AND PORT
    Socket socket = new Socket( somehost, someport );
    
    // CONNECT A BUFFERED READER
    BufferedReader reader = new BufferedReader(new InputStreamReader( 
    socket.getInputStream()));

    // CONNECT A PRINT STREAM
    PrintStream pstream =new PrintStream( socket.getOutputStream() );
}catch (Exception e){
    System.err.println("Error – " + e);
}
````

#### Construtor Server Socket:

![image](https://user-images.githubusercontent.com/84712694/196045558-484f0b98-95a1-4320-8a3c-471a63a19d55.png)


#### Exemplo serviço Day Time:

##### Cliente:

````
import java.net.*;
import java.io.*;
public class DaytimeClient {
    public static final int SERVICE_PORT = 5001;
    public static void main(String args[]){
        if (args.length != 1){
            System.out.println ("Syntax – java DaytimeClient host");return;
        }
        // GET THE HOSTNAME OF SERVER
        String hostname = args[0];
        try {
            Socket daytime = new Socket (hostname, SERVICE_PORT);
            System.out.println ("Connection established");
            
            // SET THE SOCKET OPTION JUST IN CASE SERVER STALLS
            daytime.setSoTimeout( 2000 ); //ms
            
            // READ FROM THE SERVER
            BufferedReader reader = new BufferedReader(new InputStreamReader(daytime.getInputStream()));
            System.out.println ("Results : " + reader.readLine());
            // CLOSE THE CONNECTION
            daytime.close();
        }catch (IOException e){ //catches also InterruptedIOExceptionSystem.err.println ("Error " + e);
        }
    }
}
````

##### Servidor:

````
import java.net.*;
import java.io.*;

public class DaytimeServer{

    public static final int SERVICE_PORT = 5001;
    public static void main(String args[]) {
        try {
            // BIND TO THE SERVICE PORT
            ServerSocket server = new ServerSocket(SERVICE_PORT);
            System.out.println ("Daytime service started");
            // LOOP INDEFINITELY, ACCEPTING CLIENTS
            for (;;){
                // GET THE NEXT TCP CLIENT
                Socket nextClient = server.accept();
                System.out.println ("Received request from " +
                nextClient.getInetAddress() + ":" + nextClient.getPort() );OutputStream out = nextClient.getOutputStream();
                PrintStream pout = new PrintStream (out);
                // WRITE THE CURRENT DATE OUT TO THE USER
                pout.println(new java.util.Date());
                // FLUSH UNSENT BYTES
                pout.flush();
                // CLOSE THE CONNECTION
                nextClient.close();
            }
        }catch (BindException e){
            System.err.println("Service already running on port " + SERVICE_PORT);
        }catch (IOException e){
            System.err.println ("I/O error - " + e);
        }
    }
}
````

### Exceções:

- java.net.SocketException representa erros genéricos associados aos sockets (ver protocolo UDP) 
- Subclasses de java.net.SocketException

![image](https://user-images.githubusercontent.com/84712694/196169105-36277685-e32c-4ed7-b0cd-ad82a967aed6.png)


## Aplicações *Multicast*

- O protocolo UDP permite o envio de datagramas para endereços de difusão e de multicast.
- Com endereços IP do tipo difusão, todas as máquinas do domínio de difusão recebem os datagramas.
- Com endereços IP do tipo multicast (classe D), apenas recebem os datagramas as máquinas que se tenham registado no respetivo grupo/endereço.
- Classe: *MulticastSocket* (subclasse de DatagramSocket).

````
InetAddress group = InetAddress.getByName("224.1.2.3");
MulticastSocket socket = new MulticastSocket(port);
socket.joinGroup(group); //Deprecated
Socket.setTimeToLive(1); //TTL

byte[] buffer = new byte[1024];

//...

DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);socket.send(packet);

//...

byte[] response = new byte[1024];
DatagramPacket packet = new DatagramPacket(response, response.length);socket.receive(packet);

//...

socket.leaveGroup(group); //Deprecated
````

- É aconselhável usar os métodos ***joinGroup*** e ***leaveGroup*** que especificam a interface de rede (NIC) associada ao grupo.

````
String NicId = ... //e.g., "127.0.0.1", "lo", "en0", "eth0", "10.1.1.1“, ...NetworkInterface nif;

try{
    nif = NetworkInterface.getByInetAddress(InetAddress.getByName(NicId)); //e.g., 127.0.0.1, 192.168.10.1, ...
}catch (Exception ex){
    nif = NetworkInterface.getByName(NicId); //e.g., lo, eth0, wlan0, en0, ...
}
socket = new MulticastSocket(port);
socket.joinGroup(new InetSocketAddress(group, port), nif);

//socket.setNetworkInterface(nif);
//socket.joinGroup(group);
````

### Serialização de objetos:

![image](https://user-images.githubusercontent.com/84712694/196170246-03f10689-3c17-4631-9398-ddea821bb97c.png)


#### Ligações TCP:

````
s = new Socket(serverAddr, serverPort);

//TRANSMIT OBJECT
in = new ObjectInputStream(s.getInputStream());
out = new ObjectOutputStream(s.getOutputStream());

out.writeObject(objectToTransmit);
//out.writeUnshared(objectToTransmit) in order to avoid caching issues

out.flush();

//RECEIVE OBJECT
returnedObject = (MyClass)in.readObject();
````

#### Datagramas UDP:

````
s = new DatagramSocket();

//TRANSMIT OBJECT

bOut = new ByteArrayOutputStream();
out = new ObjectOutputStream(bOut);

out.writeObject(objectToTransmit);
//out.writeUnshared(objectToTransmit) in order to avoid caching issues

out.flush();

packet = new DatagramPacket(bOut.toByteArray(), bOut.size(), serverAddr, serverPort);
s.send(packet);

//RECEIVE OBJECT
packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
s.receive(packet);
in = new ObjectInputStream(new ByteArrayInputStream(packet.getData(), 0, packet.getLength()));
returnedObject = (MyClass)in.readObject();
````




