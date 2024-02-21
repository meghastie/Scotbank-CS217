package uk.co.asepstrath.bank;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.MarshalException;

public class AccountTests {
    Account a;
    Transaction t;
    Transaction f;

    @BeforeEach
    public void setUp(){
        a = new Account("0", "kath", 0, false);
        t = new Transaction("21/02/24",53.64,"mum","123456","kath", "donation");
        f = new Transaction("21/02/24",66.89,"kath","123456","mum", "donation");
    }
    @Test
    public void transactionTo(){
        a.myTranaction(t);
        assertEquals(a.getBalance(), 53.64);
    }
    @Test
    public void transactionFrom(){
        a.deposit(100);
        a.myTranaction(f);
        assertEquals(a.getBalance(),(100.0-66.89));
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

    @Test
    public void setAccountNumber(){
        a.setAccountNumber("12345678");
        assertTrue(a.getAccNum().equals("12345678"));
    }

    @Test
    public void wrongAccountNumber(){
        a.setAccountNumber("123");
        assertNull(a.getAccNum());
    }
    @Test
    public void setSortCode(){
        a.setSortCode("123456");
        assertTrue(a.getSortCode().equals("123456"));
    }

    @Test
    public void wrongSortCode(){
        a.setSortCode("123");
        assertNull(a.getSortCode());
    }

    @Test
    public void setCVC(){
        a.setCVC("123");
        assertTrue(a.getCvc().equals("123"));
    }

    @Test
    public void wrongCVC(){
        a.setCVC("123456");
        assertNull(a.getCvc());
    }

    @Test
    public void setCardNumber(){
        a.setCardNumber("1234567812345678");
        assertTrue(a.getCardNumber().equals("1234567812345678"));
    }

    @Test
    public void wrongCardNumber(){
        a.setCardNumber("123");
        assertNull(a.getCardNumber());
    }




}
