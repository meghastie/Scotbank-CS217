package uk.co.asepstrath.bank.models;

import uk.co.asepstrath.bank.services.XmlParser;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static java.lang.Math.round;

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
    private ArrayList<Transactions> myTransactions;
    private BigDecimal pot;

    public Account(String id, String name, double balance, boolean round) {
        this.id = id;
        this.name = name;
        startingBalance = balance;
        dec = BigDecimal.valueOf(balance);
        roundUpEnabled = round;
        myTransactions = new ArrayList<>();
        pot = BigDecimal.ZERO;
    }

    public String getName(){ return name; }

    public String toString(){
        return name + " - " + dec.toString();
    }
    public boolean isRoundUpEnabled() {
        if (roundUpEnabled) {
            return true;
        }
        return false;
    }


    public void roundUpSwitch(){roundUpEnabled = !roundUpEnabled;}
    public boolean myTranaction(Transactions t){
        if(t.getFrom().equals(id)){
            if(roundUpEnabled){
                BigDecimal addition = BigDecimal.valueOf(round(t.getAmount())-t.getAmount());
                    withdraw(t.getAmount()+(addition.doubleValue()));
                pot = pot.add(addition);
            }
            else {
                withdraw(t.getAmount());
            }
        } else if (t.getTo().equals(id)) {
            deposit(t.getAmount());
        }
        else {
            return false;
        }
        myTransactions.add(t);
        return true;
    }
    public double getPot(){
        return pot.doubleValue();
    }

    public void releaseSavings() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        XmlParser p = new XmlParser();
        ArrayList<Transactions> allTransactions = p.ParserList();
        boolean valid = false;
        String thisTransactionID = null;
        while (!valid) {
            thisTransactionID = "";
            Random rand = new Random();
            for (int i = 0; i < 8; i++) {
                int number = rand.nextInt(10);
                thisTransactionID += number;
            }
            thisTransactionID += "-";
            for (int i = 0; i < 4; i++) {
                int number = rand.nextInt(10);
                thisTransactionID += number;
            }
            thisTransactionID += "-";
            for (int i = 0; i < 4; i++) {
                int number = rand.nextInt(10);
                thisTransactionID += number;
            }
            thisTransactionID += "-";
            for (int i = 0; i < 12; i++) {
                int number = rand.nextInt(10);
                thisTransactionID += number;
            }
            valid = p.checkID(allTransactions, thisTransactionID);
        }
        System.out.println(thisTransactionID);
        Transactions thisTransaction = new Transactions(timeStamp, pot.doubleValue(), id, thisTransactionID, name, "Saving pot reclaim");
        myTransactions.add(thisTransaction);
        dec = dec.add(pot);
        pot = BigDecimal.ZERO;
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
            dec.add(BigDecimal.valueOf(money));
            throw new ArithmeticException();
        }
    }
    public ArrayList<Transactions> getMyTransactions(){ return myTransactions; }
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

    public boolean roundUpEnabled() {
        return roundUpEnabled;
    }
}



