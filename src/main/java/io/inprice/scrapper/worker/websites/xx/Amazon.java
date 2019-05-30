package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * asin den buldurma https://www.amazon.co.uk/dp/B00VX62MHO
 */
public class Amazon extends AbstractWebsite {

    public Amazon(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element available = doc.selectFirst("div#availability span");
        return (available != null && available.text().contains("In Stock"));
    }

    @Override
    public String getSku() {
        Element sku = doc.getElementById("ASIN");
        if (sku != null) {
            return sku.val().trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.getElementById("productTitle");
        if (name != null) {
            return name.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        String strPrice = null;

        Element price = doc.getElementById("priceblock_dealprice");
        if (price != null) {
            strPrice = price.text();
        } else {
            price = doc.getElementById("cerberus-data-metrics");
            if (price != null) strPrice = price.attr("data-asin-price");
        }

        if (strPrice == null || strPrice.isEmpty()) {
            price = doc.select(".header-price").first();
            if (price == null) price = doc.select("span.a-color-price").first();
            if (price == null) price = doc.select(".a-size-medium.a-color-price.offer-price.a-text-normal").first();

            if (price != null) {
                strPrice = price.text();
            } else {
                price = doc.select(".price-large").first();
                if (price != null) {
                    String left = cleanPrice(price.text());
                    String right = "00";
                    if (price.nextElementSibling() != null) {
                        right = price.nextElementSibling().text();
                    }
                    strPrice = left + "." + right;
                } else {
                    price = doc.getElementById("priceblock_ourprice");
                    if (price != null) {
                        if (price.text().contains("-")) {
                            String[] priceChunks = price.text().split("-");
                            String first = cleanPrice(priceChunks[0]);
                            String second = cleanPrice(priceChunks[1]);
                            BigDecimal low = new BigDecimal(first);
                            BigDecimal high = new BigDecimal(second);
                            strPrice = high.add(low).divide(BigDecimal.valueOf(2)).toString();
                        } else {
                            strPrice = price.text();
                        }
                    }
                }
            }
        }

        if (strPrice == null || strPrice.isEmpty())
            return BigDecimal.ZERO;
        else
            return new BigDecimal(cleanPrice(strPrice));
    }

    @Override
    public String getSeller() {
        return "Amazon";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.getElementById("price-shipping-message");
        if (shipment == null) shipment = doc.getElementById("ddmDeliveryMessage span");
        if (shipment == null) shipment = doc.select(".shipping3P").first();

        if (shipment != null) {
            return shipment.text().trim();
        }

        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("span.ac-keyword-link a");
        if (brand == null) brand = doc.getElementById("bylineInfo");

        if (brand != null) {
            return brand.text().trim();
        }

        return "Amazon";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("#feature-bullets li:not(.aok-hidden)"));
    }
}
