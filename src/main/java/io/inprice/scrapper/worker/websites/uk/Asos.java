package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Asos extends AbstractWebsite {

    @Override
    public boolean isAvailable() {
        Element inStock = doc.selectFirst("link[itemprop='availability']");
        if (inStock != null) {
            return inStock.attr("href").contains("InStock");
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("span[itemprop='sku']");
        if (sku != null) {
            return sku.text().trim();
        }
        return null;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("span[itemprop='brand'] span[itemprop='name']");
        if (name != null) {
            return name.text().trim();
        }
        return null;
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
        return null;
    }

    @Override
    public String getShipment() {
        String val = null;

        Element shipment = doc.selectFirst("#shipping-restrictions .shipping-restrictions");
        if (shipment != null) {
            val = shipment.attr("style");
            if (val != null) val = "Please refer to Delivery and returns info section";
        }

        shipment = doc.getElementById("shippingRestrictionsLink");
        if (shipment != null) val = shipment.text().trim();

        return val;
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("span span[itemprop='name']");
        if (brand != null) {
            return brand.text().trim();
        }
        return null;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div.product-description li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                specList.add(new LinkSpec("", spec.text().trim()));
            }
        }
        return specList;
    }
}
