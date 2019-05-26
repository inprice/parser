package io.inprice.scrapper.worker.info;

public class HtmlResponse {

    private int status;
    private String html;

    public HtmlResponse(int status) {
        this.status = status;
    }

    public HtmlResponse(int status, String html) {
        this.status = status;
        this.html = html;
    }

    public int getStatus() {
        return status;
    }

    public String getHtml() {
        return html;
    }
}
