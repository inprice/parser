package io.inprice.scrapper.worker.websites.generic;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GenericWebsiteT1 extends AbstractWebsite {

    private final String websiteName;
    private JSONObject offers;

    protected GenericWebsiteT1(String websiteName) {
        this.websiteName = websiteName;
    }

    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("script[type='application/ld+json']");
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().trim());
            if (data.has("offers")) {
                offers = json.getJSONObject("offers");
            }
            return data;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (offers != null && offers.has("availability")) {
            String status = offers.getString("availability");
            return status.endsWith("InStock");
        }
        return super.isAvailable();
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

        return null;
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
        return "Delivery in store";
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
        return getSpecList("div.pw-dangerous-html li");
    }

    protected List<LinkSpec> getSpecList(String selector) {
        List<LinkSpec> specList = null;
        Elements specs = doc.select(selector);
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                specList.add(new LinkSpec("", spec.text().trim()));
            }
        }
        return specList;
    }

}
