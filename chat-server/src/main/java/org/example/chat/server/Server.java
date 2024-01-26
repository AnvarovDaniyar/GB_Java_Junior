package org.example.chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    //region Fields
    /**
     * Серверный сокет
     */
    private final ServerSocket serverSocket;

    //endregion

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer(){

        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();      //ожидаем подключения
                System.out.println("New client connected!");
                ClientManager clientManager = new ClientManager(socket);
                Thread thread = new Thread(clientManager);
                thread.start();
            }
        }
        catch (IOException e){
            closeSocket();
        }
    }

    private void closeSocket(){
        try{
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
