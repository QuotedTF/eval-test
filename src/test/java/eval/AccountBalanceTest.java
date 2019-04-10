package eval;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for AccountBalanceHandler.
 */
public class AccountBalanceTest {

    private File outputLog = new File("/output_log_balance.txt");

    public void getAccountBalanceTest(){

        outputLog.delete();
        final AccountBalanceHandler handler = new AccountBalanceHandler();

        assertTrue(handler.showBalance("14930637"));
        assertTrue(outputLog.exists());
        assertFalse(handler.showBalance("thisIsNotANumber"));
    }
}
