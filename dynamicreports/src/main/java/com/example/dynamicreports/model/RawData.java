package com.example.dynamicreports.model;

import java.util.Arrays;
import java.util.List;

public class RawData {
    /**
     * Creates a predefined list of sample raw holiday data.
     *
     * @return A {@link List} of {@code Object[]} arrays, where each array
     * represents a single holiday record (country, date string, name).
     */
    public static List<Object[]> createSampleRawData() {
        return Arrays.asList(
                new Object[]{"USA", "2025-07-04", "Independence Day"},
                new Object[]{"Germany", "2025-10-03", "Day of German Unity"},
                new Object[]{"Germany", "2025-10-16", "Day of Car"},
                new Object[]{"USA", "2025-11-27", "Thanksgiving"},
                new Object[]{"USA", "2025-12-25", "Christmas"},
                new Object[]{"Moldova", "2025-09-07", "Day of City"},
                new Object[]{"Moldova", "2025-07-07", "Day of Vine"},
                new Object[]{"India", "2025-02-12", "Day of Cow"}
        );
    }
}
