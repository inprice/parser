package io.inprice.scrapper.worker.websites.xx;

import com.google.gson.JsonObject;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Rakuten Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Rakuten extends AbstractWebsite {

    public Rakuten(Link link) {
        super(link);
    }

    @Override
    protected JSONObject getJsonData() {
        final String indicator = "productDetails =";

        int start = doc.html().indexOf(indicator) + indicator.length();
        int end   = doc.html().indexOf("};", start) + 1;

        if (start > indicator.length() && end > start) {
            return new JSONObject(doc.html().substring(start, end));
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("isAvailable")) {
            return json.getBoolean("isAvailable");
        }

        Element available = doc.selectFirst("meta[property='product:availability']");
        if (available != null) {
            return available.attr("content").trim().contains("in stock");
        }
        return false;
    }

    @Override
    public String getSku() {
        if (json != null && json.has("articleNumber")) {
            return json.getString("articleNumber");
        }

        Element sku = doc.selectFirst("meta[property='product:retailer_item_id']");
        if (sku != null) {
            return sku.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (json != null && json.has("name")) {
            return json.getString("name");
        }

        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && json.has("price")) {
            return json.getBigDecimal("price");
        }

        Element price = doc.selectFirst("meta[property='product:price:amount']");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content")));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        if (json != null && json.has("details")) {
            JSONObject details =  json.getJSONObject("details");
            if (details != null && details.has("merchant")) {
                JSONObject seller = details.getJSONObject("merchant");
                if (seller.has("company")) {
                    return seller.getString("company");
                }
            }
        }

        Element brand = doc.selectFirst("meta[property='og:description']");
        if (brand != null) {
            return brand.attr("content").trim();
        }
        return "Rakuten";
    }

    @Override
    public String getShipment() {
        if (json != null && json.has("shipping")) {
            JSONObject shipping = json.getJSONObject("shipping");
            if (shipping.has("price")) {
                BigDecimal shippingPrice = shipping.getBigDecimal("price");
                if (shippingPrice.compareTo(BigDecimal.ZERO) > 0) {
                    return "Shipping cost " + shippingPrice;
                } else {
                    return "Free shipping";
                }
            }
        }

        return "NA";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brandName")) {
            return json.getString("brandName");
        }

        Element brand = doc.selectFirst("meta[property='product:brand']");
        if (brand != null) {
            return brand.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        if (json != null && json.has("details")) {
            JSONObject details = json.getJSONObject("details");
            if (details.has("features")) {
                specList = new ArrayList<>();

                JSONArray features = details.getJSONArray("features");
                for (int i = 0; i < features.length(); i++) {
                    JSONObject pair = features.getJSONObject(i);
                    specList.add(new LinkSpec(pair.getString("key"), pair.getString("value")));
                }
            }
        }

        if (specList == null || specList.size() == 0) {
            return getValueOnlySpecList(doc.select("div.product-description li"));
        }

        return specList;
    }
}
