package io.inprice.scrapper.worker.helpers;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.worker.info.HtmlResponse;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class Browser {

    public static final String EMPTY_RESPONSE = "<html><head></head><body></body></html>";

    private static final Logger log = new Logger(Browser.class);

    private static ChromeDriver driver;

    static {
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "./webdriver/chromedriver");
        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "./webdriver/chromedriver.log");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings.javascript", 2);
        prefs.put("profile.default_content_settings.images", 2);
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.managed_default_content_settings.stylesheets", 2);
        prefs.put("profile.managed_default_content_settings.cookies", 2);
        prefs.put("profile.managed_default_content_settings.plugins", 2);
        prefs.put("profile.managed_default_content_settings.popups", 2);
        prefs.put("profile.managed_default_content_settings.geolocation", 2);
        prefs.put("profile.managed_default_content_settings.media_stream", 2);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("useAutomationExtension", false);

        options.addArguments("--no-sandbox");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--no-first-run");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-component-update");
        options.addArguments("--disable-translate");
        options.addArguments("--disable-hang-monitor");
        options.addArguments("--disable-touch-events");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-prompt-on-repost");
        options.addArguments("--disable-default-apps");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--headless");
        options.addArguments("--test-type");
        options.addArguments("--noerrdialogs");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--dom-automation");
        options.addArguments("--homepage=about:blank");
        options.addArguments("--enable-experimental-extension-apis");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
    }

    public static synchronized HtmlResponse getHtmlWithJS(String caller, String url) {
        int httpStatus = checkFirst(url);
        if (httpStatus != 200) return new HtmlResponse(httpStatus);

        long start = new Date().getTime();
        driver.get(url);

        try {
            String html = driver.findElementByCssSelector(".pix-price").getAttribute("innerHTML");
            System.out.println("-----------------------------------------------------------------" + html);
        } catch (Exception e) {
        }

        long end = new Date().getTime();
        log.info("Caller: %s, Time: %d", caller, (end-start));
        final String result = driver.getPageSource();
        driver.close();

        return new HtmlResponse(200, result);
    }

    private static int checkFirst(String path) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            URL url = new URL(path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            return http.getResponseCode();
        } catch (UnknownHostException e1) {
            return 404;
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    public static void close() {
        if (driver != null) driver.quit();
    }

}
