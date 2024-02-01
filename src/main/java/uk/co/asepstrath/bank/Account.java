package uk.co.asepstrath.bank;

import java.lang.*;
import java.math.BigDecimal;
public class Account {

    private BigDecimal balance;
    private String name;

    public Account(){
        balance = new BigDecimal(0);
        name = null;
    }

    public Account(BigDecimal balance){
        this.balance = balance;
        name = null;
    }

    public Account(String name,BigDecimal balance){
        this.name = name;
        this.balance = balance;
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public String getBalance() {
        return balance.toString();
    }

    public String getName(){return name;}

    public void withdraw(BigDecimal amount){
        if(amount.compareTo(balance)<=0) {
            balance = balance.subtract(amount);
        }else
            throw new ArithmeticException("no");
        }

}
