package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Debenhams extends AbstractWebsite {

    @Override
    public JSONObject getJsonData() {
        Element data = doc.selectFirst("script[type='application/ld+json']");
        if (data != null) {
            return new JSONObject(data.dataNodes().get(0).getWholeData().trim());
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("offers")) {
            JSONObject offers = json.getJSONObject("offers");
            if (! offers.isEmpty() && offers.has("availability")) {
                String status = offers.getString("availability");
                return ("http://schema.org/InStock".equals(status));
            }
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        if (json != null && json.has("@id")) {
            String id = json.getString("@id");
            if (id != null) return cleanPrice(id);
        }

        Element sku = doc.selectFirst("span.item-number-value");
        if (sku != null) {
            return sku.text().trim();
        }

        return null;
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

        return null;
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && json.has("offers")) {
            JSONObject offers = json.getJSONObject("offers");
            if (! offers.isEmpty() && offers.has("price")) {
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
        return "Debenhams";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.pw-dangerous-html.dbh-content");
        if (shipment == null) shipment = doc.getElementById("hd3");

        if (shipment != null) {
            return shipment.text().trim();
        }
        return null;
    }

    @Override
    public String getBrand() {
        if (json != null) {
            if (json.has("schema:brand")) {
                return json.getString("schema:brand");
            }
            if (json.has("brand")) {
                return json.getJSONObject("brand").getString("name");
            }
        }
        return null;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div.pw-dangerous-html li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                specList.add(new LinkSpec("", spec.text().trim()));
            }
        }
        return specList;
    }
}
