package uk.co.asepstrath.bank;

import uk.co.asepstrath.bank.models.Transactions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    Transactions t;
    @BeforeEach
    public void setup(){t = new Transactions("21/02/24",53.64,"mum","123456","kath", "donation");
    }
    @Test
    void getTime() {
        assertTrue(t.getTime().equals("21/02/24"));
    }

    @Test
    void getAmount() {
        assertEquals(t.getAmount(), 53.64);
    }

    @Test
    void getFrom() {
        assertTrue(t.getFrom().equals("mum"));
    }

    @Test
    void getID() {
        assertTrue(t.getID().equals("123456"));
    }

    @Test
    void getTo() {
        assertTrue(t.getTo().equals("kath"));
    }

    @Test
    void getType() {
        assertTrue(t.getType().equals("donation"));
    }
}