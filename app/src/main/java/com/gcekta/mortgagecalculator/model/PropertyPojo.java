package com.gcekta.mortgagecalculator.model;

/**
 * Created by ekta2803 on 3/11/17.
 */

public class PropertyPojo {

    String propertyType;
    String address;
    String city;
    String state;
    int zipcode;
    double loanAmt;
    double downPayment;
    double apr;
    int terms;

    public int getTerms() {
        return terms;
    }

    public void setTerms(int terms) {
        this.terms = terms;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public double getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(double loanAmt) {
        this.loanAmt = loanAmt;
    }

    public double getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(double downPayment) {
        this.downPayment = downPayment;
    }

    public double getApr() {
        return apr;
    }

    public void setApr(double apr) {
        this.apr = apr;
    }
}
