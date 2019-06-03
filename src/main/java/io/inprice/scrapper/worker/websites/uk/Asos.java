package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Asos UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Asos extends AbstractWebsite {

    public Asos(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        final String html = doc.html();
        final String indicator = "\"isInStock\":";

        int start = html.indexOf(indicator) + indicator.length();
        int end = html.indexOf(",", start);

        final String result = html.substring(start, end);
        return "true".equalsIgnoreCase(result);
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("span[itemprop='sku']");
        if (sku != null) {
            return sku.text().trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("span[itemprop='brand'] span[itemprop='name']");
        if (name != null) {
            return name.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        BigDecimal val = null;

        Element price = doc.selectFirst("span[itemprop='price']");
        if (price != null) {
            val = new BigDecimal(price.text().trim());
            val = val.multiply(new BigDecimal(1.3825)); //asos' multiplier
        }
        return val;
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst("span[itemprop='seller'] span[itemprop='name']");
        if (seller != null) {
            return seller.text().trim();
        }
        return "NA";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("#shipping-restrictions .shipping-restrictions");
        if (shipment != null) {
            String val = shipment.attr("style");
            if (val != null) return "Please refer to Delivery and returns info section";
        }

        shipment = doc.getElementById("shippingRestrictionsLink");
        if (shipment != null) {
            return shipment.text().trim();
        } else {
            return "See delivery and returns info";
        }
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("span span[itemprop='name']");
        if (brand != null) {
            return brand.text().trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("div.product-description li"));
    }
}
