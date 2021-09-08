package com.example.coursecron;

public class Valute {
    private String nominal;
    private String value;
    private String numCode;

    public String getName() {
        return nominal;
    }

    public String getAge() {
        return value;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String toString() {
        return "NumCode - " + numCode + ", Value - " + value + ", Nominal - " + nominal;
    }
}