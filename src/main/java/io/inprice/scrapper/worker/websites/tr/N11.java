package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class N11 extends AbstractWebsite {

    @Override
    public boolean isAvailable() {
        Element amount = doc.selectFirst("input[class='stockCount']");
        if (amount != null) {
            try {
                int realAmount = new Integer(amount.val().trim());
                return (realAmount > 0);
            } catch (Exception e) {}
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("input[class='productId']");
        if (sku != null) {
            return sku.val().trim();
        }
        return null;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1.proName");
        if (name == null) name = doc.selectFirst("h1.pro-title_main");
        if (name != null) {
            return name.text().trim();
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        String strPrice = null;

        Element price = doc.selectFirst(".newPrice ins");
        if (price == null) price = doc.selectFirst("ins.price-now");
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
        String val = null;
        Element seller = doc.selectFirst("div.sallerTop h3 a");
        if (seller != null) {
            val = seller.attr("title").trim();
        } else {
            seller = doc.selectFirst(".shop-name");
            if (seller != null) {
                val = seller.text().trim();
            }
        }
        return val;
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst(".shipment-detail-container .cargoType");
        if (shipment == null) shipment = doc.selectFirst(".delivery-info_shipment span");

        if (shipment != null) {
            return shipment.text().replaceAll(":", "").trim();
        }
        return null;
    }

    @Override
    public String getBrand() {
        String[] titleChunks = getName().split("\\s");
        if (titleChunks.length > 1) return titleChunks[0].trim();
        return null;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div.feaItem");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String key = spec.select(".label").text();
                String value = spec.select(".data").text();
                specList.add(new LinkSpec(key, value));
            }
        }
        return specList;
    }
}
