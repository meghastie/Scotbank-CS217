package uk.co.asepstrath.bank;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.MarshalException;

public class AccountTests {
    Account a;

    @BeforeEach
    public void setUp(){
        a = new Account("0", "kath", 0, false);
    }
    @Test
    public void createAccount(){
        assertTrue(a != null);
    }

    @Test
    public void emptyAccount(){
        assertTrue(a.getBalance()==0);
    }

    @Test
    public void totalFunds(){
        a.deposit(20);
        a.deposit(50);
        assertTrue(a.getBalance()==70);
    }

    @Test
    public void withdraw(){
        a.deposit(40);
        a.withdraw(20);
        assertTrue(a.getBalance() == 20);

    }
    @Test
    public void overdraft(){
        a.deposit(30);
        Assertions.assertThrows(ArithmeticException.class,() -> a.withdraw(100));

    }

    @Test
    public void superSaving(){
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
        a.deposit(17.56);
        a.deposit(5.45);
        System.out.println(a.getBalance());
        assertTrue(a.getBalance() == 23.01);

    }


}
