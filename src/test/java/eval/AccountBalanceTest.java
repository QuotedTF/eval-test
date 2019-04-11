package eval;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class AccountBalanceTest {

    private File outputLog = new File("output_log.txt");

    @Test
    public void getAccountBalanceTest(){

        final AccountBalanceHandler handler = new AccountBalanceHandler();

        assertTrue(handler.showBalance("14930637"));
        assertThrows(IllegalArgumentException.class, () -> handler.showBalance("notAnInteger"));
    }
}
