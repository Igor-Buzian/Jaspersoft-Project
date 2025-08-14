package com.example.dynamicreports;

import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class Report {

    public void build(List<Object[]> rawData) {
        try {
            // Styles
            StyleBuilder boldStyle = stl.style().bold();
            StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
            StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle)
                    .setBorder(stl.pen1Point())
                    .setBackgroundColor(Color.LIGHT_GRAY);

            // Main Colomns
            List<ColumnBuilder<?, ?>> columns = new ArrayList<>();
            TextColumnBuilder<String> countryColumn = col.column("Country", "country", type.stringType());
            TextColumnBuilder<Date> dateColumn = col.column("Date", "date", type.dateType());
            TextColumnBuilder<String> nameColumn = col.column("Name", "name", type.stringType());

            columns.add(countryColumn);
            columns.add(dateColumn);
            columns.add(nameColumn);

            //  DataSource for table
            DRDataSource tableDataSource = new DRDataSource("country", "date", "name");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (Object[] row : rawData) {
                tableDataSource.add(row[0], dateFormat.parse((String) row[1]), row[2]);
            }

            // Collections with mounth and country
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
            Map<String, Map<String, Integer>> monthCountryCounts = new LinkedHashMap<>();
            Map<String, Integer> totalPerCountry = new LinkedHashMap<>();

            // filling the our collection
            for (Object[] row : rawData) {
                String country = (String) row[0];
                Date date = dateFormat.parse((String) row[1]);
                String month = monthFormat.format(date);

                monthCountryCounts.putIfAbsent(month, new LinkedHashMap<>());
                monthCountryCounts.get(month).put(country,
                        monthCountryCounts.get(month).merge(country, 1, Integer::sum));

                totalPerCountry.put(country, totalPerCountry.getOrDefault(country, 0) + 1);
            }

            //collection with all categories and Serie for our Bar3DChartBuilder
            List<String> dataSourceColumns = new ArrayList<>();
            dataSourceColumns.add("month");
            dataSourceColumns.addAll(totalPerCountry.keySet());

            // logic for take all need information about our countries and month
            DRDataSource chartDataSource = new DRDataSource(dataSourceColumns.toArray(new String[0]));
            List<String> countries = new ArrayList<>(totalPerCountry.keySet());
            List<String> allMonths = new ArrayList<>(monthCountryCounts.keySet());
            for (String month : allMonths) {
                Map<String, Integer> counts = monthCountryCounts.get(month);
                List<Object> row = new ArrayList<>();
                row.add(month);
                for (String country : countries) {
                    row.add(counts.getOrDefault(country, 0));
                }
                chartDataSource.add(row.toArray());
            }


            // create diagramm with month and countries
            Bar3DChartBuilder chart = cht.bar3DChart()
                    .setTitle("Number of Holidays per Month")
                    .setCategory(Columns.column("Month", "month", type.stringType()))
                    .setDataSource(chartDataSource);


            // add country and their count of holidays
            for (String country : totalPerCountry.keySet()) {
                int total = totalPerCountry.get(country);
                chart.addSerie(
                        cht.serie(Columns.column(country + " (" + total + " total)", country,  type.integerType()))

                );
            }

            chart.setDataSource(chartDataSource);

            //  Build Report
            report()
                    .setDataSource(tableDataSource)
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
                    .summary(chart)
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Object[]> rawData = Arrays.asList(
                new Object[]{"USA", "2025-07-04", "Independence Day"},
                new Object[]{"Germany", "2025-10-03", "Day of German Unity"},
                new Object[]{"Germany", "2025-10-16", "Day of Car"},
                new Object[]{"USA", "2025-11-27", "Thanksgiving"},
                new Object[]{"USA", "2025-12-25", "Christmas"},
                new Object[]{"Moldova", "2025-09-7", "Day of city"},
                new Object[]{"Moldova", "2025-07-7", "Day of vine"},
                new Object[]{"India", "20245-02-12", "Day of Cow"}
        );

        new Report().build(rawData);
    }
}
