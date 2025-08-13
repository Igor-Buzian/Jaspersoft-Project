package com.example.dynamicreports;

import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class Report {

    public void build(JRDataSource dataSource, String[] columnNames) {
        try {
            StyleBuilder boldStyle = stl.style().bold();
            StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
            StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle)
                    .setBorder(stl.pen1Point())
                    .setBackgroundColor(Color.LIGHT_GRAY);

            List<ColumnBuilder> columns = new ArrayList<>();
            TextColumnBuilder<String> countryColumn = null;

            for (String columnName : columnNames) {
                switch (columnName.toLowerCase()) {
                    case "country":
                        countryColumn = col.column("Country", "country", type.stringType());
                        columns.add(countryColumn);
                        break;
                    case "date":
                        columns.add(col.column("Date", "date", type.dateType()));
                        break;
                    case "name":
                        columns.add(col.column("Name", "name", type.stringType()));
                        break;
                    default:
                        System.err.println("Unknow column: " + columnName);
                        break;
                }
            }


            if (columns.isEmpty()) {
                System.err.println("Error! We have not columns");
                return;
            }

            if(countryColumn!=null)
            report()
                    .setDataSource(dataSource)
                    .columns(columns.toArray(new ColumnBuilder[0]))
                    .title(cmp.text("Holidays info").setStyle(boldCenteredStyle))
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                    .setColumnTitleStyle(columnTitleStyle)
                    .setColumnStyle(boldCenteredStyle)
                    .highlightDetailEvenRows()
                    .subtotalsAtSummary(
                            sbt.count(countryColumn)
                                    .setLabel("Total Count")
                                    .setLabelStyle(boldCenteredStyle)
                                    .setStyle(boldCenteredStyle)
                    )
                    .show();
            else
                report()
                    .setDataSource(dataSource)
                    .columns(columns.toArray(new ColumnBuilder[0]))
                    .title(cmp.text("Holidays info").setStyle(boldCenteredStyle))
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                    .setColumnTitleStyle(columnTitleStyle)
                    .setColumnStyle(boldCenteredStyle)
                    .highlightDetailEvenRows()
                    .show();

        } catch (DRException e) {
            throw new RuntimeException(e);
        }
    }

    public JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("country", "date", "name");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dataSource.add("USA", dateFormat.parse("2025-07-04"), "Independence Day");
            dataSource.add("Germany", dateFormat.parse("2025-10-03"), "Day of German Unity");
            dataSource.add("USA", dateFormat.parse("2025-11-27"), "Thanksgiving");
            dataSource.add("USA", dateFormat.parse("2025-12-25"), "Christmas");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}