package bilokhado.twitterrestclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Main application class to test Twitter's REST API
 */
public class Client {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Twitter REST API Client. Parameters required:");
            System.out.println(" consumer_key");
            System.out.println(" consumer_secret");
            System.out.println(" access_token");
            System.out.println(" token_secret");
            System.exit(-1);
        }
        String consumerKey = args[0];
        String consumerSecret = args[1];
        String accessToken = args[2];
        String tokenSecret = args[3];
        String endpoint = "http://twitter.com/search";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("q", "place:07d9cd6afd884001");
        TwitterConnection connection = TwitterConnection.getBuilder()
                .consumerKey(consumerKey)
                .consumerSecret(consumerSecret)
                .accessToken(accessToken)
                .tokenSecret(tokenSecret)
                .endpoint(endpoint)
                .queryParams(queryParams)
                .build();
        BufferedReader reader = connection.connect();
        try {
            for (String line; (line = reader.readLine()) != null; )
                System.out.println(line);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read line from Twitter", ex);
        } finally {
            connection.close();
        }
    }
}
