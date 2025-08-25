package com.example.dynamicreports.services;

import com.example.dynamicreports.model.HolidayDataSummary;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * A factory class responsible for creating various report components,
 * such as styles, table columns, and chart builders.
 */
public class ReportComponentFactory {

    /**
     * Creates a basic style for bold text.
     *
     * @return A {@link StyleBuilder} instance for bold text.
     */
    public StyleBuilder createBoldStyle() {
        return stl.style().bold();
    }

    /**
     * Creates a style for bold and horizontally centered text.
     *
     * @return A {@link StyleBuilder} instance for bold and centered text.
     */
    public StyleBuilder createBoldCenteredStyle() {
        return stl.style(createBoldStyle()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    }

    /**
     * Creates a style specifically for report column titles, including a border
     * and a light gray background, with bold and centered text.
     *
     * @return A {@link StyleBuilder} instance for column titles.
     */
    public StyleBuilder createColumnTitleStyle() {
        return stl.style(createBoldCenteredStyle())
                .setBorder(stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);
    }

    /**
     * Dynamically creates a list of {@link ColumnBuilder} instances based on
     * an array of requested column names. It also identifies and provides
     * a reference to the "country" column for potential use in subtotals.
     *
     * @param columnNames       An array of strings representing the desired column names
     * (e.g., "country", "date", "name").
     * @param countryColumnRef  A single-element array used as a mutable reference to store
     * the created "country" {@link TextColumnBuilder} if it's included.
     * This allows the calling method to retrieve it.
     * @return A {@link List} of {@link ColumnBuilder} instances.
     */
    public List<ColumnBuilder<?, ?>> createTableColumns(String[] columnNames, TextColumnBuilder<String>[] countryColumnRef) {
        List<ColumnBuilder<?, ?>> columns = new ArrayList<>();
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
                    System.err.println("Warning: Unknown column specified for report table: " + columnName);
                    break;
            }
        }
        // If the country column was created, store its reference in the provided array.
        if (countryColumnRef != null && countryColumnRef.length > 0) {
            countryColumnRef[0] = countryColumn;
        }
        return columns;
    }

    /**
     * Creates and configures a {@link Bar3DChartBuilder} for displaying holiday counts
     * per month, broken down by country.
     *
     * @param holidaySummary  The {@link HolidayDataSummary} containing the aggregated data.
     * @param chartDataSource The {@link DRDataSource} specifically prepared for the chart.
     * @return A configured {@link Bar3DChartBuilder} instance.
     */
    public Bar3DChartBuilder createChart(HolidayDataSummary holidaySummary, DRDataSource chartDataSource) {
        Bar3DChartBuilder chart = cht.bar3DChart()
                .setTitle("Number of Holidays per Month")
                .setCategory(Columns.column("Month", "month", type.stringType()));

        // Dynamically add a series for each country based on the summary data
        List<String> countries = new ArrayList<>(holidaySummary.getTotalPerCountry().keySet());
        for (String country : countries) {
            int total = holidaySummary.getTotalPerCountry().get(country);
            chart.addSerie(cht.serie(Columns.column(country + " (" + total + " total)", country, type.integerType())));
        }

        chart.setDataSource(chartDataSource);
        return chart;
    }
}