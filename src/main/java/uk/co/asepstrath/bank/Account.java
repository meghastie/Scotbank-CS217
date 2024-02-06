package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Account {
    private double value;
    private String name;

    public Account(String n, double amount) {
        value = amount;
        name = n;
    }
    public String getName(){ return name; }


    public void deposit(double amount){
        value = value*100;
        value = value+(amount*100);
        value = value/100.0;


    }

    public double getBalance() {
        return value;
    }

    public double withdraw(double money){
        try{value = value*100;
            value = value-(money*100);
            value = value/100.0;
            if(value<0){
                throw new ArithmeticException();
            }
        } catch(ArithmeticException MyException) {
            System.out.println("Not enough money!");
        }
        return money;
    }

}
