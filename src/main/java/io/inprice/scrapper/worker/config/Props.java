package io.inprice.scrapper.worker.config;

public class Props {

  public static boolean IS_RUN_FOR_DEV() {
    return !"prod".equals(System.getenv().getOrDefault("APP_ENV", "prod").toLowerCase());
  }

  public static int APP_WAITING_TIME() {
    return new Integer(System.getenv().getOrDefault("APP_WAITING_TIME", "30"));
  }

  public static String MQ_HOST() {
    return System.getenv().getOrDefault("MQ_HOST", "localhost");
  }

  public static int MQ_PORT() {
    return new Integer(System.getenv().getOrDefault("MQ_PORT", "5672"));
  }

  public static String MQ_USERNAME() {
    return System.getenv().getOrDefault("MQ_USERNAME", "guest");
  }

  public static String MQ_PASSWORD() {
    return System.getenv().getOrDefault("MQ_PASSWORD", "guest");
  }

  public static String MQ_EXCHANGE_LINKS() {
    return System.getenv().getOrDefault("MQ_EXCHANGE_LINKS", "links");
  }

  public static String MQ_EXCHANGE_CHANGES() {
    return System.getenv().getOrDefault("MQ_EXCHANGE_CHANGES", "changes");
  }

  public static String MQ_EXCHANGE_DEAD_LETTER() {
    return System.getenv().getOrDefault("MQ_EXCHANGE_DEAD_LETTER", "dead-letters");
  }

  public static String MQ_ROUTING_DELETED_LINKS() {
    return System.getenv().getOrDefault("MQ_ROUTING_DELETED_LINKS", "deleted-links");
  }

  public static String MQ_ROUTING_TOBE_AVAILABLE_LINKS() {
    return System.getenv().getOrDefault("MQ_ROUTING_TOBE_AVAILABLE_LINKS", "tobe-available-links");
  }

  public static String MQ_ROUTING_STATUS_CHANGES() {
    return System.getenv().getOrDefault("MQ_ROUTING_STATUS_CHANGES", "status-change");
  }

  public static String MQ_ROUTING_PRICE_CHANGES() {
    return System.getenv().getOrDefault("MQ_ROUTING_PRICE_CHANGES", "price-change");
  }

  public static String MQ_QUEUE_NEW_LINKS() {
    return System.getenv().getOrDefault("MQ_QUEUE_NEW_LINKS", "new-links");
  }

  public static String MQ_QUEUE_AVALIABLE_LINKS() {
    return System.getenv().getOrDefault("MQ_QUEUE_AVALIABLE_LINKS", "available-links");
  }

  public static String MQ_QUEUE_FAILED_LINKS() {
    return System.getenv().getOrDefault("MQ_QUEUE_FAILED_LINKS", "failed-links");
  }

}
