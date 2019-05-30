package io.inprice.scrapper.worker.helpers;

import io.inprice.scrapper.common.logging.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpClient {

    protected static final Logger log = new Logger(HttpClient.class);

    public static String get(String getUrl) {
        return get(getUrl, null);
    }

    public static String get(String getUrl, Map<String, String> headers) {
        try {
            HttpURLConnection con = getConnection("GET", getUrl, headers);
            return HttpClient.read(con.getInputStream());
        } catch (Exception e) {
            log.error("Failed to connect to " + getUrl, e);
        }
        return null;
    }

    public static String post(String postUrl) {
        return post(postUrl, null, null);
    }

    public static String post(String postUrl, String data) {
        return post(postUrl, null, data);
    }

    public static String post(String postUrl, Map<String, String> headers, String data) {
        try {
            HttpURLConnection con = getConnection("POST", postUrl, headers);
            con.setDoOutput(true);
            HttpClient.sendData(con, data);

            return HttpClient.read(con.getInputStream());
        } catch (Exception e) {
            log.error("Failed to connect to " + postUrl, e);
        }
        return null;
    }

    private static HttpURLConnection getConnection(String method, String strUrl, Map<String, String> headers) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", UserAgents.findARandomUA());
        con.setRequestProperty("Referrer", UserAgents.findARandomReferer());

        if (headers != null && ! headers.isEmpty()) {
            for (Map.Entry<String, String> entry: headers.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return con;
    }

    private static void sendData(HttpURLConnection con, String data) {
        DataOutputStream wr = null;
        try {
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();
        } catch(IOException ioe) {
            log.error(ioe);
        } finally {
            HttpClient.closeQuietly(wr);
        }
    }

    private static String read(InputStream is) {
        BufferedReader in = null;
        String inputLine;
        StringBuilder body;
        try {
            in = new BufferedReader(new InputStreamReader(is));
            body = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                body.append(inputLine);
            }
            in.close();
            return body.toString();
        } catch(IOException ioe) {
            log.error(ioe);
        } finally {
            HttpClient.closeQuietly(in);
        }

        return null;
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            if( closeable != null ) {
                closeable.close();
            }
        } catch(IOException ex) {
            //
        }
    }
}
