package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
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

    private JSONObject offer;

    public Rakuten(Link link) {
        super(link);
    }

    @Override
    protected JSONObject getJsonData() {
        final String indicator = "var localDatalayerPersonali=";

        int start = doc.html().indexOf(indicator) + indicator.length();
        int end   = doc.html().indexOf(";", start);

        if (start > indicator.length() && end > start) {
            JSONObject data = new JSONObject(doc.html().substring(start, end));
            if (data.has("mainOffer")) {
                offer = data.getJSONObject("mainOffer");
            }
            return data;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getSku() {
        if (json != null && json.has("productId")) {
            return json.getString("productId");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (json != null && json.has("productTitle")) {
            return json.getString("productTitle");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (offer != null && offer.has("productPrice")) {
            return offer.getBigDecimal("productPrice");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        if (offer != null && offer.has("sellerName")) {
            return offer.getString("sellerName");
        }
        return "NA";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("p.freeShipping");
        if (shipment == null) shipment = doc.selectFirst("div.shipping_country");

        if (shipment != null) {
            return shipment.text();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("productBrand")) {
            return json.getString("productBrand");
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getKeyValueSpecList(doc.select("table.spec_table_ctn tr"), "th", "td");
    }
}
