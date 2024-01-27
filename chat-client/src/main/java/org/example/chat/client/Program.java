package org.example.chat.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");

            String name = scanner.nextLine();


            System.out.println();


            InetAddress address = InetAddress.getLocalHost();

            Socket socket = new Socket(address, 5000);
            Client client = new Client(socket, name);
            InetAddress inetAddress = socket.getInetAddress();
            System.out.println("InetAddress: " + inetAddress);
            String remoteIp = inetAddress.getHostAddress();
            System.out.println("Remote IP: " + remoteIp);
            System.out.println("LocalPort:" + socket.getLocalPort());

            System.out.println("To send a message personally, enter it in the format:" +
                    " '/private' 'recipient's name' 'message'");
            System.out.println("Or just write a message in the general chat: ");

            client.listenForMessage();  // Слушатель для входящих сообщений
            client.sendMessage();       // Отправить сообщение

        }
        catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
