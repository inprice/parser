package io.inprice.scrapper.worker.websites.generic;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * This is a generic website type 1 class.
 *
 * Used by websites containing standard data, almost all data can be extracted by css selectors
 *
 * @author mdpinar
 */
public class GenericWebsiteT1 extends AbstractWebsite {

    /*
     * holds price info set in getJsonData()
     */
    protected JSONObject offers;

    /*
     * used to distinguish who is the seller
     */
    private final String websiteName;

    protected GenericWebsiteT1(Link link, String websiteName) {
        super(link);
        this.websiteName = websiteName;
    }

    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.getElementsByTag("title").next("script[type='application/ld+json']").first();
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().trim());
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
                if (id != null) return cleanPrice(id);
            }
        }

        Element sku = doc.selectFirst("span.item-number-value");
        if (sku != null) {
            return sku.text().trim();
        }

        return "NA";
    }

    @Override
    public String getName() {
        if (json != null && json.has("name")) {
            return json.getString("name");
        }

        Element name = doc.selectFirst("div#ProductTitle span.title");
        if (name != null) {
            return name.text().trim();
        }

        return "NA";
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
            return new BigDecimal(cleanPrice(price.attr("alt").trim()));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return this.websiteName;
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.pw-dangerous-html.dbh-content");
        if (shipment == null) shipment = doc.getElementById("hd3");

        if (shipment != null) {
            return shipment.text().trim();
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
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getSpecList("div.pw-dangerous-html li");
    }

    protected List<LinkSpec> getSpecList(String selector) {
        return getValueOnlySpecList(doc.select(selector));
    }

}
