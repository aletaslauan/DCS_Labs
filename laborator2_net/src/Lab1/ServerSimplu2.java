
package Lab1;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class ServerSimplu2 {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;
        Socket s = null;

        try {
            ss = new ServerSocket(1900);
            System.out.println("Server is waiting for connections...");

            while (true) {
                s = ss.accept();
                System.out.println("Client connected: " + s.getInetAddress() + ":" + s.getPort());

                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);

                // Get the IP address and the port of the connected client
                InetSocketAddress remoteAddress = (InetSocketAddress) s.getRemoteSocketAddress();
                String remoteHost = remoteAddress.getHostName();
                int remotePort = remoteAddress.getPort();

                System.out.println("The connected client is: " + remoteHost + ":" + remotePort);

                int x = Integer.parseInt(in.readLine());
                int y = Integer.parseInt(in.readLine());
                System.out.println("x: " + x);
                System.out.println("y :" + y);

               
                out.println((double) y / (x * 100));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ss.close();
            if (s != null)
                s.close();
        }
    }
}