package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for CoolBlue the Netherlands
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class CoolBlue extends AbstractWebsite {

    /*
     * holds price info set in getJsonData()
     */
    private JSONObject offers;

    public CoolBlue(Link link) {
        super(link);
    }

    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("script[type='application/ld+json']");
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().replace("\r\n"," "));
            if (data.has("offers")) {
                offers = data.getJSONObject("offers");
            }
            return data;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (offers != null && offers.has("availability")) {
            String availability = offers.getString("availability");
            return availability.contains("InStock") || availability.contains("PreOrder");
        }
        return false;
    }

    @Override
    public String getSku() {
        if (json != null && json.has("sku")) {
            return json.getString("sku");
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        if (json != null && json.has("name")) {
            return json.getString("name");
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        if (offers != null && offers.has("price")) {
            return offers.getBigDecimal("price");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "CoolBlue";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brand")) {
            return json.getJSONObject("brand").getString("name");
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("span.js-delivery-information-usp");
        if (shipment != null) {
            return shipment.text();
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getKeyValueSpecList(
                doc.select("div.product-specs__list-item.js-product-specs--list-item"),
                ".product-specs__item-title", ".product-specs__item-spec");
    }

}
