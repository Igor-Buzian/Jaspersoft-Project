package com.example.jaspersoft;


import net.sf.jasperreports.engine.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class ResultSetReportFiller extends ReportBaseFiller {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/HolidaysResult";
    private static final String USER = "postgres";
    private static final String PASS = "islam";

    public void createReport(String jrxmlPath, String outputPath) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            JasperReport jasperReport = compileReport(jrxmlPath);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNTRY, DATE, NAME FROM holidays");

            JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(rs);
            fillAndExportReport(jasperReport, resultSetDataSource, outputPath);
            System.out.println("Raport for ResultSet was created: " + outputPath);
        } catch (Exception e) {
            System.err.println("Error with ResultSet. Trouble with BD");
            e.printStackTrace();
        }
        finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}