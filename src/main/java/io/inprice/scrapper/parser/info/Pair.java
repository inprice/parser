package io.inprice.scrapper.parser.info;

public class Pair {

  private int status;
  private String body;

  public Pair(int status) {
    this.status = status;
  }

  public Pair(String body) {
    this.status = 200;
    this.body = body;
  }

  public Pair(int status, String body) {
    this.status = status;
    this.body = body;
  }

  public int getStatus() {
    return status;
  }

  public String getBody() {
    return body;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
