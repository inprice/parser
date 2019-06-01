package io.inprice.scrapper.worker.websites.ca;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.AbstractSpasite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.*;

/**
 * Parser for CanadianTire Canada
 *
 * Please note that: link's url (aka main url) is never used for data pulling
 *
 * This is a very special case. CanadianTire provides too limited data in html form.
 * So, we need to make a json request to collect all the data we need.
 *
 * in getJsonData(), the data is pulled with the payload whose steps are explained above
 *
 * @author mdpinar
 */
public class CanadianTire extends AbstractSpasite {

    public CanadianTire(Link link) {
        super(link);
    }

    /**
     * Returns payload as key value maps
     *
     * @return Map - payload required for having the data
     */
    private Map<String, String> getPayload() {
        long now = new Date().getTime();
        Map<String, String> payload = new HashMap<>();
        payload.put("SKU", getSku());
        payload.put("Store", "0144");
        payload.put("Banner", "CTR");
        payload.put("isKiosk", "FALSE");
        payload.put("Language", "E");
        payload.put("_", "" + now);
        return payload;
    }

    /**
     * Request the data with a constant url with product-id and payload.
     * Besides, data handles best-offer which holds some important data
     *
     * @return JSONObject - json
     */
    @Override
    public JSONObject getJsonData() {
        final Map<String, String> payload = getPayload();
        if (payload != null) {
            String body = HttpClient.get("https://www.canadiantire.ca/ESB/PriceAvailability", payload, true);
            if (body != null && ! body.trim().isEmpty()) {
                JSONArray data = new JSONArray(body.trim());
                if (! data.isEmpty()) {
                    return data.getJSONObject(0);
                }
            }
        }
        return null;
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("IsOnline")) {
            JSONObject isOnline = json.getJSONObject("IsOnline");
            return "Y".equalsIgnoreCase(isOnline.getString("Sellable"));
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("div[data-sku]");
        if (sku != null) {
            return sku.attr("data-sku").split(":")[0];
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content").replaceAll(" \\| Canadian Tire", "");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null) {
            if (json.has("promo")) {
                return json.getJSONObject("promo").getBigDecimal("Price");
            }
            if (json.has("Price")) {
                return json.getBigDecimal("Price");
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "CanadianTire";
    }

    @Override
    public String getShipment() {
        return "In-store pickup";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("img.brand-logo-link__img");
        if (brand == null) brand = doc.selectFirst("img.brand-footer__logo");

        if (brand != null) {
            return brand.attr("alt");
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("div.pdp-details-features__items li"));
    }

}
