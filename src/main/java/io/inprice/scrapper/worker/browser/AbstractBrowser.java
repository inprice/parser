package io.inprice.scrapper.worker.browser;

import io.inprice.scrapper.worker.helpers.UserAgents;
import io.inprice.scrapper.worker.info.Pair;
import org.openqa.selenium.Platform;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class AbstractBrowser {

    private static final Logger log = LoggerFactory.getLogger(AbstractBrowser.class);

    private static PhantomJSDriver driver;

    public abstract String getName();
    public abstract void addSpecificCapabilities(DesiredCapabilities caps);

    AbstractBrowser() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(BrowserType.FIREFOX);
        caps.setPlatform(Platform.WINDOWS);
        caps.setCapability(CapabilityType.TAKES_SCREENSHOT, false);

        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "User-Agent", UserAgents.findARandomUA());
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Connection", "keep-alive");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept-Language", "en-US,en;q=0.5");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Upgrade-Insecure-Requests", "1");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Cache-Control", "max-age=0");

        addSpecificCapabilities(caps);

        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
            new String[] {
                "--web-security=false",
                "--ssl-protocol=any",
                "--ignore-ssl-errors=true",
                "--webdriver-loglevel=NONE"
            }
        );

        PhantomJSDriverService service = new PhantomJSDriverService.Builder()
                .usingPhantomJSExecutable(new File("webdriver/phantomjs"))
                .usingAnyFreePort()
                .build();

        driver = new PhantomJSDriver(service, caps);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    public Pair getHtml(String url) {
        long start = new Date().getTime();
        driver.get(url);

        long end = new Date().getTime();
        log.info("Caller: {}, Time: {}", getName(), (end-start));
        final String result = driver.getPageSource();

        return new Pair(200, result);
    }

    public void quit() {
        if (driver != null) driver.quit();
    }

}