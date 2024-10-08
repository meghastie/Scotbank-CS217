package uk.co.asepstrath.bank.models;

public class Transactions {
    private String time;
    private double amount;
    private String from;
    private String id;
    private String to;
    private String type;
    public Transactions(){

    }
    public Transactions(String time, double amount, String from, String id, String to, String type){
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

    @Override
    public String toString() {
        return ("Time: " + getTime() +
        "\nAmount: " + getAmount() +
        "\nFrom: " + getFrom() +
        "\nID: " + getID() +
        "\nTo: " + getTo() +
        "\nType: " + getType() + "\n\n\n");
    }
}
