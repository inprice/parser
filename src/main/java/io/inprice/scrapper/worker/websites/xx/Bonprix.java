package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Bonprix Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Bonprix extends AbstractWebsite {

    public Bonprix(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element available = doc.selectFirst("meta[property='og:availability']");
        if (available == null) available = doc.selectFirst("meta[content='https://schema.org/InStock']");

        if (available != null) {
            return available.attr("content").toLowerCase().contains("instock");
        }

        available = doc.selectFirst("div.product-availability-box_wrapper div");
        if (available == null) doc.selectFirst("div#product-detail-availibility-container noscript");

        if (available != null) {
            return available.text().contains("erfügbar");
        }

        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("meta[itemprop='sku']");
        if (sku != null) {
            return sku.attr("content");
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1.product-name span[itemprop='name']");
        if (name != null) return name.text();

        name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content");
        }

        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("span.price");
        if (price != null) {
            return new BigDecimal(cleanDigits(price.attr("content")));
        }

        price = doc.selectFirst("span[itemprop='price']");
        if (price != null) {
            return new BigDecimal(cleanDigits(price.text()));
        }

        price = doc.selectFirst("span.clearfix.price");
        if (price == null) price = doc.selectFirst("meta[property='og:price:amount']");

        if (price != null) {
            return new BigDecimal(cleanDigits(price.attr("content")));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Bonprix";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div[data-controller='infoicon'] div.info-text");
        if (shipment != null) {
            return shipment.text().replaceAll(" ada12_info-icon-bigsize-additional-text", "");
        }

        shipment = doc.selectFirst("div.product-availability-box_wrapper div");
        if (shipment != null) {
            return shipment.text();
        }

        shipment = doc.getElementById("aiDelChargeSame");
        if (shipment != null) {
            return shipment.text();
        }

        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("meta[itemprop='brand']");
        if (brand == null) brand = doc.selectFirst("meta[property='og:brand']");

        if (brand != null) {
            return brand.attr("content");
        }

        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        Elements specKeys = doc.select("div.product-attributes strong");
        if (specKeys != null && specKeys.size() > 0) {
            specList = new ArrayList<>();
            Elements specValues = doc.select("div.product-attributes span");
            for (int i = 0; i < specKeys.size(); i++) {
                Element key = specKeys.get(i);
                Element val = null;
                if (i < specValues.size() - 1) {
                    val = specValues.get(i);
                }
                specList.add(new LinkSpec(key.text().replaceAll(":", ""), (val != null ? val.text() : "")));
            }
            return specList;
        }

        specKeys = doc.select("div.productFeaturesContainer span.productFeatureName");
        if (specKeys == null) specKeys = doc.select("div.product-attributes strong");

        if (specKeys != null && specKeys.size() > 0) {
            specList = new ArrayList<>();
            for (Element key : specKeys) {
                specList.add(new LinkSpec(key.text().replaceAll(":", ""), ""));
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
                    specList.add(new LinkSpec("", value.text()));
                } else {
                    specList.get(i).setValue(value.text());
                }
            }

            return specList;
        }

        specValues = doc.select("div.productDescription");

        if (specValues != null) {
            specList = new ArrayList<>();
            String desc = specValues.text();
            String[] descChunks = desc.split("\\.");
            if (descChunks.length > 0) {
                for (String dsc: descChunks) {
                    specList.add(new LinkSpec("", dsc));
                }
            }
        }

        return specList;
    }
}
