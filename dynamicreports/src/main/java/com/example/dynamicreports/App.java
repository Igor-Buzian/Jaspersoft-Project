package com.example.dynamicreports;
import net.sf.jasperreports.engine.JRDataSource;

import java.util.Scanner;

// ... (остальные импорты)

public class App {
    public static void main(String[] args) {
        Report report = new Report();
        JRDataSource dataSource = report.createDataSource();

        System.out.println("Write tables (ex: country, name, date):");

        // Создаем объект Scanner для чтения ввода
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Разделяем строку на отдельные названия столбцов
        String[] columnNames = input.replaceAll(" ","").split("\\s*,\\s*");

        // Проверяем, что пользователь что-то ввёл
        if (columnNames.length > 0 && !columnNames[0].isEmpty()) {
            System.out.println("Create a report with selected columns...");
            report.build(dataSource, columnNames);
        } else {
            System.out.println("Column names were not entered. The report will not be generated.");
        }
    }
}
