package com.castaware.nocturne.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

@SuppressWarnings("deprecation")
public class BinanceRequest 
{
	// LOG
	private final Logger LOG = LoggerFactory.getLogger(getClass());

    public String userAgent = "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0";
    public HttpsURLConnection conn = null;
    public String requestUrl = "";
    public String method = "GET";
    public String lastResponse = "";

    public String apiKey = "";
    public String secretKey = "";

    public Map<String, String> headers = new HashMap<>();

    // Internal JSON parser    
	private JsonParser jsonParser = new JsonParser();
    private String requestBody = "";

    public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public HttpsURLConnection getConn() {
		return conn;
	}

	public void setConn(HttpsURLConnection conn) {
		this.conn = conn;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getLastResponse() {
		return lastResponse;
	}

	public void setLastResponse(String lastResponse) {
		this.lastResponse = lastResponse;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public JsonParser getJsonParser() {
		return jsonParser;
	}

	public void setJsonParser(JsonParser jsonParser) {
		this.jsonParser = jsonParser;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	// Creating public request
    public BinanceRequest(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    // HMAC encoding
    public static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    /**
     * Requests signing - with public and secret key
     * @param apiKey string of public API Key
     * @param secretKey string of secret Key
     * @param options map of additional parameters to include
     * @return this request object
     * @throws BinanceException in case of any error
     */
    public BinanceRequest sign(String apiKey, String secretKey) throws BinanceException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(apiKey))
            throw new BinanceException("Missing BINANCE_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(secretKey))
            throw new BinanceException("Missing BINANCE_SECRET_KEY. " + humanMessage);

        if (!Strings.isNullOrEmpty(secretKey) && !requestUrl.contains("&signature=")) {
            
            String queryToAdd = "";
            String query = "";
            //log.debug("Signature: RequestUrl = {}", requestUrl);
            if (requestUrl.contains("?")) {
                query = requestUrl.substring(requestUrl.indexOf('?') + 1) + "&";
            }
            query = query.concat(queryToAdd);

            //log.debug("Signature: query to be included  = {} queryToAdd={}", query, queryToAdd);
            try {
                String signature = encode(secretKey, query); // set the HMAC hash header
                String concatenator = requestUrl.contains("?") ? "&" : "?";
                requestUrl += concatenator + queryToAdd + "&signature=" + signature;
            } catch (Exception e ) {
                throw new BinanceException("Encryption error " + e.getMessage());
            }
        }
        headers.put("X-MBX-APIKEY", apiKey);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return this;
    }

    public BinanceRequest sign(String apiKey, String secretKey, String asset, Map<String, String> options) throws BinanceException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(apiKey))
            throw new BinanceException("Missing BINANCE_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(secretKey))
            throw new BinanceException("Missing BINANCE_SECRET_KEY. " + humanMessage);

        if (!Strings.isNullOrEmpty(secretKey) && !requestUrl.contains("&signature=")) {
            List<String> list = new LinkedList<>();
            if (options != null) {
                for (String key : options.keySet()) {
                    list.add(key + "=" + options.get(key));
                }
            }
            list.add("recvWindow=" + 7000);
            list.add("projectId=" + asset);
            list.add("timestamp=" + String.valueOf(new Date().getTime()-2000));
            String queryToAdd = String.join("&", list);
            String query = "";
            //log.debug("Signature: RequestUrl = {}", requestUrl);
            if (requestUrl.contains("?")) {
                query = requestUrl.substring(requestUrl.indexOf('?') + 1) + "&";
            }
            query = query.concat(queryToAdd);

            //log.debug("Signature: query to be included  = {} queryToAdd={}", query, queryToAdd);
            try {
                String signature = encode(secretKey, query); // set the HMAC hash header
                String concatenator = requestUrl.contains("?") ? "&" : "?";
                requestUrl += concatenator + queryToAdd + "&signature=" + signature;
            } catch (Exception e ) {
                throw new BinanceException("Encryption error " + e.getMessage());
            }
        }
        headers.put("X-MBX-APIKEY", apiKey);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return this;
    }
    
    /**
     * Requests signing - just with a public key
     * @param apiKey public key string
     * @return this request object
     * @throws BinanceException in case of any error
     */
    public BinanceRequest sign(String apiKey) throws BinanceException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(apiKey))
            throw new BinanceException("Missing BINANCE_API_KEY. " + humanMessage);

        headers.put("X-MBX-APIKEY", apiKey);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return this;
    }

    /**
     * Settings method as post, keeping interface fluid
     * @return this request object
     */
    public BinanceRequest post() {
        this.setMethod("POST");
        return this;
    }

    /**
     * Settings method as PUT, keeping interface fluid
     * @return this request object
     */
    public BinanceRequest put() {
        this.setMethod("PUT");
        return this;
    }


