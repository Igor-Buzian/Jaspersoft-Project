package com.example.dynamicreports.demo;

import com.example.dynamicreports.processors.HolidayReport;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Object[]> rawData = Arrays.asList(
                new Object[]{"USA", "2025-07-04", "Independence Day"},
                new Object[]{"Germany", "2025-10-03", "Day of German Unity"},
                new Object[]{"Germany", "2025-10-16", "Day of Car"},
                new Object[]{"USA", "2025-11-27", "Thanksgiving"},
                new Object[]{"USA", "2025-12-25", "Christmas"},
                new Object[]{"Moldova", "2025-09-07", "Day of city"},
                new Object[]{"Moldova", "2025-07-07", "Day of vine"},
                new Object[]{"India", "2025-02-12", "Day of Cow"}
        );

        new HolidayReport().buildReport(rawData);
    }
}
