package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Rakuten Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Rakuten extends AbstractWebsite {

    public Rakuten(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element available = doc.selectFirst("meta[property='product:availability']");
        if (available != null) {
            return available.attr("content").trim().contains("in stock");
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("meta[property='product:retailer_item_id']");
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
        Element price = doc.selectFirst("meta[property='product:price:amount']");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content")));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element brand = doc.selectFirst("meta[property='og:description']");
        if (brand != null) {
            return brand.attr("content").trim();
        }
        return "Rakuten";
    }

    @Override
    public String getShipment() {
        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("meta[property='product:brand']");
        if (brand != null) {
            return brand.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return null;
    }
}
