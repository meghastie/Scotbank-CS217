package uk.co.asepstrath.bank.models;

public class Transaction {
    private String time;
    private double amount;
    private String from;
    private String id;
    private String to;
    private String type;

    public Transaction(String time, double amount, String from, String id, String to, String type){
        this.time = time;
        this.amount = amount;
        this.from = from;
        this.id = id;
        this.to = to;
        this.type = type;
    }

    public String getTime(){
        return time;
    }
    public double getAmount(){
        return amount;
    }
    public String getFrom(){
        return from;
    }
    public String getID(){
        return id;
    }
    public String getTo(){
        return to;
    }
    public String getType(){
        return type;
    }


}
