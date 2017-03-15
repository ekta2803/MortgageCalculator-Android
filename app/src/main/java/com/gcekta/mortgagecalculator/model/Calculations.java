package com.gcekta.mortgagecalculator.model;

/**
 * Created by gauravchodwadia on 3/11/17.
 */

public class Calculations {

    private static final String TAG = "Calculations";

    public static double calculateMonthlyPayment(PropertyPojo pp){


        double p = pp.getPropertyPrice() - pp.getDownPayment();
        double r = pp.getApr()/1200;
        double n = pp.getLoanTerms()*12;
        double calc = p * r * Math.pow((1 + r), n);
        double calc1 = calc/(Math.pow((1 + r), n)-1);

        return calc1;
    }

}
