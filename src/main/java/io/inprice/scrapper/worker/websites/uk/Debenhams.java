package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Debenhams UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Debenhams extends AbstractWebsite {

    /*
     * holds price info set in getJsonData()
     */
    private JSONObject offers;

    public Debenhams(Link link) {
        super(link);
    }

    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("script[type='application/ld+json']");
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
            if (data.has("offers")) {
                offers = data.getJSONObject("offers");
            }
            return data;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        Element availability = doc.selectFirst("meta[name='twitter:data2']");
        if (availability != null) {
            return "In Stock".equals(availability.attr("content"));
        }
        return false;
    }

    @Override
    public String getSku() {
        if (json != null) {
            if (json.has("sku")) {
                return json.getString("sku");
            }
            if (json.has("@id")) {
                String id = json.getString("@id");
                if (id != null) return cleanDigits(id);
            }
        }

        Element sku = doc.selectFirst("span.item-number-value");
        if (sku != null) {
            return sku.text();
        }

        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        if (json != null && json.has("name")) {
            return json.getString("name");
        }

        Element name = doc.selectFirst("div#ProductTitle span.title");
        if (name != null) {
            return name.text();
        }

        return Constants.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        if (offers != null) {
            if (! offers.isEmpty() && offers.has("lowPrice")) {
                return offers.getBigDecimal("lowPrice");
            } else if (! offers.isEmpty() && offers.has("price")) {
                return offers.getBigDecimal("price");
            }
        }

        Element price = doc.selectFirst("span.VersionOfferPrice img");
        if (price != null) {
            return new BigDecimal(cleanDigits(price.attr("alt")));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Debenhams";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.pw-dangerous-html.dbh-content");
        if (shipment == null) shipment = doc.getElementById("hd3");

        if (shipment != null) {
            return shipment.text();
        }
        return "In-store pickup";
    }

    @Override
    public String getBrand() {
        if (json != null) {
            if (json.has("brand")) {
                return json.getJSONObject("brand").getString("name");
            }
            if (json.has("schema:brand")) {
                return json.getString("schema:brand");
            }
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("div.pw-dangerous-html li"));
    }

}
