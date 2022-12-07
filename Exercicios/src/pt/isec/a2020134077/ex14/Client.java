package pt.isec.a2020134077.ex14;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\develop\\Aulas\\3 Ano\\pd\\Exercicios\\src\\pt\\isec\\a2020134077\\ex14\\workers.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> ip = new ArrayList<>();
        List<Integer> port = new ArrayList<>();
        String linha;
        do {
            linha = bufferedReader.readLine();
            if (linha != null && !linha.isBlank()) {
                String[] aux = linha.split(" ");
                if (aux.length == 2) {
                    int n;
                    try {
                        n = Integer.parseInt(aux[1]);
                        ip.add(aux[0]);
                        port.add(n);
                    } catch (Exception e) {
                    }
                }
            }
        }while (linha != null) ;

            int i = port.size();
            Socket[] cliSocket = new Socket[i];
            int a = 0;
            for (int j = 0; j < i; j++) {
                Socket socket;
                try {
                    socket = new Socket(ip.get(j), port.get(j));
                    cliSocket[a] = socket;
                    a++;
                } catch (IOException e) {
                }
            }

            ObjectOutputStream[] objectOutputStreams = new ObjectOutputStream[a];
            ObjectInputStream[] objectInputStreams = new ObjectInputStream[a];

            for (int j = 0; j < a; j++) {
                objectOutputStreams[j] = new ObjectOutputStream(cliSocket[j].getOutputStream());
                objectInputStreams[j] = new ObjectInputStream(cliSocket[j].getInputStream());
            }

            SendInfo[] sendInfos = new SendInfo[a];

            for(int j = 0;j<a;j++){
                sendInfos[j] = new SendInfo(j+1,a,100000);
            }

            for(int j = 0;j<a;j++){
                objectOutputStreams[j].writeObject(sendInfos[j]);
            }

            double pi = 0;

            try {
                for (int j = 0; j < a; j++) {
                    Double msgBuffer = (Double) objectInputStreams[j].readObject();
                    pi += msgBuffer;
                }
            } catch (ClassNotFoundException e) {}

            System.out.println(pi);

            for(int j = 0;j<a;j++){
                cliSocket[j].close();
            }
    }
}
