package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Currys extends AbstractWebsite {

    @Override
    public JSONObject getJsonData() {
        Element data = doc.getElementById("app.digitalData");
        if (data != null) {
            return new JSONObject(data.dataNodes().get(0).getWholeData().trim());
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("product") && ! json.getJSONArray("product").isEmpty()) {
            JSONObject product = json.getJSONArray("product").getJSONObject(0);
            if (product.has("stockStatus")) {
                String status = product.getString("stockStatus");
                return "In stock".equals(status);
            }
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        if (json != null && json.has("product") && ! json.getJSONArray("product").isEmpty()) {
            JSONObject product = json.getJSONArray("product").getJSONObject(0);
            if (product.has("productSKU")) {
                return product.getString("productSKU");
            }
        }
        return null;
    }

    @Override
    public String getName() {
        if (json != null && json.has("product") && ! json.getJSONArray("product").isEmpty()) {
            JSONObject product = json.getJSONArray("product").getJSONObject(0);
            if (product.has("name")) {
                return product.getString("name");
            }
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && json.has("product") && ! json.getJSONArray("product").isEmpty()) {
            JSONObject product = json.getJSONArray("product").getJSONObject(0);
            if (product.has("currentPrice")) {
                return product.getBigDecimal("currentPrice");
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Currys";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.getElementById("delivery");
        if (shipment != null) {
            return shipment.text().replaceAll("More info", "").trim();
        }
        return null;
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("product") && ! json.getJSONArray("product").isEmpty()) {
            JSONObject product = json.getJSONArray("product").getJSONObject(0);
            if (product.has("manufacturer")) {
                return product.getString("manufacturer");
            }
        }
        return null;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div.product-highlight li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                specList.add(new LinkSpec("", spec.text().trim()));
            }
        }
        return specList;
    }
}
