package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

public class BestBuy extends AbstractWebsite {

    public BestBuy(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element availability = doc.selectFirst("p.inactive-product-message");
        return (availability == null);
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("div.sku .product-data-value");
        if (sku != null) {
            return sku.text().trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("div.sku-title h1");
        if (name != null) {
            return name.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("div.priceView-hero-price span[aria-hidden]");

        if (price != null) {
            return new BigDecimal(cleanPrice(price.text().trim()));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "BestBuy";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.fulfillment-fulfillment-summary");

        if (shipment != null) {
            return shipment.text().trim();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        String brand = getName();
        if (brand != null && brand.indexOf("-") > 0) {
            return brand.split("-")[0];
        }

        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("li.bullet"));
    }

}
