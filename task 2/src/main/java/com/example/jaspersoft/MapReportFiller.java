// MapReportFiller.java
package com.example.jaspersoft;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapReportFiller extends ReportBaseFiller {

    public void createReport(String jrxmlPath, String outputPath) {
        try {
            JasperReport jasperReport = compileReport(jrxmlPath);

            // Создаем данные в виде списка Map
            List<Map<String, ?>> data = new ArrayList<>();
            Map<String, Object> record1 = new HashMap<>();
            record1.put("COUNTRY", "Russa");
            record1.put("DATE", "01/01/2024");
            record1.put("NAME", "New Year");
            data.add(record1);

            Map<String, Object> record2 = new HashMap<>();
            record2.put("COUNTRY", "Germany");
            record2.put("DATE", "04/07/2024");
            record2.put("NAME", "Car Day");
            data.add(record2);

            JRMapCollectionDataSource mapDataSource = new JRMapCollectionDataSource(data);

            fillAndExportReport(jasperReport, mapDataSource, outputPath);
            System.out.println("Raport for Map was success created: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}