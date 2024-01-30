package org.example.lesson4.Homework4.JDBC;

import org.example.lesson4.Homework4.UI.ConsoleView;
import org.example.lesson4.Homework4.models.Course;

import java.sql.*;
import java.util.Collection;

public class Program {
    private static final ConsoleView cv = new ConsoleView();

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "1234";

        try {
            while (true) {
                // Подключение к базе данных
                Connection connection = DriverManager.getConnection(url, user, password);
                try (connection) {
                    // Создание базы данных
                    JDBCActions.createDatabase(connection);
                    System.out.println("Database created successfully");

                    // Использование базы данных
                    JDBCActions.useDatabase(connection);
                    System.out.println("Use database successfully");

                    // Создание таблицы
                    JDBCActions.createTable(connection);
                    System.out.println("Create table successfully");

                    Collection<Course> courses = JDBCActions.readData(connection);

                    switch (cv.menu()) {
                        // Вставка данных
                        case 1 -> insert(connection);
                        // Чтение данных
                        case 2 -> read(courses);
                        // Изменение названия курса
                        case 3 -> changeTitle(connection, courses);
                        // Изменение продолжительности курса
                        case 4 -> changeDuration(connection, courses);
                        // Удаление данных
                        case 5 -> delete(connection);
                        // Закрытие приложения
                        case 0 -> {
                            connection.close();
                            System.out.println("Database connection close successfully");
                            System.exit(0);
                        }
                        default -> System.out.println("Некорректный ввод");
                    }
                } catch (RuntimeException e) {
                    System.out.println("Некорректный ввод");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // region Вспомогательные методы

    // Вставка данных
    private static void insert(Connection connection) throws SQLException {
        JDBCActions.insertData(connection, new Course(cv.inputTitle(), cv.inputDuration()));
        System.out.println("Insert data successfully");
    }

    // Чтение данных
    private static void read(Collection<Course> courses) {
        for (var course : courses)
            System.out.println(course);
        System.out.println("Read data successfully");
    }

    // Изменение названия курса
    private static void changeTitle(Connection connection, Collection<Course> courses) throws SQLException {
        int findID = cv.inputID();
        for (var course : courses) {
            if (course.getId() == findID) course.setTitle(cv.inputTitle());
            JDBCActions.updateData(connection, course);
        }
        System.out.println("Update data successfully");
    }

    // Изменение продолжительности курса
    private static void changeDuration(Connection connection, Collection<Course> courses) throws SQLException {
        int findID = cv.inputID();
        for (var course : courses) {
            if (course.getId() == findID) course.setDuration(cv.inputDuration());
            JDBCActions.updateData(connection, course);
        }
        System.out.println("Update data successfully");
    }

    // Удаление данных
    private static void delete(Connection connection) throws SQLException {
        JDBCActions.deleteData(connection, cv.inputID());
        System.out.println("Delete data successfully");
    }
    // endregion
}