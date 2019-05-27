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

    private JSONObject offers;

    public Teknosa() {
        super("Teknosa");
    }

    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("script[type='application/ld+json']");
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().trim());
            if (data.has("offers")) {
                JSONArray offersArray = offers.getJSONArray("offers");
                if (! offersArray.isEmpty()) {
                    offers = offersArray.getJSONObject(0);
                }
            }
            return data;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        Element availablity = doc.selectFirst("div.stock-status.in-stock");
        if (availablity != null) {
            return availablity.text().contains("Stokta var");
        }

        return super.isAvailable();
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
