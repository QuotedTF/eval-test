package eval;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.*;

/**
 * Unit test for simple AccountBalanceHandler.
 */
public class CreateSCTOrderTest {

    private File SCTOrderTest = new File("/test_SCT.json");

    @Test
    public void sctOrderTest() {

        SCTOrderHandler handler = new SCTOrderHandler();
        Gson gson = new Gson();

        try (Reader reader = new FileReader(SCTOrderTest)) {
            JsonReader jsonReader = new JsonReader(reader);
            JsonObject arguments;
            arguments = gson.fromJson(jsonReader, new TypeToken<JsonElement>(){}.getType());
            assertTrue(handler.placeSctOrder("14930637", arguments.get("success")));
            assertFalse(handler.placeSctOrder("14930637", arguments.get("exception1")));
            assertFalse(handler.placeSctOrder("14930637", arguments.get("exception2")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}