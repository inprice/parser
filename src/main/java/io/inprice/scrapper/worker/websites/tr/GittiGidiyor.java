package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GittiGidiyor extends AbstractWebsite {

    @Override
    public boolean isAvailable() {
        Element amount = doc.getElementById("VariantProductRemaingCount");
        if (amount != null) {
            try {
                int realAmount = new Integer(amount.text().trim());
                return (realAmount > 0);
            } catch (Exception e) {}
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.getElementById("productId");
        if (sku != null) {
            return sku.val().trim();
        }
        return null;
    }

    @Override
    public String getName() {
        Element name = doc.getElementById("productTitle");
        if (name == null) name = doc.selectFirst("span.title");

        if (name != null) {
            return name.text().trim();
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        String strPrice = null;

        Element price = doc.selectFirst("[data-price]");
        if (price != null) {
            strPrice = price.attr("data-price").trim();
        }

        if (strPrice == null)
            return BigDecimal.ZERO;
        else
            return new BigDecimal(cleanPrice(strPrice));
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst(".member-name a strong");
        if (seller != null) {
            return seller.text().trim();
        }
        return null;
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst(".CargoInfos .color-black");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return null;
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst(".mr10.gt-product-brand-0 a");
        if (brand != null) {
            return brand.text().trim();
        }
        return null;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("#specs-container ul li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String key = spec.select("span").text();
                String value = spec.select("strong").text().replaceAll(":", "").trim();
                specList.add(new LinkSpec(key, value));
            }
        }
        return specList;
    }

}
