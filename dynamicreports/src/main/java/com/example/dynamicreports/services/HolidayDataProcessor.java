package com.example.dynamicreports.services;

import com.example.dynamicreports.model.HolidayDataSummary;
import net.sf.dynamicreports.report.datasource.DRDataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Handles all data processing logic for holiday reports, including parsing raw data
 * and creating data sources for tables and charts.
 */
public class HolidayDataProcessor {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMMM", Locale.ENGLISH);

    /**
     * Processes raw holiday data to create a DRDataSource suitable for the main report table.
     *
     * @param rawData A list of raw holiday data arrays (ex. {"USA", "2025-07-04", "Independence Day"}).
     * @return A DRDataSource populated with holiday records.
     * @throws ParseException If a date string within the raw data cannot be parsed.
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
     * Processes raw holiday data to summarize holiday counts by month and country.
     * This summary is primarily used for chart generation.
     *
     * @param rawData A list of raw holiday data arrays.
     * @return A HolidayDataSummary object containing summarized data.
     * @throws ParseException If a date string within the raw data cannot be parsed.
     */
    public HolidayDataSummary processHolidayDataForSummary(List<Object[]> rawData) throws ParseException {
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
     * Creates a DRDataSource specifically for the chart, based on the summarized holiday data.
     *
     * @param summary The HolidayDataSummary object containing pre-processed counts.
     * @return A DRDataSource configured for the chart, with months as categories and countries as series.
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