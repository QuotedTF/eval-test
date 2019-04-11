package eval;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class CreateSCTOrderTest {

    private File SCTOrderTest = new File("test_SCT.json");

    @Test
    public void sctOrderTest() {

        final SCTOrderHandler handler = new SCTOrderHandler();
        Gson gson = new Gson();

        try (Reader reader = new FileReader(SCTOrderTest)) {
            JsonReader jsonReader = new JsonReader(reader);
            JsonObject arguments;
            arguments = gson.fromJson(jsonReader, new TypeToken<JsonElement>(){}.getType());
            assertTrue(handler.placeSctOrder("14930637", arguments.get("success").getAsJsonObject()));
            assertThrows(IllegalArgumentException.class, () -> handler.placeSctOrder("14930637", arguments.get("exception1").getAsJsonObject()));
            assertThrows(IllegalArgumentException.class, () -> handler.placeSctOrder("notAnInteger", arguments.get("exception2").getAsJsonObject()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}