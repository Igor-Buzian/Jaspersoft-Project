package com.example.jaspersoft;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportFiller {

        public static void main(String[] args) {
            try {
                String jrxmlPath = "C:/Users/crme276/JaspersoftWorkspace/MyReports/FirstTask.jrxml";

                // Raport generation for JRMapCollectionDataSource
                System.out.println("Raport generation for JRMapCollectionDataSource...");
                new MapReportFiller().createReport(jrxmlPath, "C:/Users/crme276/JaspersoftWorkspace/MyReports/Output_Maps.pdf");

                // Raport generation for JRBeanCollectionDataSource
                System.out.println("\nRaport generation for JRBeanCollectionDataSource...");
                new BeanReportFiller().createReport(jrxmlPath, "C:/Users/crme276/JaspersoftWorkspace/MyReports/Output_Beans.pdf");

                // Raport generation for JRResultSetDataSource (requires a database setup)
                System.out.println("\nRaport generation for JRResultSetDataSource...");
                // You will need to create the ResultSetReportFiller class and its method
                new ResultSetReportFiller().createReport(jrxmlPath, "C:/Users/crme276/JaspersoftWorkspace/MyReports/Output_ResultSet.pdf");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

