package com.example.dynamicreports.demo;

import com.example.dynamicreports.services.HolidayReportBuilder;
import com.example.dynamicreports.model.RawData;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Main application entry point for generating holiday reports.
 * It provides sample data, takes user input for desired columns,
 * and then orchestrates the report generation and display.
 */
public class App {
    /**
     * The main method that initiates the application.
     * It prompts the user for column names, creates sample data,
     * and then triggers the report generation process.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        HolidayReportBuilder reportBuilder = new HolidayReportBuilder();
        List<Object[]> rawData =   RawData.createSampleRawData();

        System.out.println("Welcome to the Holiday Report Generator!");
        System.out.println("Enter the column names you want in your report (country, name, date):");


        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();


        String[] columnNames = input.replaceAll(" ", "").split(",");

        // Validate user input for column names
        if (columnNames.length == 0 || columnNames[0].isEmpty()) {
            System.out.println("No valid column names were entered. The report cannot be generated. Exiting.");
            return;
        }

        System.out.println("Generating report with selected columns: " + Arrays.toString(columnNames) + "...");
        reportBuilder.buildAndShowReport(rawData, columnNames);
        System.out.println("Report generation finished.");
    }
}