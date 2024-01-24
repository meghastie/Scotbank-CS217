package uk.co.asepstrath.bank;
import static org.junit.jupiter.api.Assertions.*;
import io.jooby.test.JoobyTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JoobyTest(App.class)
public class IntegrationTest {
    /*
    Integration tests should be here
    Example can be found in example/IntegrationTest.java
     */
    @Test
    public void startup(){
        App a = new App();
        a.onStart();
        a.printList();
        assertTrue(a.getList().size() == 6);
    }
}
