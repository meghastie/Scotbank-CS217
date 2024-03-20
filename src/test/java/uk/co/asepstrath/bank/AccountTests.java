package uk.co.asepstrath.bank;
import static org.junit.jupiter.api.Assertions.*;

import uk.co.asepstrath.bank.models.Account;
import uk.co.asepstrath.bank.models.Transactions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTests {
    Account a;
    Account b;
    Transactions t;
    Transactions f;
    Transactions c;

    @BeforeEach
    public void setUp(){
        a = new Account("0", "kath", 0, false);
        b = new Account("1", "mum", 0, true);
        t = new Transactions("21/02/24",53.64,"1","123456","0", "donation");
        f = new Transactions("21/02/24",66.89,"0","123456","1", "donation");
        c = new Transactions("21/03/24",50.0,"50","40", "30","testing");
    }
    @Test
    public void roundUpEnabled(){
        assertTrue(b.roundUpEnabled());
    }
    @Test
    public void noTransaction(){
        assertFalse(a.myTranaction(c));
    }
    @Test
    public void roundUpSwitch(){
        b.roundUpSwitch();
        assertFalse(b.roundUpEnabled());
    }
    @Test
    public void roundUpDisabled(){
        assertFalse(a.roundUpEnabled());
    }
    @Test
    public void setName(){
        a.setName("test");
        assertEquals(a.getName(),"test");
    }


    @Test
    public void minusBalance(){
        a.deposit(100);
        a.myTranaction(f);
        assertEquals(33.11,a.getBalance());
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
    public void roundupTest(){
        b.deposit(100);
        b.myTranaction(t);
        assertEquals(b.getBalance(),46);
    }
    @Test
    public void roundUpRelease(){
        b.deposit(100);
        b.myTranaction(t);
        b.releaseSavings();
        assertEquals(b.getBalance(),46.36);
    }
    @Test
    public void roundUpReleaseTransactions(){
        b.deposit(100);
        b.myTranaction(t);
        b.releaseSavings();
        System.out.println(b.getMyTransactions());
        assertEquals(b.getMyTransactions().size(),2);
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
    public void AccountNumber(){
        a.setAccountNumber("12345678");
        assertTrue(a.getAccNum().equals("12345678"));
    }

    @Test
    public void wrongAccountNumber(){
        a.setAccountNumber("123");
        assertNull(a.getAccNum());
    }
    @Test
    public void SortCode(){
        a.setSortCode("123456");
        assertTrue(a.getSortCode().equals("123456"));
    }

    @Test
    public void wrongSortCode(){
        a.setSortCode("123");
        assertNull(a.getSortCode());
    }

    @Test
    public void CVC(){
        a.setCVC("123");
        assertTrue(a.getCvc().equals("123"));
    }

    @Test
    public void wrongCVC(){
        a.setCVC("123456");
        assertNull(a.getCvc());
    }

    @Test
    public void CardNumber(){
        a.setCardNumber("1234567812345678");
        assertTrue(a.getCardNumber().equals("1234567812345678"));
    }

    @Test
    public void wrongCardNumber(){
        a.setCardNumber("123");
        assertNull(a.getCardNumber());
    }


}
