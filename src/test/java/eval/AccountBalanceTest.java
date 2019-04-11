package eval;

import org.junit.Test;

import java.io.File;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for AccountBalanceHandler.
 */
public class AccountBalanceTest {

    private File outputLog = new File("output_log.txt");

    @Test
    public void getAccountBalanceTest(){

        final AccountBalanceHandler handler = new AccountBalanceHandler();

        assertTrue(handler.showBalance("14930637"));
        //assertTrue(handler.showBalance(String.valueOf(random.nextInt(100000000)))); ???
        assertTrue(outputLog.exists());
        assertFalse(handler.showBalance("thisIsNotANumber"));
    }
}
