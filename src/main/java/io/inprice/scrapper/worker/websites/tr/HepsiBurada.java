package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HepsiBurada extends AbstractWebsite {

    @Override
    public boolean isAvailable() {
        Element available = doc.selectFirst("div.product-detail-box[style*='display: none']");
        return (available != null);
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("#addToCartForm input[name='sku']");
        if (sku != null) {
            return sku.val().trim();
        }
        return null;
    }

    @Override
    public String getName() {
        Element name = doc.getElementById("product-name");
        if (name != null) {
            return name.text().trim();
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        String strPrice = null;

        Element price = doc.getElementById("offering-price");
        if (price != null) {
            strPrice = price.attr("content").trim();
        }

        if (strPrice == null)
            return BigDecimal.ZERO;
        else
            return new BigDecimal(cleanPrice(strPrice));
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst("input[name='merchantId']");
        if (seller != null) {
            return seller.val().trim();
        }
        return null;
    }

    @Override
    public String getShipment() {
        return "50 TL ve üzeri Kargo Bedava";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst(".brand-name a");
        if (brand != null) {
            return brand.text().trim();
        }
        return null;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select(".data-list.tech-spec tr");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String key = spec.select("th").text();
                String value = spec.select("td").text();
                specList.add(new LinkSpec(key, value));
            }
        }
        return specList;
    }
}
