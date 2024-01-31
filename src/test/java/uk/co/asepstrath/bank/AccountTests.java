package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTests {

    @Test
    public void createAccount(){
        Account a = new Account(0);
        Assertions.assertTrue(a.getBalance()==0);

    }
    @Test
    public void testAddFund(){
        Account a = new Account(0);
     a.deposit(50);
        Assertions.assertEquals(50, a.getBalance());
        a.deposit(20);

        Assertions.assertEquals(70, a.getBalance());
    }
    @Test
    public void testShoppingSpree(){
        Account a = new Account(0);
        Assertions.assertEquals(5.45, a.deposit(5.45));
        Assertions.assertEquals(23.01, a.deposit(17.56));
        Assertions.assertEquals(23, a.withdraw(0.01));
            Assertions.assertThrows(ArithmeticException.class,() -> a.withdraw(50));
        App app = new App();
        app.onStart();
    }


}
