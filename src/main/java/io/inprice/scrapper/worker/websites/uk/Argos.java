package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
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
        final String availability = findAPart(doc.html(), "\"globallyOutOfStock\":", ",");
        return "false".equalsIgnoreCase(availability);
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("[itemProp='sku']");
        if (sku != null) {
            return sku.attr("content");
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("span.product-title");
        if (name != null) {
            return name.text();
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst(".product-price-primary");
        if (price != null) {
            return new BigDecimal(cleanDigits(price.attr("content")));
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
            return brand.text();
        }

        final String brandName = findAPart(doc.html(), "\"brand\":\"", "\",");
        if (brandName != null) {
            return brandName;
        }

        return Constants.NOT_AVAILABLE;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select(".product-description-content-text li"));
    }
}
