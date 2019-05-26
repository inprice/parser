package io.inprice.scrapper.worker.websites.ca;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Walmart extends AbstractWebsite {

    private JSONObject offers;

    @Override
    public String getJSBasedPageCaller() {
        return "Walmart";
    }

    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("div.js-content script[type='application/ld+json']");
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
        final String body = doc.body().html();
        final String indicator = "maxOrderQuantity";

        int start = body.indexOf(indicator) + indicator.length() + 2;
        int end = body.indexOf(",", start);

        if (start > indicator.length() && end > start) {
            String maxOrder = body.substring(start, end);
            if (! maxOrder.isEmpty()) {
                int stockAmount = 0;
                try {
                    stockAmount = new Integer(maxOrder);
                } catch (Exception e) {
                    ;
                }
                return stockAmount > 0;
            }
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        if (json != null && json.has("sku")) {
            return json.getString("sku");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (json !=  null && json.has("name")) {
            return json.getString("name").trim();
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        if (offers !=  null && offers.has("price")) {
            return offers.getBigDecimal("price");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        if (offers != null && offers.has("seller")) {
            return offers.getJSONObject("seller").getString("name");
        }
        return "NA";
    }

    @Override
    public String getShipment() {
        if (getSeller() != null) {
            return "Sold & shipped by " + getSeller();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brand")) {
            JSONObject brand = json.getJSONObject("brand");
            if (brand.has("name")) {
                return brand.getString("name");
            }
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        final String body = doc.body().html();
        final String indicator = "featuresSpecifications";

        int start = body.indexOf(indicator) + indicator.length() + 3;
        int end = body.indexOf("\",\"type\"");

        List<LinkSpec> specList = null;

        if (start > indicator.length() && end > start) {
            String features = body.substring(start, end);
            String[] specs = features.split("•");
            if (specs.length > 0) {
                specList = new ArrayList<>();
                for (String spec : specs) {
                    if (! spec.trim().isEmpty()) specList.add(new LinkSpec("", spec.trim()));
                }
            }
        }
        return specList;
    }
}
