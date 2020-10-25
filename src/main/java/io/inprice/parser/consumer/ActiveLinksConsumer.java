package io.inprice.parser.consumer;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.models.Site;
import io.inprice.parser.helpers.RedisClient;
import io.inprice.parser.site.SiteFinder;
import io.inprice.parser.websites.Website;

public class ActiveLinksConsumer implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(ActiveLinksConsumer.class);

  private Link link;

  public ActiveLinksConsumer(Link link) {
    this.link = link;
  }

  @SuppressWarnings("incomplete-switch")
  @Override
  public void run() {
    LinkStatus oldStatus = link.getStatus();
    BigDecimal oldPrice = link.getPrice();

    boolean shouldBeHandled = true;

    switch (oldStatus) {

      case PAUSED: {
        if (!LinkStatus.PAUSED.equals(link.getStatus())) {
          link.setStatus(LinkStatus.PAUSED);
          shouldBeHandled = false;
        }
        break;
      }

      case RESUMED: {
        if (!LinkStatus.RESUMED.equals(link.getStatus())) {
          link.setStatus(link.getPreStatus());
          shouldBeHandled = false;
        }
        break;
      }

      case TOBE_CLASSIFIED: {
        shouldBeHandled = false;
        Site site = SiteFinder.findSiteByUrl(link.getUrl());
        if (site != null) {
          link.setSiteId(site.getId());
          link.setWebsiteClassName(site.getClassName());
          if (site.getStatus() != null) {
            link.setStatus(LinkStatus.valueOf(site.getStatus()));
          } else {
            shouldBeHandled = true;
          }
        } else {
          link.setStatus(LinkStatus.TOBE_IMPLEMENTED);
        }
        break;
      }

    }

    if (shouldBeHandled) {
      try {
        Class<?> clazz = Class.forName("io.inprice.parser.websites." + link.getWebsiteClassName());
        Website website = (Website) clazz.getConstructor().newInstance();
        website.check(link);
      } catch (Exception e) {
        log.error("Failed to find the website", e);
        link.setStatus(LinkStatus.INTERNAL_ERROR);
        link.setProblem(e.getMessage());
      }
    }

    //consumer is placed in Manager project!
    if (!oldPrice.equals(link.getPrice()) || !oldStatus.equals(link.getStatus())) {
      RedisClient.publishStatusChange(link, oldStatus, oldPrice);
    }
  }

}
