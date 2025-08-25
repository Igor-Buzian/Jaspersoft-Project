// BeanReportFiller.java
package com.example.jaspersoft.fillers;

import com.example.jaspersoft.model.Holiday;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.util.ArrayList;
import java.util.List;

// Создайте этот класс в отдельном файле Holiday.java
// class Holiday { ... }

public class BeanReportFiller extends ReportBaseFiller {

    public void createReport(String jrxmlPath, String outputPath) {
        try {
            JasperReport jasperReport = compileReport(jrxmlPath);

            List<Holiday> holidays = new ArrayList<>();
            holidays.add(new Holiday("Moldova", "01/01/2024", "Mother day"));
            holidays.add(new Holiday("USA", "04/07/2024", "Halloween"));
            holidays.add(new Holiday("Italia", "14/07/2024", "Palaca"));

            JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(holidays);

            fillAndExportReport(jasperReport, beanDataSource, outputPath);
            System.out.println("Raport for Pojo was success created: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}