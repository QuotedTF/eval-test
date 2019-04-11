package eval;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Hello world!
 */
public class AccountBalanceHandler {

    private static final String SANDBOX_URL = "https://sandbox.platfr.io";
    private static final String ENDPOINT_TOKEN = "/api/gbs/banking/v2/accounts/{accountId}/balance";
    private static final String PARAM = "{accountId}";

    private File outputLog = new File("output_log.txt");

    public boolean showBalance(String accountId) {

        try {
            URL url = new URL(SANDBOX_URL + ENDPOINT_TOKEN.replace(PARAM, accountId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Auth-Schema", "S2S");
            connection.addRequestProperty("Api-key", "");
            InputStream response = connection.getInputStream();
            //test
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
            }
            //check response status, print to file if good, handle 400 if bad
            JsonParser parser = new JsonParser();
            JsonElement jsonResponse = parser.parse(connection.getResponseMessage());
            Writer writer = new FileWriter(outputLog);
            if (connection.getResponseCode() == 200) {
                writer.append("\n").append(jsonResponse.getAsJsonObject().get("payload").getAsString()).append("\n");
                return true;
            } else if (connection.getResponseCode() == 400) {
                writer.append("\n").append(jsonResponse.getAsJsonObject().get("error").getAsString()).append("\n");
                return false;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean createLog() {
        try {
            return outputLog.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean clearLog() {
        outputLog.delete();
        return createLog();
    }
}
