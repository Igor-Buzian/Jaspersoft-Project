package com.example.components;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.style.ReportStyles;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;

public class ReportComponents {

    public static ComponentBuilder createTitleComponent(String title) {
        return cmp.text(title)
                .setStyle(ReportStyles.BOLD_CENTERED_STYLE.setFontSize(16));
    }

    public static ComponentBuilder createFooterComponent() {
        return cmp.horizontalList(
                cmp.text(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                        .setStyle(stl.style().setFontSize(8)),
                cmp.pageXofY().setStyle(stl.style().setFontSize(8).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
        );
    }
}
