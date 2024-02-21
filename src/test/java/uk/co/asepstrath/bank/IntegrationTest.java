package uk.co.asepstrath.bank;
import static org.junit.jupiter.api.Assertions.*;
import io.jooby.test.JoobyTest;
import uk.co.asepstrath.bank.models.Account;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JoobyTest(App.class)
public class IntegrationTest {
    /*
    Integration tests should be here
    Example can be found in example/IntegrationTest.java
     */
    @Test

    public void testFetchAccountDataIntegration() {
        Manager manager = new Manager();
        ArrayList<Account> accounts = manager.fetchAccountData();

        // Assert that the fetched accounts list is not empty
        assertFalse(accounts.isEmpty());


    }
}
