package eval;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;

public class AccountBalanceHandler {

    private static final String SANDBOX_URL = "https://sandbox.platfr.io";
    private static final String ENDPOINT_TOKEN = "/api/gbs/banking/v2/accounts/{accountId}/balance";
    private static final String PARAM = "{accountId}";

    private File outputLog = new File("output_log.txt");

    public boolean showBalance(String accountId) {

        if(!accountId.matches("[0-9]+")){  //not sure if accountIds are just numbers or if they are fixed length
            throw new IllegalArgumentException();
        }

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(SANDBOX_URL + ENDPOINT_TOKEN.replace(PARAM, accountId));
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("Auth-Schema", "S2S");
            httpGet.addHeader("Api-key", "");
            try (CloseableHttpResponse response = client.execute(httpGet)) {
                try (Writer writer = new FileWriter(outputLog, true)) {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        writer.append(EntityUtils.toString(entity));
                        EntityUtils.consume(entity);
                        return true;
                    } else if (response.getStatusLine().getStatusCode() == 400) {
                        HttpEntity entity = response.getEntity();
                        writer.append(EntityUtils.toString(entity));
                        EntityUtils.consume(entity);
                        return false;
                    } else {
                        throw new HttpResponseException(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
                    }
                }
            }
        } catch (IOException e){
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
