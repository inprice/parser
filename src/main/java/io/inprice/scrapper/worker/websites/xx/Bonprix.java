package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bonprix extends AbstractWebsite {

    @Override
    public boolean isAvailable() {
        Element available = doc.selectFirst("meta[property='og:availability']");
        if (available != null) {
            return available.attr("content").trim().equals("instock");
        }

        available = doc.selectFirst("div.product-availability-box_wrapper div");
        if (available != null) {
            return available.text().contains("verfügbar");
        }

        return super.isAvailable();
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
        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("span.clearfix.price");
        if (price == null) price = doc.selectFirst("meta[property='og:price:amount']");

        if (price != null) {
            return new BigDecimal(price.attr("content").trim());
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Bonprix";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.product-availability-box_wrapper div");
        if (shipment != null) {
            return shipment.text();
        }

        shipment = doc.getElementById("aiDelChargeSame");
        if (shipment != null) {
            return shipment.text();
        }

        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("meta[itemprop='brand']");
        if (brand == null) brand = doc.selectFirst("meta[property='og:brand']");

        if (brand != null) {
            return brand.attr("content").trim();
        }

        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        Elements specKeys = doc.select("div.productFeaturesContainer span.productFeatureName");
        if (specKeys != null && specKeys.size() > 0) {
            specList = new ArrayList<>();
            for (Element key : specKeys) {
                specList.add(new LinkSpec(key.text(), ""));
            }
        }

        Elements specValues = doc.select("div.productFeaturesContainer span.productFeatureValue");
        if (specValues != null && specValues.size() > 0) {
            boolean isEmpty = false;
            if (specList == null) {
                isEmpty = true;
                specList = new ArrayList<>();
            }
            for (int i = 0; i < specValues.size(); i++) {
                Element value = specValues.get(i);
                if (isEmpty) {
                    specList.add(new LinkSpec("", value.text().trim()));
                } else {
                    specList.get(i).setValue(value.text().trim());
                }
            }
        }

        if (specList == null) {
            specKeys = doc.select("div.product-attributes strong");
            if (specKeys != null && specKeys.size() > 0) {
                specList = new ArrayList<>();
                for (Element key : specKeys) {
                    specList.add(new LinkSpec(key.text().replaceAll(":", "").trim(), ""));
                }
            }

            specValues = doc.select("div.product-attributes span");
            if (specValues != null && specValues.size() > 0) {
                boolean isEmpty = false;
                if (specList == null) {
                    isEmpty = true;
                    specList = new ArrayList<>();
                }
                for (int i = 0; i < specValues.size(); i++) {
                    Element value = specValues.get(i);
                    if (isEmpty) {
                        specList.add(new LinkSpec("", value.text().trim()));
                    } else {
                        specList.get(i).setValue(value.text().trim());
                    }
                }
            }
        }

        return specList;
    }
}
