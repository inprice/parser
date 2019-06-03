package io.inprice.scrapper.worker.helpers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.inprice.scrapper.common.logging.Logger;

import java.util.Map;

public class HttpClient {

    protected static final Logger log = new Logger(HttpClient.class);

    public static HttpResponse<String> get(String url) {
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(url)
                    .header("User-Agent", UserAgents.findARandomUA())
                    .header("Referrer", UserAgents.findARandomReferer())
                    .asString();
        } catch (UnirestException e) {
            log.error(e);
        }
        return response;
    }

    public static HttpResponse<String> get(String url, Map<String, String> headers) {
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(url)
                    .headers(headers)
                    .header("User-Agent", UserAgents.findARandomUA())
                    .header("Referrer", UserAgents.findARandomReferer())
                    .asString();
        } catch (UnirestException e) {
            log.error(e);
        }
        return response;
    }

    public static HttpResponse<String> post(String url) {
        HttpResponse<String> response = null;
        try {
            response = Unirest.post(url)
                    .header("User-Agent", UserAgents.findARandomUA())
                    .header("Referrer", UserAgents.findARandomReferer())
                    .asString();
        } catch (UnirestException e) {
            log.error(e);
        }
        return response;
    }

    public static HttpResponse<String> post(String url, String data) {
        HttpResponse<String> response = null;
        try {
            response = Unirest.post(url)
                    .header("User-Agent", UserAgents.findARandomUA())
                    .header("Referrer", UserAgents.findARandomReferer())
                    .body(data)
                    .asString();
        } catch (UnirestException e) {
            log.error(e);
        }
        return response;
    }

}
