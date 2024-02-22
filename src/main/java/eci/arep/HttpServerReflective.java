package eci.arep;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class HttpServerReflective {
    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
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


            boolean firstLine = true;
            String path = "";

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (firstLine){
                    path = inputLine.split(" ")[1];
                    firstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }
            if (path.startsWith("/compreflex")) {
                System.out.println("----------------> " + path);
                outputLine = function(path);
            } else {
                outputLine = response();
            }


            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
        }
    }

    public static String function(String uri) throws URISyntaxException, ClassNotFoundException {
        URI url = new URI(uri);
        String query = url.getQuery();
        String resp = response();
        if (query.startsWith("Class")){
            System.out.println("----------------------------------------"+ query.substring(7, query.length()-2));
            try {
                Class<?> clase = Class.forName(query.substring(7, query.length() - 2));
                resp += Arrays.toString(clase.getMethods());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else if (query.startsWith("invoke")){
            System.out.println("----------------------------------------"+ query.substring(8, query.length()-2));
            try {
                String contenido = query.substring(8, query.length()-2);
                Class<?> clase = Class.forName(contenido.split(",")[0]);
                //resp += Arrays.toString(clase.getMethod(contenido.split(",")[1], String));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return resp;
    }

    public static String response(){
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
    }
}