package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Account {

    private BigDecimal dec;
    public String userID;
    private String name;
    private String accNum;
    private String sortCode;
    private String cardNumber;
    private String cvc;
    private String username;
    private String password;
    private Boolean roundUp;
    private double startingBalance

    public Account(String nme, String acc, String sort, String card, String cv,  String user, String pass, int amount) {
        name = nme;
        setAccountNumber(acc);
        setCardNumber(card);
        setCVC(cv);
        username = user;
        password = pass;
        dec = BigDecimal.valueOf(amount);
    }

    public Account(String id, String name, double startBalance, boolean roundUp){
        this.id = id;
        this.name = name;
        this.startingBalance = startBalance;
        this.roundUp = roundUp;
    }

    public String getName(){ return name; }

    public String toString(){
        return name + " - " + dec.toString();
    }


    public void deposit(double amount){
        dec = dec.add(BigDecimal.valueOf(amount));
    }

    public double getBalance() {
        return dec.doubleValue();
    }

    public void withdraw(double money){
        dec = dec.subtract(BigDecimal.valueOf(money));
        if(dec.doubleValue()<0){
            throw new ArithmeticException();
        }
    }
    public String getAccNum(){ return accNum; }
    public String getSortCode(){ return sortCode; }
    public String getCardNumber(){ return cardNumber; }
    public String getCvc(){ return cvc; }
    public String getUsername(){ return username; }
    public boolean checkPassword(String guess){
        return guess.equals(password);
    }

    public void setName(String nm){ name = nm;}
    private void setAccountNumber(String acc){
        if(acc.length() != 8){
            System.out.println("Account number error");
        }
        else{
            accNum = acc;
        }
    }
    private void setCVC(String CVC){
        if(CVC.length() != 3){
            System.out.print("CVC error");
        }
        else{
            cvc = CVC;
        }
    }
    private void setCardNumber(String card){
        if(card.length() != 16){
            System.out.println("card number error");
        }
        else{
            cardNumber = card;
        }
    }
    private void changePassword(String current, String newpass){
        if(current.equals(password)){
            password = newpass;
        }
        else{
            System.out.println("Wrong password");
        }
    }

}

