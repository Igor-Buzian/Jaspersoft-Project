package com.example.style;


import java.awt.Color;
import java.math.BigDecimal;

import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ReportStyles {

    public static final StyleBuilder ROOT_STYLE = stl.style().setPadding(2);
    public static final StyleBuilder BOLD_STYLE = stl.style(ROOT_STYLE).bold();
    public static final StyleBuilder BOLD_CENTERED_STYLE = stl.style(BOLD_STYLE).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    public static final StyleBuilder COLUMN_TITLE_STYLE = stl.style(BOLD_CENTERED_STYLE)
            .setBorder(stl.pen1Point())
            .setBackgroundColor(Color.LIGHT_GRAY);
    public static final StyleBuilder CROSSTAB_GROUP_STYLE = stl.style(BOLD_STYLE).setBorder(stl.pen1Point());
    public static final StyleBuilder CROSSTAB_CELL_STYLE = stl.style(ROOT_STYLE).setBorder(stl.pen1Point());


}
