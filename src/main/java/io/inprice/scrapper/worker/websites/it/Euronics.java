package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Euronics Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Euronics extends AbstractWebsite {

    private Element base;

    public Euronics(Link link) {
        super(link);
    }

    @Override
    protected JSONObject getJsonData() {
        base = doc.getElementsByTag("trackingProduct").first();
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getSku() {
        if (base != null) {
            return base.attr("productId");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (base != null) {
            return base.attr("productName");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (base != null) {
            return new BigDecimal(base.attr("price"));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Euronics";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("span.productDetails__label.productDetails__label--left");
        if (shipment != null) {
            return shipment.text();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        if (base != null) {
            return base.attr("brand");
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("ul.productDetails__specifications li"));
    }
}
