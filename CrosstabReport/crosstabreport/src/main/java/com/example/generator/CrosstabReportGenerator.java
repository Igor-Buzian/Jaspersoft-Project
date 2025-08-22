package com.example.generator;

import com.example.components.ReportComponents;
import com.example.data_source.ReportDataSource;
import com.example.style.ReportStyles;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabMeasureBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;

import java.awt.Color;
import java.math.BigDecimal;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class CrosstabReportGenerator {

    public static void main(String[] args) {
        buildReport();
    }

    private static void buildReport() {
        CrosstabBuilder crosstab = createCrosstab();

        try {
            report()
                    .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                    .setColumnTitleStyle(ReportStyles.COLUMN_TITLE_STYLE)
                    .title(ReportComponents.createTitleComponent("StyleCrosstab"))
                    .summary(crosstab)
                    .pageFooter(ReportComponents.createFooterComponent())
                    .setDataSource(ReportDataSource.createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private static CrosstabBuilder createCrosstab() {
        CrosstabRowGroupBuilder<String> rowGroup = ctab.rowGroup("state", String.class)
                .setTotalHeader("Total for state")
                .setTotalHeaderStyle(ReportStyles.CROSSTAB_GROUP_STYLE.setBorder(stl.pen1Point()));

        CrosstabColumnGroupBuilder<String> columnGroup = ctab.columnGroup("item", String.class)
                .setTotalHeaderStyle(ReportStyles.CROSSTAB_GROUP_STYLE.setBorder(stl.pen1Point()));

        CrosstabMeasureBuilder<Integer> quantityMeasure = ctab.measure("Quantity", "quantity", Integer.class, Calculation.SUM);
        CrosstabMeasureBuilder<BigDecimal> unitPriceMeasure = ctab.measure("Unit price", "unitprice", BigDecimal.class, Calculation.SUM);

        applyConditionalStyles(quantityMeasure, unitPriceMeasure, rowGroup, columnGroup);

        return ctab.crosstab()
                .setCellStyle(ReportStyles.CROSSTAB_CELL_STYLE)
                .setGroupStyle(ReportStyles.CROSSTAB_GROUP_STYLE)
                .headerCell(cmp.text("State / Item").setStyle(ReportStyles.BOLD_CENTERED_STYLE))
                .rowGroups(rowGroup)
                .columnGroups(columnGroup)
                .measures(quantityMeasure, unitPriceMeasure);
    }

    private static void applyConditionalStyles(
            CrosstabMeasureBuilder<Integer> quantityMeasure,
            CrosstabMeasureBuilder<BigDecimal> unitPriceMeasure,
            CrosstabRowGroupBuilder<String> rowGroup,
            CrosstabColumnGroupBuilder<String> columnGroup) {

        ConditionalStyleBuilder condition1 = stl.conditionalStyle(
                cnd.greater(unitPriceMeasure, new BigDecimal("600"))
        ).setBackgroundColor(new Color(210, 255, 210)).setBorder(stl.pen1Point());

        ConditionalStyleBuilder condition2 = stl.conditionalStyle(
                cnd.smaller(unitPriceMeasure, new BigDecimal("150"))
        ).setBackgroundColor(new Color(255, 210, 210)).setBorder(stl.pen1Point());

        StyleBuilder unitPriceStyle = stl.style()
                .conditionalStyles(condition1, condition2)
                .setBorder(stl.pen1Point());

        StyleBuilder totalCellStyle = stl.style().setBackgroundColor(new Color(200, 200, 255)).setBorder(stl.pen1Point());
        StyleBuilder totalCellStyle1 = stl.style().setBackgroundColor(new Color(210, 255, 210)).setBorder(stl.pen1Point());
        StyleBuilder totalCellStyle2 = stl.style().setBackgroundColor(new Color(118, 118, 118)).setBorder(stl.pen1Point());

        unitPriceMeasure.setStyle(unitPriceStyle)
                .setStyle(totalCellStyle, rowGroup)
                .setStyle(totalCellStyle1, columnGroup)
                .setStyle(totalCellStyle, rowGroup, columnGroup);

        quantityMeasure.setStyle(totalCellStyle, rowGroup)
                .setStyle(totalCellStyle2, columnGroup)
                .setStyle(totalCellStyle, rowGroup, columnGroup);
    }
}