package uk.co.asepstrath.bank;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.rmi.MarshalException;

public class AccountTests {

    @Test
    public void createAccount(){
        Account a = new Account("s",0);
        assertTrue(a != null);
    }

    @Test
    public void emptyAccount(){
        Account a = new Account("s",0);
        assertTrue(a.getBalance()==0);
    }

    @Test
    public void totalFunds(){
        Account a = new Account("s",0);
        a.deposit(20);
        a.deposit(50);
        assertTrue(a.getBalance()==70);
    }

    @Test
    public void withdraw(){
        Account a =new Account("s",0);
        a.deposit(40);
        a.withdraw(20);
        assertTrue(a.getBalance() == 20);

    }
    @Test
    public void overdraft(){
        Account a = new Account("s",0);
        a.deposit(30);
        Assertions.assertThrows(ArithmeticException.class,() -> a.withdraw(100));

    }

    @Test
    public void superSaving(){
        Account a = new Account("s",0);
        a.deposit(20);
        for(int i = 0;i<5;i++){
            a.deposit(10);
        }
        for(int i = 0;i<3;i++){
            a.withdraw(20);
        }
        assertTrue(a.getBalance() == 10);

    }

    @Test
    public void pennies(){
        Account a = new Account("s",0);
        a.deposit(17.56);
        a.deposit(5.45);
        System.out.println(a.getBalance());
        assertTrue(a.getBalance() == 23.01);

    }


}
