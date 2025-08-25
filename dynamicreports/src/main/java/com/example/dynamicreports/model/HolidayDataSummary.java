package com.example.dynamicreports.model;

import java.util.Map;

/**
 * A simple data class to hold the summarized holiday data, including
 * counts per month per country and total counts per country.
 */
public class HolidayDataSummary {
    private final Map<String, Map<String, Integer>> monthCountryCounts;
    private final Map<String, Integer> totalPerCountry;

    /**
     * Constructs a HolidayDataSummary with the provided maps.
     *
     * @param monthCountryCounts A map storing holiday counts grouped by month and then by country.
     * @param totalPerCountry    A map storing the total holiday counts for each country.
     */
    public HolidayDataSummary(Map<String, Map<String, Integer>> monthCountryCounts, Map<String, Integer> totalPerCountry) {
        this.monthCountryCounts = monthCountryCounts;
        this.totalPerCountry = totalPerCountry;
    }

    /**
     * Returns a map storing holiday counts grouped by month and then by country.
     *
     * @return The month-country counts map.
     */
    public Map<String, Map<String, Integer>> getMonthCountryCounts() {
        return monthCountryCounts;
    }

    /**
     * Returns a map storing the total holiday counts for each country.
     *
     * @return The total per country counts map.
     */
    public Map<String, Integer> getTotalPerCountry() {
        return totalPerCountry;
    }
}