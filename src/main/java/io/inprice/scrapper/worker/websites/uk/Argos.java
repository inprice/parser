package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Argos UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Argos extends AbstractWebsite {

    public Argos(Link link) {
        super(link);
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("[itemProp='sku']");
        if (sku != null) {
            return sku.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("span.product-title");
        if (name != null) {
            return name.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst(".product-price-primary");
        if (price != null) {
            return new BigDecimal(price.attr("content").trim());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Argos";
    }

    @Override
    public String getShipment() {
        return "In-store pickup";
    }

    @Override
    public String getBrand() {
        Element seller = doc.selectFirst(".product-brand a");
        if (seller != null) {
            return seller.text().trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select(".product-description-content-text li"));
    }
}
