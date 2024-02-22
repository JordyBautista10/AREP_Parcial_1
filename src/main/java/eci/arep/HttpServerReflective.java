package eci.arep;

import java.io.*;
import java.net.*;

public class HttpServerReflective {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        while (true) {
            try {
                serverSocket = new ServerSocket( 45000);
            } catch (IOException e) {
                System.err.println("Could not listen on port: 36000.");
                System.exit(1);
            }

            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }


            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine = "";


            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            outputLine = response();


            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
        }
    }

    public static String response(){
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
    }
}