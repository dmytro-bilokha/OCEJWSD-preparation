package bilokhado.twitterrestclient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Class provides connection to the Twitter REST API with handling authentication
 */
public class TwitterConnection {

    private static final SecureRandom randomGenerator = new SecureRandom();

    private String endpoint;
    private SortedMap<String, String> queryParams;
    private Map<String, String> requestHeaders;
    private String consumerKey;
    private String accessToken;
    private String consumerSecret;
    private String tokenSecret;
    private URLConnection connection;
    private BufferedReader reader;

    private TwitterConnection(Builder builder) {
        this.endpoint = builder.endpoint;
        this.queryParams = new TreeMap<>(builder.queryParams);
        this.requestHeaders = createHeadersMap();
        this.consumerKey = builder.consumerKey;
        this.accessToken = builder.accessToken;
        this.tokenSecret = builder.tokenSecret;
        this.consumerSecret = builder.consumerSecret;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    private Map<String, String> createHeadersMap() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Accept", "*/*");
        headersMap.put("User-Agent", "Java custom REST client");
        return headersMap;
    }

    public BufferedReader connect() {
        if (reader != null)
            throw new IllegalStateException("Connection is already opened");
        NavigableMap<String, String> oauthMap = generateOauthMap();
        addSignature(oauthMap);
        generateAuthorizationHeader(oauthMap);
        reader = openConnectionAndGetReader();
        return reader;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException ex) {
            //Ignore, we are closing anyway
        }

    }

    private NavigableMap<String, String> generateOauthMap() {
        NavigableMap<String, String> oauthMap = new TreeMap<>();
        oauthMap.put("oath_consumer_key", consumerKey);
        oauthMap.put("oauth_signature_method", "HMAC-SHA1");
        oauthMap.put("oauth_token", accessToken);
        oauthMap.put("oauth_version", "1.0");
        oauthMap.put("oauth_timestamp", Long.toString(System.currentTimeMillis()/1000L));
        oauthMap.put("oath_nonce", getRandomString());
        return oauthMap;
    }

    private void addSignature(Map<String, String> oauthMap) {
        String signatureBase = getSignatureBase(getAuthParamString(oauthMap));
        oauthMap.put("oauth_signature", sign(signatureBase));
    }

    private String getAuthParamString(Map<String, String> oauthMap) {
        Map<String, String> signingParams = new HashMap<>(oauthMap);
        signingParams.putAll(queryParams);
        NavigableMap<String, String> percentEncodedParams = new TreeMap<>();
        for (Map.Entry<String, String> paramEntry : signingParams.entrySet()) {
            percentEncodedParams.put(percentEncode(paramEntry.getKey()), percentEncode(paramEntry.getValue()));
        }
        StringBuilder authParamStringBuilder = new StringBuilder();
        for (Map.Entry<String, String> encodedParamEntry = percentEncodedParams.firstEntry();
             encodedParamEntry != null;
             encodedParamEntry = percentEncodedParams.higherEntry(encodedParamEntry.getKey())) {
            if (authParamStringBuilder.length() > 0)
                authParamStringBuilder.append('&');
            authParamStringBuilder.append(encodedParamEntry.getKey());
            authParamStringBuilder.append('=');
            authParamStringBuilder.append(encodedParamEntry.getValue());
        }
        return authParamStringBuilder.toString();
    }

    private String getSignatureBase(String authParam) {
        StringBuilder signatureBaseBuilder = new StringBuilder("GET&");
        signatureBaseBuilder.append(percentEncode(endpoint));
        signatureBaseBuilder.append('&');
        signatureBaseBuilder.append(percentEncode(authParam));
        return signatureBaseBuilder.toString();
    }

    private String percentEncode(String data) {
        try {
            return URLEncoder.encode(data, "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Unable to URLencode UTF-8 string '" + data + "'", ex);
        }
    }

    private String getRandomString() {
        byte[] randomBytes = new byte[32];
        randomGenerator.nextBytes(randomBytes);
        return Base64.getEncoder().withoutPadding().encodeToString(randomBytes).replaceAll("[^a-zA-Z0-9]", "");
    }

    private String sign(String signBase) {
        String key = percentEncode(consumerSecret) + '&' + percentEncode(tokenSecret);
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        byte[] hashBytes;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            hashBytes = mac.doFinal(signBase.getBytes());
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new RuntimeException("Unable to sign the request", ex);
        }
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    private void generateAuthorizationHeader(NavigableMap<String, String> oauthMap) {
        StringBuilder headerBuilder = new StringBuilder();
        for (Map.Entry<String, String> oauthParamEntry = oauthMap.firstEntry();
             oauthParamEntry != null;
             oauthParamEntry = oauthMap.higherEntry(oauthParamEntry.getKey())) {
            if (headerBuilder.length() > 0)
                headerBuilder.append(", ");
            headerBuilder.append(oauthParamEntry.getKey());
            headerBuilder.append("=\"");
            headerBuilder.append(oauthParamEntry.getValue());
            headerBuilder.append('"');
        }
        requestHeaders.put("Authorization", "OAauth " + headerBuilder.toString());
    }

    private BufferedReader openConnectionAndGetReader() {
        StringBuilder urlBuilder = new StringBuilder();
        for (Map.Entry<String, String> queryParam : queryParams.entrySet()) {
            if (urlBuilder.length() > 0)
                urlBuilder.append('&');
            urlBuilder.append(percentEncode(queryParam.getKey()));
            urlBuilder.append('=');
            urlBuilder.append(percentEncode(queryParam.getValue()));
        }
        String url;
        if (urlBuilder.length() > 0)
            url = endpoint + '?' + urlBuilder.toString();
        else
            url = endpoint;
        try {
            connection = new URL(url).openConnection();
        } catch (IOException ex) {
            throw new RuntimeException("Unable to create URL object from string:" + url, ex);
        }
        connection.setDoInput(true);
        for (Map.Entry<String, String> header : requestHeaders.entrySet())
            connection.setRequestProperty(header.getKey(), header.getValue());
        try {
            return new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException ex) {
            throw new RuntimeException("Unable to connect with URL=" + url, ex);
        }
    }

    public static class Builder {

        private String endpoint;
        private Map<String, String> queryParams;
        private String consumerKey;
        private String accessToken;
        private String consumerSecret;
        private String tokenSecret;

        private Builder() {
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder queryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder consumerKey(String key) {
            this.consumerKey = key;
            return this;
        }

        public Builder accessToken(String token) {
            this.accessToken = token;
            return this;
        }

        public Builder consumerSecret(String consumerSecret) {
            this.consumerSecret = consumerSecret;
            return this;
        }

        public Builder tokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
            return this;
        }

        public TwitterConnection build() {
            if (endpoint == null || endpoint.isEmpty())
                throw new IllegalStateException("Cannot build connection with null/empty endpoint parameter");
            if (queryParams == null)
                queryParams = Collections.emptyMap();
            if (consumerKey == null || consumerKey.isEmpty())
                throw new IllegalStateException("Cannot build connection with null/empty consumerKey parameter");
            if (accessToken == null || accessToken.isEmpty())
                throw new IllegalStateException("Cannot build connection with null/empty accessToken parameter");
            if (consumerSecret == null || consumerSecret.isEmpty())
                throw new IllegalStateException("Cannot build connection with null/empty consumerSecret parameter");
            if (tokenSecret == null || tokenSecret.isEmpty())
                throw new IllegalStateException("Cannot build connection with null/empty tokenSecret parameter");
            return new TwitterConnection(this);
        }

    }

}
