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

public class Zavvi extends AbstractWebsite {

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
        Element stock = doc.selectFirst("p.productStockInformation_prefix");
        if (stock != null) {
            return "In stock".equals(stock.text().trim());
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        if (json != null && json.has("offers")) {
            JSONArray offers = json.getJSONArray("offers");
            if (! offers.isEmpty()) {
                if (offers.getJSONObject(0).has("sku")) {
                    return offers.getJSONObject(0).getString("sku");
                }
            }
        }
        return null;
    }

    @Override
    public String getName() {
        if (json != null && json.has("name")) {
            return json.getString("name").trim();
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && json.has("offers")) {
            JSONArray offers = json.getJSONArray("offers");
            if (! offers.isEmpty()) {
                if (offers.getJSONObject(0).has("price")) {
                    return offers.getJSONObject(0).getBigDecimal("price");
                }
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Zavvi";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.productDeliveryAndReturns_message");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return null;
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brand")) {
            return json.getJSONObject("brand").getString("name");
        }
        return null;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div.productDescription_contentWrapper");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                Element key = spec.selectFirst("div.productDescription_contentPropertyName span");
                Element value = spec.selectFirst("div.productDescription_contentPropertyValue");

                String strKey = null;
                String strValue= null;

                if (key != null) {
                    strKey = key.text().replaceAll("\\:", "").trim();
                }
                if (value != null) strValue = value.text().trim();

                specList.add(new LinkSpec(strKey, strValue));
            }
        }
        return specList;
    }
}
