package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Zalando Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Zalando extends AbstractWebsite {

    public Zalando(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element addToCartButton = doc.getElementById("z-pdp-topSection-addToCartButton");
        return (addToCartButton != null);
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("meta[property='og:url']");
        if (sku != null) {
            String url = sku.attr("content").trim();
            if (! url.isEmpty()) {
                String[] urlChunks = url.split("-");
                if (urlChunks.length > 1) {
                    String pure = urlChunks[urlChunks.length-2] + "-" + urlChunks[urlChunks.length-1].replaceAll(".html", "");
                    return pure.toUpperCase();
                }
            }
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1[title]");
        if (name != null) {
            return name.attr("title").trim();
        }

        name = doc.selectFirst("meta[name='twitter:title']");
        if (name != null) {
            return name.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("meta[name='twitter:data1']");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content").trim()));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Zalando";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.getElementById("z-pdp-topDeliveryInfo--standard");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return "Standard shipment";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("a[title] h2");
        if (brand != null) {
            return brand.text();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div#z-pdp-detailsSection p.h-text.h-color-black.body.h-m-bottom-xs span");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            int i = 0;
            while (i < specs.size()) {
                String key = specs.get(i++).text().replaceAll(":", "").trim();
                String value = "";
                if (i < specs.size())  value = specs.get(i++).text().trim();
                if (! key.isEmpty() && ! value.isEmpty()) specList.add(new LinkSpec(key, value));
            }
        }
        return specList;
    }

}
