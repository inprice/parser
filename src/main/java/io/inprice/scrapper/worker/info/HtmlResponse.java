package io.inprice.scrapper.worker.info;

public class HtmlResponse {

    private int status;
    private String body;

    public HtmlResponse(int status) {
        this.status = status;
    }

    public HtmlResponse(String body) {
        this.status = 200;
        this.body = body;
    }

    public HtmlResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
