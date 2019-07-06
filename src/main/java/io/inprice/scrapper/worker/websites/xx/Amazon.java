package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Amazon Global
 *
 * Contains standard data. Nothing special, all is extracted by css selectors
 *
 * finding by asin : https://www.amazon.com/dp/B00VX62MHO
 *
 * @author mdpinar
 */
public class Amazon extends AbstractWebsite {

    public Amazon(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element available = doc.getElementById("availability");
        if (available != null) {
            Element span = available.selectFirst("span.a-color-success");
            if (span == null) span = available.selectFirst("span.a-color-price");
            return span != null;
        }

        available = doc.getElementById("ebooksProductTitle");
        if (available == null) available = doc.getElementById("add-to-cart-button");

        return (available != null);
    }

    @Override
    public String getSku() {
        Element sku = doc.getElementById("ASIN");
        if (sku != null) {
            return sku.val();
        }

        sku = doc.selectFirst("input[name='ASIN.0']");
        if (sku != null) {
            return sku.val();
        }

        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element name = doc.getElementById("productTitle");
        if (name == null) name = doc.getElementById("ebooksProductTitle");

        if (name != null) {
            return name.text();
        }

        return Constants.NOT_AVAILABLE;
    }

    /**
     * The reason why this method became so complicated.
     *
     * Amazon has different types of page designs. Price can be found in different locations.
     * Sometimes it can be a range like $100 - $300, or the product is on sale which is required to mark with extra css and tags.
     * So, we need to consider all those possibilities to have the correct price info.
     *
     * @return BigDecimal - the price
     */
    @Override
    public BigDecimal getPrice() {
        String strPrice = null;

        Element price = doc.getElementById("priceblock_dealprice");
        if (price == null) {
            price = doc.getElementById("priceblock_ourprice");
            if (price != null) {
                Element integer = price.selectFirst("span.price-large");
                if (integer != null) {
                    Element decimal = integer.nextElementSibling();
                    if (decimal != null) {
                        strPrice = integer.text().trim() + "." + decimal.text().trim();
                        return new BigDecimal(cleanDigits(strPrice));
                    }
                }
            }
        }

        if (price == null) price = doc.selectFirst("div#buybox span.a-color-price");

        if (price != null) {
            strPrice = price.text();
        } else {
            price = doc.getElementById("cerberus-data-metrics");
            if (price != null) strPrice = price.attr("data-asin-price");
        }

        if (strPrice == null || strPrice.isEmpty()) {
            price = doc.selectFirst(".header-price");
            if (price == null) price = doc.selectFirst("span.a-size-base.a-color-price.a-color-price");
            if (price == null) price = doc.selectFirst(".a-size-medium.a-color-price.offer-price.a-text-normal");

            if (price != null) {
                strPrice = price.text();
            } else {
                price = doc.selectFirst(".price-large");
                if (price != null) {
                    String left = cleanDigits(price.text());
                    String right = "00";
                    if (price.nextElementSibling() != null) {
                        right = price.nextElementSibling().text();
                    }
                    strPrice = left + "." + right;
                } else {
                    //if price is a range like 100 - 300
                    price = doc.getElementById("priceblock_ourprice");
                }
            }
        }

        if (price != null) {
            if (price.text().contains("-")) {
                String[] priceChunks = price.text().split("-");
                String first = cleanDigits(priceChunks[0]);
                String second = cleanDigits(priceChunks[1]);
                BigDecimal low = new BigDecimal(cleanDigits(first));
                BigDecimal high = new BigDecimal(cleanDigits(second));
                strPrice = high.add(low).divide(BigDecimal.valueOf(2)).toString();
            } else {
                strPrice = price.text();
            }
        }

        if (strPrice == null || strPrice.isEmpty())
            return BigDecimal.ZERO;
        else
            return new BigDecimal(cleanDigits(strPrice));
    }

    @Override
    public String getSeller() {
        Element seller = doc.getElementById("sellerProfileTriggerId");
        if (seller == null) seller = doc.selectFirst("span.mbcMerchantName");

        if (seller != null) {
            return seller.text();
        }
        return "Amazon";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.getElementById("price-shipping-message");
        if (shipment != null && shipment.text().trim().length() == 0) shipment = null;

        if (shipment == null) shipment = doc.selectFirst(".shipping3P");
        if (shipment == null) shipment = doc.getElementById("mbc-shipping-free-1");
        if (shipment == null) shipment = doc.getElementById("mbc-shipping-sss-returns-free-1");
        if (shipment == null) shipment = doc.getElementById("mbc-shipping-sss-eligible-1");
        if (shipment == null) shipment = doc.getElementById("ddmDeliveryMessage");
        if (shipment == null) shipment = doc.getElementById("deliverTo");
        if (shipment == null) shipment = doc.getElementById("delivery-message");

        if (shipment != null) {
            return shipment.text();
        }

        shipment = doc.getElementById("buybox-see-all-buying-choices-announce");
        if (shipment != null) {
            return "See all offers";
        }

        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getBrand() {
        Element brand = doc.getElementById("mbc");
        if (brand != null) {
            String brnd = brand.attr("data-brand");
            if (!brnd.trim().isEmpty()) {
                return brnd;
            }
        }

        brand = doc.getElementById("bylineInfo");
        if (brand == null) brand = doc.selectFirst("span.ac-keyword-link a");

        if (brand != null) {
            return brand.text();
        }

        return "Amazon";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = getValueOnlySpecList(doc.select("#feature-bullets li:not(.aok-hidden)"));
        if (specList == null) {
            specList = getValueOnlySpecList(doc.select("div.content ul li"));
        }
        return specList;
    }
}
