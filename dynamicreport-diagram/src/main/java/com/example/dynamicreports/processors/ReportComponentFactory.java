package com.example.dynamicreports.processors;

import com.example.dynamicreports.domain.HolidayDataSummary;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ReportComponentFactory {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMMM", Locale.ENGLISH);

    /**
     * Creates a style for report column titles.
     *
     * @return The StyleBuilder instance.
     */
    public StyleBuilder createColumnTitleStyle() {
        return stl.style(createBoldCenteredStyle())
                .setBorder(stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);
    }

    // All other methods (createTableColumns, createChart, etc.) should be public as well
    // ...

    /**
     * Creates a style for bold and centered text.
     *
     * @return The StyleBuilder instance.
     */
    public StyleBuilder createBoldCenteredStyle() {
        StyleBuilder boldStyle = stl.style().bold();
        return stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    }

    /**
     * Defines the columns for the main report table.
     *
     * @return A list of ColumnBuilder instances.
     */
    public List<ColumnBuilder<?, ?>> createTableColumns(TextColumnBuilder<String> countryColumn) {
        return Arrays.asList(
                countryColumn,
                col.column("Date", "date", type.dateType()),
                col.column("Name", "name", type.stringType())
        );
    }

    /**
     * Creates a 3D bar chart based on the summarized holiday data.
     *
     * @param summary The summarized holiday data.
     * @param chartDataSource The data source for the chart.
     * @return The Bar3DChartBuilder instance.
     */
    public Bar3DChartBuilder createChart(HolidayDataSummary summary, DRDataSource chartDataSource) {
        Bar3DChartBuilder chart = cht.bar3DChart()
                .setTitle("Number of Holidays per Month")
                .setCategory(Columns.column("Month", "month", type.stringType()));

        List<String> countries = new ArrayList<>(summary.getTotalPerCountry().keySet());
        for (String country : countries) {
            int total = summary.getTotalPerCountry().get(country);
            chart.addSerie(cht.serie(Columns.column(country + " (" + total + " total)", country, type.integerType())));
        }

        chart.setDataSource(chartDataSource);
        return chart;
    }

    /**
     * Processes raw holiday data to summarize holiday counts by month and country.
     *
     * @param rawData A list of raw holiday data arrays.
     * @return A HolidayDataSummary object containing summarized data.
     * @throws ParseException if a date string cannot be parsed.
     */
    public HolidayDataSummary processHolidayData(List<Object[]> rawData) throws ParseException {
        Map<String, Map<String, Integer>> monthCountryCounts = new LinkedHashMap<>();
        Map<String, Integer> totalPerCountry = new LinkedHashMap<>();

        for (Object[] row : rawData) {
            String country = (String) row[0];
            Date date = DATE_FORMAT.parse((String) row[1]);
            String month = MONTH_FORMAT.format(date);

            monthCountryCounts.putIfAbsent(month, new LinkedHashMap<>());
            monthCountryCounts.get(month).merge(country, 1, Integer::sum);
            totalPerCountry.merge(country, 1, Integer::sum);
        }

        return new HolidayDataSummary(monthCountryCounts, totalPerCountry);
    }

    /**
     * Creates a data source for the report table from raw holiday data.
     *
     * @param rawData A list of raw holiday data arrays.
     * @return The DRDataSource for the table.
     * @throws ParseException if a date string cannot be parsed.
     */
    public DRDataSource createTableDataSource(List<Object[]> rawData) throws ParseException {
        DRDataSource dataSource = new DRDataSource("country", "date", "name");
        for (Object[] row : rawData) {
            String country = (String) row[0];
            Date date = DATE_FORMAT.parse((String) row[1]);
            String name = (String) row[2];
            dataSource.add(country, date, name);
        }
        return dataSource;
    }

    /**
     * Creates a data source for the chart from the summarized data.
     *
     * @param summary The summarized holiday data.
     * @return The DRDataSource for the chart.
     */
    public DRDataSource createChartDataSource(HolidayDataSummary summary) {
        List<String> countries = new ArrayList<>(summary.getTotalPerCountry().keySet());
        List<String> dataSourceColumns = new ArrayList<>();
        dataSourceColumns.add("month");
        dataSourceColumns.addAll(countries);

        DRDataSource chartDataSource = new DRDataSource(dataSourceColumns.toArray(new String[0]));
        List<String> allMonths = new ArrayList<>(summary.getMonthCountryCounts().keySet());

        for (String month : allMonths) {
            Map<String, Integer> counts = summary.getMonthCountryCounts().get(month);
            List<Object> row = new ArrayList<>();
            row.add(month);
            for (String country : countries) {
                row.add(counts.getOrDefault(country, 0));
            }
            chartDataSource.add(row.toArray());
        }
        return chartDataSource;
    }
}