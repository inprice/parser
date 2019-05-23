package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Otto extends AbstractWebsite {

    private JSONObject variation = null;

    @Override
    public JSONObject getJsonData() {
        Element data = doc.selectFirst("script#productDataJson");
        if (data != null) {
            JSONObject result = new JSONObject(data.dataNodes().get(0).getWholeData().trim());
            if (! result.isEmpty() && result.has("variations")) {
                JSONObject variations = result.getJSONObject("variations");
                Set<String> keySet = variations.keySet();
                if (keySet != null && keySet.size() > 0) {
                    variation = variations.getJSONObject(keySet.iterator().next());
                }
            }
            return result;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (json != null && variation != null) {
            JSONObject var = variation.getJSONObject("availability");
            if (! var.isEmpty()) {
                return "available".equals(var.getString("status"));
            }
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        if (json != null && json.has("id")) {
            return json.getString("id");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (json != null && variation != null) {
            return variation.getString("name");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && variation != null) {
            JSONObject var = variation.getJSONObject("displayPrice");
            if (! var.isEmpty()) {
                return var.getBigDecimal("techPriceAmount");
            }
        }

        Element price = doc.getElementById("reducedPriceAmount");
        if (price == null) price = doc.getElementById("normalPriceAmount");

        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content").trim()));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Otto";
    }

    @Override
    public String getShipment() {
        if (json != null && variation != null) {
            JSONObject var = variation.getJSONObject("availability");
            if (! var.isEmpty()) {
                return var.getString("displayName");
            }
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brand")) {
            return json.getString("brand");
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("ul.prd_unorderedList li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String val = spec.text().trim();
                if (! val.isEmpty()) specList.add(new LinkSpec("", val));
            }
        }
        return specList;
    }

}
