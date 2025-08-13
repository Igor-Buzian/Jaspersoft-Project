// ReportBaseFiller.java
package com.example.jaspersoft;

import net.sf.jasperreports.engine.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class ReportBaseFiller {

    protected JasperReport compileReport(String jrxmlPath) throws JRException, java.io.IOException {
        InputStream jrxmlInputStream = Files.newInputStream(Paths.get(jrxmlPath));
        return JasperCompileManager.compileReport(jrxmlInputStream);
    }

    protected void fillAndExportReport(JasperReport jasperReport, JRDataSource dataSource, String outputPath) throws JRException {
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
    }
}