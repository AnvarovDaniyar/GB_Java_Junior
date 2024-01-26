package org.example.chat.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    //region Fields

    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private final String name;

    //endregion

    public Client(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));  // создаем буферизированный поток для записи
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));   // создаем буферизированный поток для чтения
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    /**
     * Отправить сообщение
     */
    public void sendMessage() {

        try {
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();     // отправляем сообщение и сбрасываем буфер

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = scanner.nextLine();    // читаем сообщение от пользователя
                bufferedWriter.write(name + ": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }


    /**
     * Слушатель для входящих сообщений
     */
    public void listenForMessage() {
        // Создаем поток (имплейментируем интерфейс Runnable)
        new Thread(new Runnable() {

            @Override
            public void run() {
                String message;
                while (socket.isConnected()) {
                    try {
                        message = bufferedReader.readLine();
                        System.out.println(message);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }

        }).start();
    }

    /**
     * Вспомогательный метод для закрытия потоков
     * @param socket
     * @param bufferedReader
     * @param bufferedWriter
     */

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            // Завершаем работу буфера на чтение данных
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            // Завершаем работу буфера для записи данных
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            // Завершаем работу клиентского сокета
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



