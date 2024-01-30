package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Account {
   // private double value;
    private BigDecimal dec;
    private String name;

    public Account(String n, double amount) {
        //value = amount;
        name = n;
        dec = BigDecimal.valueOf(amount);
    }
    public String getName(){ return name; }

    public String toString(){
        return name + " - " + dec.toString();
    }


    public void deposit(double amount){
       /* value = value*100;
        value = value+(amount*100);
        value = value/100.0;
    */
        dec = dec.add(BigDecimal.valueOf(amount));


    }

    public double getBalance() {
        //return value;
        return dec.doubleValue();
    }

    public void withdraw(double money){
        /* value = value*100;
            value = value-(money*100);
            value = value/100.0;
            if(value<0){
                throw new ArithmeticException();
            }
        */
        dec = dec.subtract(BigDecimal.valueOf(money));
        if(dec.doubleValue()<0){
            throw new ArithmeticException();
        }
    }

}
