package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MediaMarkt extends AbstractWebsite {

    @Override
    public boolean isAvailable() {
        Element inStock = doc.selectFirst(".online-nostock");
        if (inStock != null) return false;

        inStock = doc.selectFirst("meta[property='og:availability']");
        if (inStock != null) {
            return ! inStock.attr("content").trim().equals("out of stock");
        }

        return super.isAvailable();
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("dd span[itemprop='sku']");
        if (sku != null) {
            return sku.text().trim();
        }
        return null;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content").trim();
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        Element name = doc.selectFirst("meta[property='product:price:amount']");
        if (name != null) {
            return new BigDecimal(name.attr("content").trim());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element brand = doc.selectFirst("meta[property='og:site_name']");
        if (brand != null) {
            return brand.attr("content").trim();
        }
        return "Media Markt";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.price.big");
        if (shipment != null) {
            Element desc = shipment.nextElementSibling().selectFirst("small");
            if (desc != null) return desc.text();
        }

        shipment = doc.selectFirst("div.old-price-block");
        if (shipment != null) {
            Element desc = shipment.nextElementSibling().selectFirst("small");
            if (desc != null) return desc.text();
        }

        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("meta[property='product:brand']");
        if (brand != null) {
            return brand.attr("content").trim();
        }
        return null;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        String parentClass = "specification";
        Elements isParentExist = doc.select("dl." + parentClass);
        if (isParentExist == null) parentClass = "product-details";

        Elements specKeys = doc.select(String.format("dl.%s dt", parentClass));
        if (specKeys != null && specKeys.size() > 0) {
            specList = new ArrayList<>();
            for (Element key : specKeys) {
                specList.add(new LinkSpec(key.text().replaceAll(":","").trim(), ""));
            }
        }

        Elements specValues = doc.select(String.format("dl.%s dd", parentClass));
        if (specValues != null && specValues.size() > 0) {
            boolean isEmpty = false;
            if (specList == null) {
                isEmpty = true;
                specList = new ArrayList<>();
            }
            for (int i = 0; i < specList.size(); i++) {
                Element value = specValues.get(i);
                if (isEmpty) {
                    specList.add(new LinkSpec("", value.text().trim()));
                } else {
                    specList.get(i).setValue(value.text().trim());
                }
            }
        }

        if (specList == null) {
            specValues = doc.select("p.autoWrapParagraph p");
            if (specValues != null && specValues.size() > 0) {
                specList = new ArrayList<>();
                for (Element spec: specValues) {
                    specList.add(new LinkSpec("", spec.text().trim()));
                }
            }
        }

        return specList;
    }
}
