package org.example.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 1. Разработайте простой чат на основе сокетов как это было показано на самом семинаре.
 *    Ваше приложение должно включать в себя сервер, который принимает сообщения от клиентов и
 *    пересылает их всем участникам чата. (Вы можете просто переписать наше приложение с семинара,
 *    этого будет вполне достаточно)
 * 2*. Подумайте, как организовать отправку ЛИЧНЫХ сообщений в контексте нашего чата,
 *     доработайте поддержку отправки личных сообщений, небольшую подсказку я дал в конце семинара.
 */

public class ClientManager implements Runnable {

    //region Fields

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;

    public static ArrayList<ClientManager> clients = new ArrayList<>();  // список клиентов (статическая коллекция)

    //endregion

    public ClientManager(Socket socket) {
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            name = bufferedReader.readLine();       // получаем имя
            clients.add(this);
            System.out.println(name + " connected to chat.");
            broadcastMessage("Server: " + name + " connected to chat.");
        }
        catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    /**
     * Удаление клиента из коллекции
     */
    private void removeClient() {
        clients.remove(this);
        System.out.println(name + " disconnected from chat.");
        broadcastMessage("Server: " + name + " disconnected from chat.");
    }

    /**
     * Реализация интерфейса Runnable
     * метод run() - выполняется в отдельном потоке
     * чтение данных и отправка
     */
   /* @Override
    public void run() {
        String massageFromClient;

        while (socket.isConnected()) {
            try {
                // Чтение данных
                massageFromClient = bufferedReader.readLine();    // читаем сообщение от клиента
                if (massageFromClient == null) {
                    // для  macOS (при дисконнекте клиента macOS выдаст null)
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                // Отправка данных всем слушателям
                broadcastMessage(massageFromClient);

            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }

    }*/

    /**v2
     * добавляем проверку приватного сообщения ("/private" в начале сообщения)
     */
    @Override
    public void run() {
        String massageFromClient;

        while (socket.isConnected()) {
            try {
                // Чтение данных
                massageFromClient = bufferedReader.readLine();    // читаем сообщение от клиента

                if (massageFromClient == null) {
                    // для  macOS (при дисконнекте клиента macOS выдаст null)
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                if (massageFromClient.split(" ")[1].equals("/private")) {
                    String[] parts = massageFromClient.split(" ");
                    if (parts.length >= 3) {
                        String recipient = parts[2];
                        String privateMessage = parts[3];
                        sendPrivateMessage(recipient, privateMessage);  // вызываем метод для отправки личного сообщения
                    }
                } else {
                    // Отправка данных всем слушателям
                    broadcastMessage(massageFromClient);
                }

            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }

    }
    /**
     * Отправка сообщения всем слушателям
     *
     * @param message сообщение
     */
    private void broadcastMessage(String message) {
        for (ClientManager client : clients) {
            try {
                if (!client.name.equals(name) && message != null) {
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }
            catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    /** v2
     * Метод для отправки личного сообщения
     * @param recipient
     * @param message
     */
    private void sendPrivateMessage(String recipient, String message) {
        for (ClientManager client : clients) {
            if (client.name.equals(recipient)) {
                try {
                    client.bufferedWriter.write(name + " (private): " + message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
                break;
            }
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        // Удаление клиента из коллекции
        removeClient();
        try {
            // Завершаем работу буфера на чтение данных
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            // Завершаем работу буфера для записи данных
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            // Закрытие соединения с клиентским сокетом
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
