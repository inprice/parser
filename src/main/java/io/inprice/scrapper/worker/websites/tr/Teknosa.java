package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.generic.GenericWebsiteT1;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Teknosa Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 * Please refer GenericWebsiteT1 for extra information
 *
 * @author mdpinar
 */
public class Teknosa extends GenericWebsiteT1 {

    public Teknosa(Link link) {
        super(link,"Teknosa");
    }

    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("script[type='application/ld+json']");
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().trim());
            if (data.has("offers")) {
                JSONArray offersArray = data.getJSONArray("offers");
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
        Element availability = doc.selectFirst("div.stock-status.in-stock");
        if (availability != null) {
            return availability.text().contains("Stokta var");
        }
        return false;
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
