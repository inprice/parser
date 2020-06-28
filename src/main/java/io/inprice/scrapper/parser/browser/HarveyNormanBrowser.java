package io.inprice.scrapper.parser.browser;

import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Please be careful!!! Some sites like HarveyNorman are guarded by a security
 * software like incapsulata and it can sense us if the parameter below is set
 *
 * caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", false);
 */
class HarveyNormanBrowser extends AbstractBrowser {

  @Override
  public String getName() {
    return "www.harveynorman.com.au";
  }

  @Override
  public void addSpecificCapabilities(DesiredCapabilities caps) {
    caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Host", getName());
  }

}