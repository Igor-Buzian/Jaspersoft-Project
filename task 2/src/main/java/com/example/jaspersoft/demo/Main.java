package com.example.jaspersoft.demo;

import com.example.jaspersoft.fillers.BeanReportFiller;
import com.example.jaspersoft.fillers.MapReportFiller;
import com.example.jaspersoft.fillers.ResultSetReportFiller;

public class Main {

    public static void main(String[] args) {
        try {
            String jrxmlPath = "C:/Users/crme276/JaspersoftWorkspace/MyReports/FirstTask.jrxml";

            System.out.println("Report JRMapCollectionDataSource...");
            new MapReportFiller().createReport(jrxmlPath, "C:/Users/crme276/JaspersoftWorkspace/MyReports/Output_Maps.pdf");

            System.out.println("\nReport JRBeanCollectionDataSource...");
            new BeanReportFiller().createReport(jrxmlPath, "C:/Users/crme276/JaspersoftWorkspace/MyReports/Output_Beans.pdf");

            System.out.println("\nReport JRResultSetDataSource...");
            new ResultSetReportFiller().createReport(jrxmlPath, "C:/Users/crme276/JaspersoftWorkspace/MyReports/Output_ResultSet.pdf");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

