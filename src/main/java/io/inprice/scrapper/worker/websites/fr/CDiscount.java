package io.inprice.scrapper.worker.websites.fr;

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

/**
 * Parser for CDiscount France
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class CDiscount extends AbstractWebsite {

    /*
     * holds price info set in getJsonData()
     */
    private JSONObject offers;

    public CDiscount(Link link) {
        super(link);
    }

    @Override
    public JSONObject getJsonData() {
        Elements scripts = doc.select("script[type='application/ld+json']");
        if (scripts != null) {
            Element dataEL = null;
            for (Element script: scripts) {
                if (script.html().contains("itemCondition")) {
                    dataEL = script;
                    break;
                }
            }
            if (dataEL != null) {
                JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().trim());
                if (data.has("offers")) {
                    if (data.has("offers")) {
                        offers = data.getJSONObject("offers");
                    }
                }
                return data;
            }
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (offers != null && offers.has("availability")) {
            return offers.getString("availability").contains("InStock");
        }
        return false;
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
        if (json != null && json.has("name")) {
            return json.getString("name");
        }
        return "NA";
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
        Element seller = doc.selectFirst("a.fpSellerName");
        if (seller == null) seller = doc.selectFirst("span.logoCDS");

        if (seller != null) {
            return seller.text();
        }
        return "CDiscount";
    }

    @Override
    public String getShipment() {
        return "Vendu et expédié par " + getSeller();
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brand")) {
            JSONObject merchant = json.getJSONObject("brand");
            if (merchant.has("name")) {
                return merchant.getString("name");
            }
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        Elements specs = doc.select("div#fpBulletPointReadMore li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec: specs) {
                String[] specChunks = spec.text().split(":");
                LinkSpec ls = new LinkSpec(specChunks[0], "");
                if (specChunks.length > 1) {
                    ls.setValue(specChunks[1]);
                }
                specList.add(ls);
            }
        }

        if (specList == null) {
            specs = doc.select("table.fpDescTb tr");
            if (specs != null && specs.size() > 0) {
                specList = new ArrayList<>();
                for (Element spec: specs) {
                    Elements pairs = spec.select("td");
                    if (pairs.size() > 0) {
                        LinkSpec ls = new LinkSpec(pairs.get(0).text(), "");
                        if (pairs.size() > 1) {
                            ls.setValue(pairs.get(1).text());
                        }
                        specList.add(ls);
                    }
                }
            }
        }

        return specList;
    }
}