    /**
     * Settings method as DELETE, keeping interface fluid
     * @return this request object
     */
    public BinanceRequest delete() {
        this.setMethod("DELETE");
        return this;
    }

    /**
     * Opens HTTPS connection and save connection Handler
      @return this request object
     * @throws BinanceException in case of any error
     */
    public BinanceRequest connect() throws BinanceException {

        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        URL url = null;
        try {
            url = new URL(requestUrl);
            //log.debug("{} {}", getMethod(), url);
        } catch (MalformedURLException e) {
            throw new BinanceException("Mailformed URL " + e.getMessage());
        }
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            throw new BinanceException("SSL Error " + e.getMessage() );
        } catch (KeyManagementException e) {
            throw new BinanceException("Key Management Error " + e.getMessage() );
        }

        try {
            conn = (HttpsURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new BinanceException("HTTPS Connection error " + e.getMessage());
        }

        try {
            conn.setRequestMethod(method);
        } catch (ProtocolException e) {
            throw new BinanceException("HTTP method error " + e.getMessage());
        }
        conn.setRequestProperty("User-Agent", getUserAgent());
        for(String header: headers.keySet()) {
            conn.setRequestProperty(header, headers.get(header));
        }
        return this;
    }

    /**
     * Saving response into local string variable
     * @return this request object
     * @throws BinanceException in case of any error
     */
    public BinanceRequest read() throws BinanceException {
        if (conn == null) {
            connect();
        }
        try {

            // posting payload it we do not have it yet
            if (!Strings.isNullOrEmpty(getRequestBody())) {
                //log.debug("Payload: {}", getRequestBody());
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(getRequestBody());
                writer.close();
            }

            InputStream is;
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                is = conn.getInputStream();
            } else {
                /* error from server */
                is = conn.getErrorStream();
            }

            BufferedReader br = new BufferedReader( new InputStreamReader(is));
            lastResponse = IOUtils.toString(br);
                        
            if (conn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) 
            {
                // Try to parse JSON            
            	try
            	{
	                JsonObject obj = (JsonObject)jsonParser.parse(lastResponse);
	                if (obj.has("code") && obj.has("msg")) {
	                    throw new BinanceException("ERROR: " +
	                            obj.get("code").getAsString() + ", " + obj.get("msg").getAsString() );
	                }
            	}
            	catch (JsonSyntaxException e) 
                {
            		LOG.error("================================");
            		LOG.error("=== ERROR IN BINANCE REQUEST ===");
            		LOG.error("================================");
            		LOG.error("URL: "+requestUrl);
            		LOG.error("Message: "+e.getMessage());
            		LOG.error("Responde: "+lastResponse);
            		
                    throw new BinanceException("Error in binance request",e);
                }   
            }
        }            
        catch (IOException e) 
        {
            throw new BinanceException("Error in reading response " + e.getMessage());
        }
        return this;
    }
    
    public BinanceRequest readLenient() throws BinanceException {
        if (conn == null) {
            connect();
        }
        try {

            // posting payload it we do not have it yet
            if (!Strings.isNullOrEmpty(getRequestBody())) {
                //log.debug("Payload: {}", getRequestBody());
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(getRequestBody());
                writer.close();
            }

            InputStream is;
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                is = conn.getInputStream();
            } else {
                /* error from server */
                is = conn.getErrorStream();
            }

            BufferedReader br = new BufferedReader( new InputStreamReader(is));
//            lastResponse = IOUtils.toString(br);
//            log.debug("Response: {}", lastResponse);

            if (conn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                // Try to parse JSON            
            	JsonReader reader = new JsonReader(br);
            	reader.setLenient(true);
                JsonElement ret = jsonParser.parse(reader);
				JsonObject obj = (JsonObject)ret;
                if (obj.has("code") && obj.has("msg")) {
                    throw new BinanceException("ERROR: " +
                            obj.get("code").getAsString() + ", " + obj.get("msg").getAsString() );
                }
            }
        } catch (IOException e) {
            throw new BinanceException("Error in reading response " + e.getMessage());
        }
        return this;
    }

    public BinanceRequest payload(JsonObject payload) {
        if (payload == null) return this; // this is a valid case
        // according to documentation we need to have this header if we have preload
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        this.requestBody = payload.toString();
        return this;
    }

    /**
     * Getting last response as google JsonObject
     * @return response as Json Object
     */
    public JsonObject asJsonObject() {
        return (JsonObject)jsonParser.parse(getLastResponse());
    }
    /**
     * Getting last response as google GAON JsonArray
     * @return response as Json Array
     */
    public JsonArray asJsonArray() {
        return (JsonArray)jsonParser.parse(getLastResponse());
    }
    
    

}
