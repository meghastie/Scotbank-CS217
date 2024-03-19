package uk.co.asepstrath.bank;
import static org.junit.jupiter.api.Assertions.*;

import io.jooby.exception.StatusCodeException;
import io.jooby.test.JoobyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import uk.co.asepstrath.bank.controllers.Accounts;
import uk.co.asepstrath.bank.models.Account;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.services.HelperMethods;
import uk.co.asepstrath.bank.services.XmlParser;

import javax.activation.DataSource;
import java.sql.Connection;
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
        ArrayList<Account> accounts = HelperMethods.getAccountList();

        // Assert that the fetched accounts list is not empty
        assertFalse(accounts.isEmpty());


    }

    @Test
    public void xmlParserReturnString(){
        Assertions.assertNotNull(XmlParser.Parser());
    }

}
