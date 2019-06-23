package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Parser for Otto Deutschland
 *
 * The parsing steps:
 *
 * Three types of data is used for extracting all the info:
 *      a) html body (specList)
 *      b) json object extracted from html body in a script tag (brand)
 *      c) product object set in getJsonData() by using json object in step b
 *
 *  - the html body of link's url contains data (in json format) we need
 *  - in getJsonData(), we get that json data placed in a specific script tag
 *  - this data is named as product which is hold on a class-level variable
 *
 * @author mdpinar
 */
public class Otto extends AbstractWebsite {

    /*
     * the main data provider derived from json placed in html
     */
    private JSONObject product;

    public Otto(Link link) {
        super(link);
    }

    @Override
    public JSONObject getJsonData() {
        Element data = doc.selectFirst("script#productDataJson");
        if (data != null) {
            JSONObject result = new JSONObject(data.dataNodes().get(0).getWholeData().trim());
            if (! result.isEmpty() && result.has("variations")) {
                JSONObject variations = result.getJSONObject("variations");
                Set<String> keySet = variations.keySet();
                if (keySet != null && keySet.size() > 0) {
                    product = variations.getJSONObject(keySet.iterator().next());
                }
            }
            return result;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (product != null && product.has("availability")) {
            JSONObject var = product.getJSONObject("availability");
            if (var.has("status")) {
                return "available".equals(var.getString("status")) || "delayed".equals(var.getString("status"));
            }
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("meta[itemprop='sku']");
        if (sku != null) {
            return sku.attr("content");
        }

        if (json != null && json.has("id")) {
            return json.getString("id");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (product != null && product.has("name")) {
            return product.getString("name");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.getElementById("reducedPriceAmount");
        if (price == null) price = doc.getElementById("normalPriceAmount");

        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content").trim()));
        }

        if (product != null && product.has("displayPrice")) {
            JSONObject var = product.getJSONObject("displayPrice");
            if (var.has("techPriceAmount")) {
                return var.getBigDecimal("techPriceAmount");
            }
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Otto";
    }

    @Override
    public String getShipment() {
        if (product != null && product.has("availability")) {
            JSONObject var = product.getJSONObject("availability");
            if (var.has("displayName")) {
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
        return getValueOnlySpecList(doc.select("ul.prd_unorderedList li"));
    }

}
