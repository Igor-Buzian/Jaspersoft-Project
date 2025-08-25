package com.example.dynamicreports.processors;

import com.example.dynamicreports.domain.HolidayDataSummary;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;


import java.text.ParseException;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class HolidayReport {

    private final ReportComponentFactory componentFactory = new ReportComponentFactory();

    /**
     * Builds and displays a report with a table and a 3D bar chart of holiday data.
     *
     * @param rawData The raw holiday data to be processed.
     */
    public void buildReport(List<Object[]> rawData) {
        try {
            // Step 1: Prepare data sources and summary
            DRDataSource tableDataSource = componentFactory.createTableDataSource(rawData);
            HolidayDataSummary holidaySummary = componentFactory.processHolidayData(rawData);
            DRDataSource chartDataSource = componentFactory.createChartDataSource(holidaySummary);

            // Step 2: Build the report template
            JasperReportBuilder reportTemplate = buildReportTemplate(tableDataSource, holidaySummary, chartDataSource);

            // Step 3: Show the final report
            reportTemplate.show();

        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            e.printStackTrace();
        } catch (DRException e) {
            System.err.println("Error building or showing the report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates and configures the report template with all components.
     * This method encapsulates the entire report assembly process.
     *
     * @param tableDataSource  The data source for the table.
     * @param holidaySummary   The summary data for the chart.
     * @param chartDataSource  The data source for the chart.
     * @return The fully configured ReportBuilder instance.
     */
    private JasperReportBuilder buildReportTemplate(
            DRDataSource tableDataSource,
            HolidayDataSummary holidaySummary,
            DRDataSource chartDataSource) throws DRException {

        // Create styles
        StyleBuilder columnTitleStyle = componentFactory.createColumnTitleStyle();
        StyleBuilder boldCenteredStyle = componentFactory.createBoldCenteredStyle();

        // The specific column builder for subtotals
        TextColumnBuilder<String> countryColumn = col.column("Country", "country", type.stringType());
        List<ColumnBuilder<?, ?>> tableColumns = componentFactory.createTableColumns(countryColumn);

        // Create the chart component
        Bar3DChartBuilder chart = componentFactory.createChart(holidaySummary, chartDataSource);

        // Assemble the report and return the builder
        return report()
                .setDataSource(tableDataSource)
                .columns(tableColumns.toArray(new ColumnBuilder[0]))
                .title(cmp.text("Holidays Information").setStyle(boldCenteredStyle))
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
                .summary(chart);
    }
}