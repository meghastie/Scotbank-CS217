package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.services.XmlParser;

import static org.junit.jupiter.api.Assertions.*;

class XmlParserTest {

    @Test
    void parser() {
        Assertions.assertNotNull(XmlParser.Parser());
    }
}