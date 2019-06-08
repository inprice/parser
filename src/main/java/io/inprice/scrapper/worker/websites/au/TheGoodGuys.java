package io.inprice.scrapper.worker.websites.au;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for The Good Guys Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class TheGoodGuys extends AbstractWebsite {

    public TheGoodGuys(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element available = doc.getElementById("add2CartBtn");
        return  (available != null);
    }

    @Override
    public String getSku() {
        Element sku = doc.getElementById("mainProductId");
        if (sku != null) {
            return sku.val();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1.titleItems_head");
        if (name != null) {
            return name.text();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("meta[property='og:price:amount']");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content").trim()));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "The Good Guys";
    }

    @Override
    public String getShipment() {
        return "Check delivery cost";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("img.brand_logo_keyftrs");
        if (brand != null) {
            return brand.attr("alt").trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getKeyValueSpecList(doc.select("section#keyftr li"), "small", "h2");
    }
}