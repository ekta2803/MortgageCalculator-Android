package com.gcekta.mortgagecalculator.model;

import java.io.Serializable;

/**
 * Created by ekta2803 on 3/11/17.
 */

@SuppressWarnings("serial")
public class PropertyPojo implements Serializable {



    int propertyId;
    String propertyType;
    String address;
    String city;
    String state;
    String zipcode;
    double propertyPrice;
    double downPayment;
    double apr;
    int loanTerms;

    public int getLoanTerms() {
        return loanTerms;
    }

    public void setLoanTerms(int loanTerms) {
        this.loanTerms = loanTerms;
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

    public double getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(double propertyPrice) {
        this.propertyPrice = propertyPrice;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }
}
