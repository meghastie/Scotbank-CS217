package uk.co.asepstrath.bank;

public class Account {
    double wealth;
    Account(double wealth){
        this.wealth=wealth;
    }

    public double deposit(double amount) {

        wealth= wealth+amount;
        wealth=Math.round(wealth * 100d) / 100d;
        return wealth;

    }
    public double withdraw(double amount){
        wealth= wealth-amount;
        wealth=Math.round(wealth * 100d) / 100d;
            if(wealth>=0){
                return wealth;
            }else{
                throw new ArithmeticException();
            }



    }
    public double getBalance() {
        return wealth;
    }

}
