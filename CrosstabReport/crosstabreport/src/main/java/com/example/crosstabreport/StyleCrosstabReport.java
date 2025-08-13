package com.example.crosstabreport;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabMeasureBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;


public class StyleCrosstabReport {

    // Стили, которые ранее были в Templates
    private StyleBuilder rootStyle = stl.style().setPadding(2);
    private StyleBuilder boldStyle = stl.style(rootStyle).bold();
    private StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    private StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle)
            .setBorder(stl.pen1Point())
            .setBackgroundColor(Color.LIGHT_GRAY);
    private StyleBuilder crosstabStyle = stl.style(rootStyle).setBorder(stl.pen1Point());
    private StyleBuilder crosstabGroupStyle = stl.style(boldStyle).setBorder(stl.pen1Point());
    private StyleBuilder crosstabCellStyle = stl.style(rootStyle).setBorder(stl.pen1Point());


    private ComponentBuilder titleComponent(String title) {
        return cmp.text(title)
                .setStyle(boldCenteredStyle.setFontSize(16));
    }

    private ComponentBuilder footerComponent = cmp.horizontalList(
            cmp.text(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                    .setStyle(stl.style().setFontSize(8)),
            cmp.pageXofY().setStyle(stl.style().setFontSize(8).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
    );

    public StyleCrosstabReport() {
        build();
    }
    private void build() {
        CrosstabRowGroupBuilder<String> rowGroup = ctab.rowGroup("state", String.class)
                .setTotalHeader("Total for state")
                .setTotalHeaderStyle(stl.style(crosstabGroupStyle).setBorder(stl.pen1Point()));

        CrosstabColumnGroupBuilder<String> columnGroup = ctab.columnGroup("item", String.class)
                .setTotalHeaderStyle(stl.style(crosstabGroupStyle).setBorder(stl.pen1Point()));

        CrosstabMeasureBuilder<Integer> quantityMeasure = ctab.measure("Quantity", "quantity", Integer.class, Calculation.SUM);
        CrosstabMeasureBuilder<BigDecimal> unitPriceMeasure = ctab.measure("Unit price", "unitprice", BigDecimal.class, Calculation.SUM);


        ConditionalStyleBuilder condition1 = stl.conditionalStyle(
                        cnd.greater(unitPriceMeasure, new BigDecimal("600"))
                ).setBackgroundColor(new Color(210, 255, 210))
                .setBorder(stl.pen1Point());

        ConditionalStyleBuilder condition2 = stl.conditionalStyle(
                        cnd.smaller(unitPriceMeasure, new BigDecimal("150"))
                ).setBackgroundColor(new Color(255, 210, 210))
                .setBorder(stl.pen1Point());


        StyleBuilder unitPriceStyle = stl.style()
                .conditionalStyles(condition1, condition2)
                .setBorder(stl.pen1Point());


        StyleBuilder totalCellStyle = stl.style()
                .setBackgroundColor(new Color(200, 200, 255))
                .setBorder(stl.pen1Point());

        StyleBuilder totalCellStyle1 = stl.style()
                .setBackgroundColor(new Color(210, 255, 210))
                .setBorder(stl.pen1Point());

        StyleBuilder totalCellStyle2 = stl.style()
                .setBackgroundColor(new Color(118, 118, 118))
                .setBorder(stl.pen1Point());

        unitPriceMeasure.setStyle(unitPriceStyle)
                .setStyle(totalCellStyle, rowGroup)
                .setStyle(totalCellStyle1, columnGroup)
                .setStyle(totalCellStyle, rowGroup, columnGroup);

        quantityMeasure
                .setStyle(totalCellStyle, rowGroup)
                .setStyle(totalCellStyle2, columnGroup)
                .setStyle(totalCellStyle, rowGroup, columnGroup);


        CrosstabBuilder crosstab = ctab.crosstab()
                .setCellStyle(crosstabCellStyle)      // regular cells with frame
                .setGroupStyle(crosstabGroupStyle)    // group titles
                .headerCell(cmp.text("State / Item").setStyle(boldCenteredStyle))
                .rowGroups(rowGroup)
                .columnGroups(columnGroup)
                .measures(quantityMeasure, unitPriceMeasure);

        try {
            report()
                    .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                    .setColumnTitleStyle(columnTitleStyle)
                    .title(titleComponent("StyleCrosstab"))
                    .summary(crosstab)
                    .pageFooter(footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }


    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("state", "item", "quantity", "unitprice");
        dataSource.add("New York", "Notebook", 1, new BigDecimal(500));
        dataSource.add("New York", "DVD", 5, new BigDecimal(30));
        dataSource.add("New York", "DVD", 2, new BigDecimal(45));
        dataSource.add("New York", "DVD", 4, new BigDecimal(36));
        dataSource.add("New York", "DVD", 5, new BigDecimal(41));
        dataSource.add("New York", "Book", 2, new BigDecimal(11));
        dataSource.add("New York", "Book", 8, new BigDecimal(9));
        dataSource.add("New York", "Book", 6, new BigDecimal(14));

        dataSource.add("Washington", "Notebook", 1, new BigDecimal(610));
        dataSource.add("Washington", "DVD", 4, new BigDecimal(40));
        dataSource.add("Washington", "DVD", 6, new BigDecimal(35));
        dataSource.add("Washington", "DVD", 3, new BigDecimal(46));
        dataSource.add("Washington", "DVD", 2, new BigDecimal(42));
        dataSource.add("Washington", "Book", 3, new BigDecimal(12));
        dataSource.add("Washington", "Book", 9, new BigDecimal(8));
        dataSource.add("Washington", "Book", 4, new BigDecimal(14));
        dataSource.add("Washington", "Book", 5, new BigDecimal(10));

        dataSource.add("Florida", "Notebook", 1, new BigDecimal(460));
        dataSource.add("Florida", "DVD", 3, new BigDecimal(49));
        dataSource.add("Florida", "DVD", 4, new BigDecimal(32));
        dataSource.add("Florida", "DVD", 2, new BigDecimal(47));
        dataSource.add("Florida", "Book", 4, new BigDecimal(11));
        dataSource.add("Florida", "Book", 8, new BigDecimal(6));
        dataSource.add("Florida", "Book", 6, new BigDecimal(16));
        dataSource.add("Florida", "Book", 3, new BigDecimal(18));
        return dataSource;
    }

    public static void main(String[] args) {
        new StyleCrosstabReport();
    }
}
