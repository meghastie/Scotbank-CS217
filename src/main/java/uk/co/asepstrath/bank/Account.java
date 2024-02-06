package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Account {
    private double value;
    private String name;
    private String accNum;
    private String sortCode;
    private String cardNumber;
    private String cvc;
    private String username;
    private String password;


    public Account(String nme, String acc, String sort, String card, String cv,  String user, String pass, int amount) {
        value = amount;
        name = nme;
        setAccountNumber(acc);
        setCardNumber(card);
        setCVC(cv);




    }
    public String getName(){ return name; }
    public String getAccNum(){ return accNum; }
    public String getSortCode(){ return sortCode; }
    public String getCardNumber(){ return cardNumber; }
    public String getCvc(){ return cvc; }
    public String getUsername(){ return username; }
    public boolean checkPassword(String guess){
        return guess.equals(password);
    }

    public void setName(String nm){ name = nm};
    public void setAccountNumber(String acc){
        if(acc.length() != 8){
            System.out.println("Account  Number error");
        }
        else{
            accNum = acc;
        }
    }
    public void setCVC(String CVC){
        if(CVC.length() != 3){
            System.out.print("CVC error");
        }
        else{
            cvc = CVC;
        }
    }
    public void setCardNumber(String card){
        if(card.length() != 16){
            System.out.println("card number error");
        }
        else{
            cardNumber = card;
        }
    }

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
    /*get for all except password
     check password when given string

    */
    


}
