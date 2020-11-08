package io.inprice.parser.consumer;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.helpers.SiteFinder;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.models.Site;
import io.inprice.parser.helpers.RedisClient;
import io.inprice.parser.websites.Website;

public class ActiveLinksConsumer implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(ActiveLinksConsumer.class);
  private static final String PACKAGE_PATH = "io.inprice.parser.websites.";

  private Link link;

  public ActiveLinksConsumer(Link link) {
    this.link = link;
  }

  @Override
  public void run() {
    if (! LinkStatus.PASSIVE_GROUP.equals(link.getStatus().getGroup())) {
      LinkStatus oldStatus = link.getStatus();
      BigDecimal oldPrice = link.getPrice();
  
      if (LinkStatus.TOBE_CLASSIFIED.equals(link.getStatus())) {
        Site site = SiteFinder.findSiteByUrl(link.getUrl());
        if (site != null) {
          link.setPlatform(site.getDomain());
          link.setClassName(site.getClassName());
          if (site.getStatus() != null) {
            link.setStatus(LinkStatus.valueOf(site.getStatus()));
          }
        } else {
          link.setStatus(LinkStatus.TOBE_IMPLEMENTED);
        }
      }

      //status may have been made passive in the previous code block, let's check once again
      if (! LinkStatus.PASSIVE_GROUP.equals(link.getStatus().getGroup())) {
        if (link.getClassName() != null) {
          try {
            Class<?> clazz = Class.forName(PACKAGE_PATH + link.getClassName());
            Website website = (Website) clazz.getConstructor().newInstance();
            website.check(link);
          } catch (Exception e) {
            log.error(link.getUrl(), e);
            link.setStatus(LinkStatus.INTERNAL_ERROR);
            link.setProblem(e.getMessage());
          }
        } else {
          log.warn("Website class name is null! Status: {}, Url: {} ", link.getStatus(), link.getUrl());
          link.setStatus(LinkStatus.TOBE_IMPLEMENTED);
          link.setProblem("UNIMPLEMENTED WEBSITE");
        }
      }

      RedisClient.publishStatusChange(link, oldStatus, oldPrice);
    } else {
      log.warn("A passive link came to ActiveLinkConsumer! Status: {}, Url: {} ", link.getStatus(), link.getUrl());
    }
  }

}
