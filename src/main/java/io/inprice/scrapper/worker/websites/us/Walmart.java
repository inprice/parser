package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Walmart extends AbstractWebsite {

    @Override
    public boolean isAvailable() {
        return doc.html().contains("\"availabilityStatus\":\"IN_STOCK\"");
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("meta[itemprop='sku']");
        if (sku != null) {
            return sku.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1[itemprop='name']");
        if (name != null) {
            return name.attr("content").trim();
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        Element name = doc.selectFirst("span[itemprop='price']");
        if (name != null) {
            return new BigDecimal(name.attr("content").trim());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst("a[data-tl-id='ProductSellerInfo-SellerName']");
        if (seller != null) {
            return seller.text().trim();
        }
        return "Walmart";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.prod-pickupMessageAccess span");
        if (shipment == null) shipment = doc.selectFirst("span.copy-small.font-bold");

        if (shipment != null) {
            return shipment.text();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("span[itemprop='brand']");
        if (brand != null) {
            return brand.text().trim();
        }
        return null;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div#product-about li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                specList.add(new LinkSpec("", spec.text().trim()));
            }
        }
        return specList;
    }
}
