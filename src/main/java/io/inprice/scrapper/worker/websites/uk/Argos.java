package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Argos UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Argos extends AbstractWebsite {

    public Argos(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        final String html = doc.html();
        final String indicator = "\"globallyOutOfStock\":";

        int start = html.indexOf(indicator) + indicator.length();
        int end = html.indexOf(",", start);

        final String result = html.substring(start, end);
        return "false".equalsIgnoreCase(result);
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("[itemProp='sku']");
        if (sku != null) {
            return sku.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("span.product-title");
        if (name != null) {
            return name.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst(".product-price-primary");
        if (price != null) {
            return new BigDecimal(price.attr("content").trim());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Argos";
    }

    @Override
    public String getShipment() {
        final String staticPart = "In-store pickup";

        Element shippingFee = doc.selectFirst("a.ac-propbar__slot > span.sr-only");
        if (shippingFee != null) {
            return staticPart + " OR " + shippingFee.text();
        }

        return staticPart;
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("[itemprop='brand']");
        if (brand != null) {
            return brand.text().trim();
        }

        final String html = doc.html();
        final String indicator = "\"brand\":\"";

        int start = html.indexOf(indicator) + indicator.length();
        int end = html.indexOf("\",", start);

        if (end > start) {
            return html.substring(start, end);
        }

        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select(".product-description-content-text li"));
    }
}
