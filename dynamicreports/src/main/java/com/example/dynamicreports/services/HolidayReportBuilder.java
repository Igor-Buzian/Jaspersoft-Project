package com.example.dynamicreports.services;

import com.example.dynamicreports.model.HolidayDataSummary;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JasperReport;

import java.text.ParseException;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * Orchestrates the creation and display of a holiday report.
 * It uses {@link HolidayDataProcessor} for data preparation and
 * {@link ReportComponentFactory} for generating report components.
 */
public class HolidayReportBuilder {

    private final HolidayDataProcessor dataProcessor;
    private final ReportComponentFactory componentFactory;

    /**
     * Constructs a new {@code HolidayReportBuilder} with default data processor
     * and report component factory instances.
     */
    public HolidayReportBuilder() {
        this.dataProcessor = new HolidayDataProcessor();
        this.componentFactory = new ReportComponentFactory();
    }

    /**
     * Builds and displays a comprehensive report detailing holiday information.
     * The report includes a table with specified columns and a 3D bar chart
     * summarizing holiday counts per month per country.
     *
     * @param rawData     A list of raw holiday data arrays (e.g., {"USA", "2025-07-04", "Independence Day"}).
     * @param columnNames An array of strings representing the names of columns
     * to be displayed in the report table (e.g., "country", "date", "name").
     */
    public void buildAndShowReport(List<Object[]> rawData, String[] columnNames) {
        try {
            // 1. Prepare data sources and summary from raw data
            DRDataSource tableDataSource = dataProcessor.createTableDataSource(rawData);
            HolidayDataSummary holidaySummary = dataProcessor.processHolidayDataForSummary(rawData);
              DRDataSource chartDataSource = dataProcessor.createChartDataSource(holidaySummary);

            // 2. Assemble the full report template using prepared data and components
            JasperReportBuilder reportTemplate = assembleReportTemplate(
                    tableDataSource, chartDataSource, holidaySummary, columnNames);

            // 3. Show the generated report
            reportTemplate.show();

        } catch (ParseException e) {
            System.err.println("Error parsing date in holiday data: " + e.getMessage());
            e.printStackTrace();
        } catch (DRException e) {
            System.err.println("Error building or displaying the report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Private helper method to create and configure the entire report structure.
     * This method encapsulates the complex chaining of DynamicReports builders.
     *
     * @param tableDataSource  The {@link DRDataSource} for the main report table.
     * @param chartDataSource  The {@link DRDataSource} for the chart summary.
     * @param holidaySummary   The {@link HolidayDataSummary} containing aggregated data for the chart.
     * @param columnNames      An array of column names to include in the table.
     * @return A fully configured {@link JasperReport} ready to be displayed.
     * @throws DRException If there is an error during the DynamicReports configuration.
     */
    private JasperReportBuilder assembleReportTemplate(
            DRDataSource tableDataSource,
            DRDataSource chartDataSource,
            HolidayDataSummary holidaySummary,
            String[] columnNames) throws DRException {

        StyleBuilder columnTitleStyle = componentFactory.createColumnTitleStyle();
        StyleBuilder boldCenteredStyle = componentFactory.createBoldCenteredStyle();

        // Create table columns and capture the 'country' column
        TextColumnBuilder<String>[] countryColumnRef = new TextColumnBuilder[1];
        List<ColumnBuilder<?, ?>> tableColumns = componentFactory.createTableColumns(columnNames, countryColumnRef);
        TextColumnBuilder<String> countryColumn = countryColumnRef[0];


        if (tableColumns.isEmpty()) {
            throw new DRException("No valid columns were defined for the report. Please check the provided column names.");
        }


        Bar3DChartBuilder chart = componentFactory.createChart(holidaySummary, chartDataSource);


        JasperReportBuilder builder = report()
                .setDataSource(tableDataSource)
                .columns(tableColumns.toArray(new ColumnBuilder[0]))
                .title(cmp.text("Holidays Information").setStyle(boldCenteredStyle))
                .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                .setColumnTitleStyle(columnTitleStyle)
                .setColumnStyle(boldCenteredStyle)
                .highlightDetailEvenRows();


        if (countryColumn != null) {
            builder.subtotalsAtSummary(
                    sbt.count(countryColumn)
                            .setLabel("Total Count")
                            .setLabelStyle(boldCenteredStyle)
                            .setStyle(boldCenteredStyle)
            );
        }

        builder.summary(chart);

        return builder;
    }
}