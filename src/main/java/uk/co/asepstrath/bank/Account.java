package uk.co.asepstrath.bank;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Account {

    private BigDecimal dec;
    public String id;
    private String name;
    private String accNum;
    private String sortCode;
    private String cardNumber;
    private String cvc;
    private String username;
    private String password;
    private boolean roundUpEnabled;
    private double startingBalance;
    private ArrayList<Transaction> myTransactions;

    public Account(String id, String name, double balance, boolean round) {
        this.id = id;
        this.name = name;
        startingBalance = balance;
        dec = BigDecimal.valueOf(balance);
        roundUpEnabled = round;
        myTransactions = new ArrayList<>();
    }

    public Account(String name, String acc, String sort, String card, String cv,  String user, String pass, int amount){
        this.name = name;
        setAccountNumber(acc);
        setCardNumber(card);
        setCVC(cv);
        username = user;
        password = pass;
        dec = BigDecimal.valueOf(amount);
    }

    public String getName(){ return name; }

    public String toString(){
        System.out.println("______________________________ATTENTION TESTING BIG DEC _______________________________________");
        System.out.println(dec);
        return name + " - " + dec.toString();
    }
    public boolean myTranaction(Transaction t){
        if(t.getFrom().equals(name)){
            withdraw(t.getAmount());
            if(roundUpEnabled){
                //do round up
            }
        } else if (t.getTo().equals(name)) {
            deposit(t.getAmount());
        }
        else {
            System.out.println("error with transaction");
            return false;
        }
        myTransactions.add(t);
        return true;
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
    public String getId(){return id;}
    public double getStartingBalance(){return startingBalance;}
    public boolean getRoundUp(){return roundUpEnabled;}

    public boolean checkPassword(String guess){
        return guess.equals(password);
    }

    public void setName(String nm){ name = nm;}
    public void setAccountNumber(String acc){
        if(acc.length() != 8){
            System.out.println("Account number error");
        }
        else{
            accNum = acc;
        }
    }
    public void setSortCode(String srt){
        if(srt.length() != 6){
            System.out.println("sort code error");
        }
        else{
            sortCode = srt;
        }
    }
    public void setCVC(String CVC){
        if(CVC.length() != 3){
            System.out.println("CVC error");
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
    public void changePassword(String current, String newpass){
        if(current.equals(password)){
            password = newpass;
        }
        else{
            System.out.println("Wrong password");
        }
    }

}

