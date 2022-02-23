package edu.escuelaing.arep.parcial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;


public class HttpServer {
    private static int port;
    private static URL url;

    private static HttpServer _instance = new HttpServer();

    private HttpServer(){

    }

    private static HttpServer getInstance(){
        return _instance;
    }

    public static void main(String... args) throws IOException{
        HttpServer.getInstance().startServer(args);
    }

    static {
        try {
            url = new URL("https://openweathermap.org/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void startServer(String[] args) throws IOException {
        port = 35000;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir en puerto "+getPort()+" ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            URL nuevaUrl = new URL("https://openweathermap.org/?ciudad="+inputLine);
            outputLine = nuevaUrl.toString();
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static int getPort() {
        return port;
    }
}
