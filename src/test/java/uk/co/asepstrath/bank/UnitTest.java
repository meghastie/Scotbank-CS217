package uk.co.asepstrath.bank;

import io.jooby.StatusCode;
import io.jooby.test.MockRouter;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.models.Account;

import javax.sql.DataSource;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTest {
    /*
    Unit tests should be here
    Example can be found in example/UnitTest.java
     */

//    @Test
//    public void HundredEntries(){
//        Manager manager = new Manager();
//        ArrayList<Account> accounts = manager.fetchAccountData();
//
//        assertEquals(accounts.size(),100);
//    }

    @Test
    public void accountsWebsiteActive(){
        MockRouter router = new MockRouter(new App());
        router.get("/bank/accounts", rsp -> {
            assertEquals(StatusCode.OK,rsp.getStatusCode());
        });
    }
}
