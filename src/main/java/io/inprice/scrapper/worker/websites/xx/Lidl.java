package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Lidl Global
 *
 * Contains standard data, all is extracted from html body and via json data in getJsonData()
 *
 * @author mdpinar
 */
public class Lidl extends AbstractWebsite {

    public Lidl(Link link) {
        super(link);
    }

    @Override
    public JSONObject getJsonData() {
        Element data = doc.selectFirst("section.page__section script");
        if (data == null) data = doc.selectFirst("div.hidden-trackers.no-static-copy script");

        if (data != null) {
            String rawData = data.dataNodes().get(0).getWholeData();
            String pureData = rawData.replaceAll("var dynamic_tm_data = ", "").replaceAll(";", "").trim();
            return new JSONObject(pureData);
        }

        return null;
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("product_instock")) {
            return json.getInt("product_instock") > 0;
        }
        return false;
    }

    @Override
    public String getSku() {
        if (json != null && json.has("productid")) {
            return json.getString("productid").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (json != null && json.has("productname")) {
            return json.getString("productname");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && json.has("amount")) {
            return json.getBigDecimal("amount");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Lidl";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.delivery span");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("productbrand")) {
            return json.getString("productbrand").trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div.product-detail-hero li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String value = spec.text().trim();
                specList.add(new LinkSpec("", value));
            }

            return specList;
        }

        specs = doc.select("div.attributebox__keyfacts li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String strSpec = spec.text().trim();
                String key = "";
                String value = strSpec.trim();

                if (strSpec.contains(":")) {
                    String[] specChunks = strSpec.split(":");
                    if (specChunks.length > 1) {
                        key = specChunks[0].trim();
                        value = specChunks[1].trim();
                    }
                }
                specList.add(new LinkSpec(key, value));
            }
        }
        return specList;
    }
}
