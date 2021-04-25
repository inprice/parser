package io.inprice.parser.consumer;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.utils.StringUtils;
import io.inprice.parser.helpers.RedisClient;
import io.inprice.parser.websites.Website;

public class ConsumerActiveLinks implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(ConsumerActiveLinks.class);

  private static final String PACKAGE_PATH = "io.inprice.parser.websites.";

  private Link link;

  public ConsumerActiveLinks(Link link) {
    this.link = link;
  }

  @Override
  public void run() {
    LinkStatus oldStatus = link.getStatus();
    BigDecimal oldPrice = link.getPrice();

    if (link.getPlatform() != null) {
      try {
        Class<?> clazz = Class.forName(PACKAGE_PATH + link.getPlatform().getClassName());
        Website website = (Website) clazz.getConstructor().newInstance();
        website.check(link);
      } catch (Exception e) {
        link.setStatus(LinkStatus.INTERNAL_ERROR);
        link.setProblem(StringUtils.clearErrorMessage(e.getMessage()));
        link.setHttpStatus(500);
        log.error(link.getUrl(), e);
      }
    } else {
      log.warn("Website platform is null! Status: {}, Url: {} ", link.getStatus(), link.getUrl());
      link.setStatus(LinkStatus.TOBE_IMPLEMENTED);
      link.setProblem("NOT IMPLEMENTED YET");
      link.setHttpStatus(500);
    }

    RedisClient.publishStatusChange(link, oldStatus, oldPrice);
  }

}