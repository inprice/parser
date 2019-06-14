package io.inprice.scrapper.worker.helpers;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.worker.info.Pair;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Browser {

    private static final Logger log = new Logger(Browser.class);

    private static WebDriver driver;

    static {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(BrowserType.FIREFOX);
        caps.setPlatform(Platform.WINDOWS);
        caps.setCapability(CapabilityType.TAKES_SCREENSHOT, false);

        /*
         * Please be careful!!!
         * Some sites like HarveyNorman are guarded by a security software like incapsulata and it can sense us if the parameter below is set
         *
         * caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", false);
         */

        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "User-Agent", UserAgents.findARandomUA());
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Host", "www.harveynorman.com.au");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Connection", "keep-alive");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept-Language", "en-US,en;q=0.5");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Upgrade-Insecure-Requests", "1");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Cache-Control", "max-age=0");

        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                new String[] {
                        "--web-security=false",
                        "--ssl-protocol=any",
                        "--ignore-ssl-errors=true",
                        "--webdriver-loglevel=ERROR"
                }
        );

        PhantomJSDriverService service = new PhantomJSDriverService.Builder()
                .usingPhantomJSExecutable(new File("webdriver/phantomjs"))
                .usingAnyFreePort()
                .build();

        driver = new PhantomJSDriver(service, caps);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    public static Pair getHtmlWithJS(String caller, String url) {
        long start = new Date().getTime();
        driver.get(url);

        long end = new Date().getTime();
        log.info("Caller: %s, Time: %d", caller, (end-start));
        final String result = driver.getPageSource();

        return new Pair(200, result);
    }

    public static void close() {
        if (driver != null) driver.quit();
    }

}