package com.example.jaspersoft;

public class Holiday {

    private String COUNTRY;
    private String DATE;
    private String NAME;

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public String getDATE() {
        return DATE;
    }

    public String getNAME() {
        return NAME;
    }

    public Holiday(String COUNTRY, String DATE, String NAME) {
        this.COUNTRY = COUNTRY;
        this.DATE = DATE;
        this.NAME = NAME;
    }


    public Holiday() {
    }

}