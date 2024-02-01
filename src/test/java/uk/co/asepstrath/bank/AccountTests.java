package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
public class AccountTests {

    @Test
    public void createAccount(){
        Account a = new Account();
        assertTrue(a != null);
    }

    @Test
    public void newAccountValue_0(){
        Account a = new Account();
        assertTrue(a.getBalance().equals("0"));
    }

    @Test
    public void addingFunds(){
        Account a = new Account();
        a.deposit(new BigDecimal(20));
        a.deposit(new BigDecimal(50));
        assertEquals("70",a.getBalance());
    }

    @Test
    public void withdrawFunds(){
        Account a = new Account();
        a.deposit(new BigDecimal(40));
        a.withdraw(new BigDecimal(20));
        assertEquals("20",a.getBalance());
    }

    @Test
    public void overdraft(){
        Account a = new Account();
        a.deposit(new BigDecimal(30));
        assertThrows(ArithmeticException.class,() -> a.withdraw(new BigDecimal(100)));
    }

    @Test
    public void lotsOfDepositAndWithdraw(){
        Account a = new Account(new BigDecimal(20));
        for(int i=0;i<5;i++){a.deposit(new BigDecimal(10));}
        for(int i=0;i<3;i++){a.withdraw(new BigDecimal(20));}
        assertEquals("10",a.getBalance());
    }

    @Test
    public void usingDoubles(){
        Account a = new Account(new BigDecimal("5.45"));
        a.deposit(new BigDecimal("17.56"));
        assertEquals("23.01",a.getBalance());
    }
}
