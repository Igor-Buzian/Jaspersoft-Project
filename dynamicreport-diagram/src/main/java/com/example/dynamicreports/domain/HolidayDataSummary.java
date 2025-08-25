package com.example.dynamicreports.domain;

import java.util.Map;

/**
 * A simple data class to hold the summarized holiday data.
 */
public class HolidayDataSummary {
    private final Map<String, Map<String, Integer>> monthCountryCounts;
    private final Map<String, Integer> totalPerCountry;

    public HolidayDataSummary(Map<String, Map<String, Integer>> monthCountryCounts, Map<String, Integer> totalPerCountry) {
        this.monthCountryCounts = monthCountryCounts;
        this.totalPerCountry = totalPerCountry;
    }

    public Map<String, Map<String, Integer>> getMonthCountryCounts() {
        return monthCountryCounts;
    }

    public Map<String, Integer> getTotalPerCountry() {
        return totalPerCountry;
    }
}