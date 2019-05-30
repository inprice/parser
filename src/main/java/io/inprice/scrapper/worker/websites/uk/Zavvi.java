package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.Link;
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

    private JSONObject offers;

    public Zavvi(Link link) {
        super(link);
    }

    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("script[type='application/ld+json']");
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().trim());

            if (data.has("offers")) {
                JSONArray offersArray = data.getJSONArray("offers");
                if (! offersArray.isEmpty()) {
                    if (offersArray.getJSONObject(0).has("sku")) {
                        offers = offersArray.getJSONObject(0);
                    }
                }
            }

            return data;
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
        if (offers != null && offers.has("offers")) {
            return offers.getString("sku");
        }
        return "NA";
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
        if (offers != null && offers.has("price")) {
            return offers.getBigDecimal("price");
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
        return "NA";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brand")) {
            return json.getJSONObject("brand").getString("name");
        }
        return "NA";
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
                    strKey = key.text().replaceAll(":", "").trim();
                }
                if (value != null) strValue = value.text().trim();

                specList.add(new LinkSpec(strKey, strValue));
            }
        }
        return specList;
    }
}
