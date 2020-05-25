package io.inprice.scrapper.worker.helpers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpClient {

  protected static final Logger log = LoggerFactory.getLogger(HttpClient.class);

  public HttpResponse<String> get(String url) {
    HttpResponse<String> response = null;
    try {
      response = Unirest.get(url).header("Accept-Language", "en-US,en;q=0.5")
          .header("User-Agent", UserAgents.findARandomUA()).asString();
    } catch (UnirestException e) {
      log.error("Failed to make a GET request", e);
    }
    return response;
  }

  public HttpResponse<String> get(String url, String referrer) {
    HttpResponse<String> response = null;
    try {
      response = Unirest.get(url).header("Accept-Language", "en-US,en;q=0.5")
          .header("User-Agent", UserAgents.findARandomUA()).header("Referrer", referrer).asString();
    } catch (UnirestException e) {
      log.error("Failed to make a GET request with a referrer", e);
    }
    return response;
  }

  public HttpResponse<String> get(String url, Map<String, String> headers) {
    HttpResponse<String> response = null;
    try {
      response = Unirest.get(url).headers(headers).header("User-Agent", UserAgents.findARandomUA())
          .header("Referrer", UserAgents.findARandomReferer()).asString();
    } catch (UnirestException e) {
      log.error("Failed to make a GET request with headers", e);
    }
    return response;
  }

  public HttpResponse<String> post(String url) {
    HttpResponse<String> response = null;
    try {
      response = Unirest.post(url).header("User-Agent", UserAgents.findARandomUA())
          .header("Referrer", UserAgents.findARandomReferer()).asString();
    } catch (UnirestException e) {
      log.error("Failed to make a POST request", e);
    }
    return response;
  }

  public HttpResponse<String> post(String url, String data) {
    HttpResponse<String> response = null;
    try {
      response = Unirest.post(url).header("Accept-Language", "en-US,en;q=0.5")
          .header("User-Agent", UserAgents.findARandomUA()).header("Referrer", UserAgents.findARandomReferer())
          .body(data).asString();
    } catch (UnirestException e) {
      log.error("Failed to make a POST request with data", e);
    }
    return response;
  }

}
