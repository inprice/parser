package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.generic.GenericWebsiteT1;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Teknosa extends GenericWebsiteT1 {

    public Teknosa() {
        super("Teknosa");
    }

    @Override
    public String getSku() {
        if (json != null && json.has("sku")) {
            return json.getString("sku");
        }
        return super.getSku();
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("offers")) {
            JSONObject offers = json.getJSONObject("offers");
            if (! offers.isEmpty() && offers.has("offers")) {
                JSONArray offersArray = offers.getJSONArray("offers");
                if (! offersArray.isEmpty() && ! offersArray.isEmpty()) {
                    String status = offersArray.getJSONObject(0).getString("availability");
                    return status.endsWith("InStock");
                }
            }
        }
        return super.isAvailable();
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && json.has("offers")) {
            JSONObject offers = json.getJSONObject("offers");
            if (! offers.isEmpty() && offers.has("lowPrice")) {
                return offers.getBigDecimal("lowPrice");
            } else if (! offers.isEmpty() && offers.has("price")) {
                return offers.getBigDecimal("price");
            }
        }
        return super.getPrice();
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        Elements specKeys = doc.select("div.product-classifications tr");
        if (specKeys != null && specKeys.size() > 0) {
            specList = new ArrayList<>();
            for (Element key : specKeys) {
                Element val = key.selectFirst("td");
                specList.add(new LinkSpec(val.text().trim(), ""));
            }
        }

        Elements specValues = doc.select("div.product-classifications tr");
        if (specValues != null && specValues.size() > 0) {
            boolean isEmpty = false;
            if (specList == null) {
                isEmpty = true;
                specList = new ArrayList<>();
            }
            for (int i = 0; i < specValues.size(); i++) {
                Element value = specValues.get(i);
                Element val = value.selectFirst("td span");
                if (isEmpty) {
                    specList.add(new LinkSpec("", val.text().trim()));
                } else {
                    specList.get(i).setValue(val.text().trim());
                }
            }
        }

        return specList;
    }

}
