package eval;

import com.google.gson.JsonObject;

import org.apache.commons.validator.routines.IBANValidator;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;

public class SCTOrderHandler {

    private static final String SANDBOX_URL = "https://sandbox.platfr.io";
    private static final String ENDPOINT_TOKEN = "/api/gbs/banking/v2.1/accounts/{accountId}/payments/sct/orders";
    private static final String PARAM = "{accountId}";

    private File outputLog = new File("output_log.txt");

    public boolean placeSctOrder(String accountId, JsonObject requestBody) {

        if(!accountId.matches("[0-9]+")){  //not sure if accountIds are just numbers or if they are fixed length
            throw new IllegalArgumentException();
        }

        if(!requestBody.get("receiverIban").getAsString().matches("IT\\d{2}[A-Z]{1}\\d{10}[A-Z0-9]{12}")){
            throw new IllegalArgumentException();
        }

        //tried to use this but it didn't work, ended up extracting the regex from this and using it alone
        /*IBANValidator validator = new IBANValidator();
        //validator.setValidator("IT", 27, "IT\\d{2}[A-Z]{1}\\d{10}[A-Z0-9]{12}");
        if(!validator.isValid(requestBody.get("receiverIban").getAsString())){
            throw new IllegalArgumentException();
        }*/

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(SANDBOX_URL + ENDPOINT_TOKEN.replace(PARAM, accountId));
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Auth-Schema", "S2S");
            httpPost.addHeader("Api-key", "");

            String json = requestBody.toString();
            StringEntity jsonRequest = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(jsonRequest);

            try (CloseableHttpResponse response = client.execute(httpPost)) {
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
        }
        return true;
    }
}
