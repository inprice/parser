package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

public class Walmart extends AbstractWebsite {

    public Walmart(Link link) {
        super(link);
    }

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
        return "NA";
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
        return getValueOnlySpecList(doc.select("div#product-about li"));
    }
}