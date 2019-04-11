package eval;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SCTOrderHandler {

    private static final String SANDBOX_URL = "https://sandbox.platfr.io";
    private static final String ENDPOINT_TOKEN = "/api/gbs/banking/v2.1/accounts/{accountId}/payments/sct/orders";
    private static final String PARAM = "{accountId}";

    public boolean placeSctOrder(String accountId, JsonElement requestBody) {

        Gson gson = new Gson();

        try {
            URL url = new URL(SANDBOX_URL + ENDPOINT_TOKEN.replace(PARAM, accountId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            //set headers
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Auth-Schema", "S2S");
            connection.addRequestProperty("Api-key", "");

            //TODO: set body parameters

            InputStream response = connection.getInputStream();
            //test
            try(Scanner scanner = new Scanner(response)){
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
            }
            //check response status, print to file if good, handle 400 if bad
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
