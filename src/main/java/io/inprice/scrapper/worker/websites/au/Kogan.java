package io.inprice.scrapper.worker.websites.au;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Kogan Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Kogan extends AbstractWebsite {

    public Kogan(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element available = doc.selectFirst("link[itemProp='availability']");
        if (available != null) {
            return available.attr("href").contains("InStock");
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("p[itemProp='model']");
        if (sku != null) {
            return sku.text();
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1[itemprop='name']");
        if (name != null) {
            return name.text();
        }

        name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content");
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("meta[property='product:price:amount']");
        if (price != null) {
            return new BigDecimal(cleanDigits(price.attr("content")));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Kogan";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div[itemprop='offers'] span[role='tooltip']");
        if (shipment != null) {
            return shipment.text();
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("meta[itemProp='name']");
        if (brand != null) {
            return brand.attr("content");
        }
        return "Kogan";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("section[itemprop='description'] li"));
    }
}
