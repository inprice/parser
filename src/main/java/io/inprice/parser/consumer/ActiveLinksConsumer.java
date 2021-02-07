package io.inprice.parser.consumer;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.RedisClient;
import io.inprice.parser.helpers.WebsiteHelper;
import io.inprice.parser.websites.Website;

public class ActiveLinksConsumer implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(ActiveLinksConsumer.class);

  private Link link;

  public ActiveLinksConsumer(Link link) {
    this.link = link;
  }

  @Override
  public void run() {
    if (! LinkStatus.PASSIVE_GROUP.equals(link.getStatus().getGroup())) {
      LinkStatus oldStatus = link.getStatus();
      BigDecimal oldPrice = link.getPrice();
  
      if (link.getPlatform() != null) {
        try {
          Website website = WebsiteHelper.findByClassName(link.getPlatform().getClassName());
          link = website.check(link);
        } catch (Exception e) {
          link.setStatus(LinkStatus.INTERNAL_ERROR);
          link.setProblem(e.getMessage());
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
    } else {
      log.warn("A passive link came to ActiveLinkConsumer! Status: {}, Url: {} ", link.getStatus(), link.getUrl());
    }
  }

}
