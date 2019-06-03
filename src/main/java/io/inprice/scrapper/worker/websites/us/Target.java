package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.*;

/**
 * Parser for Target USA
 *
 * The parsing steps:
 *
 *  - the html body of link's url contains data (in json format) we need
 *  - in getJsonData(), we get that json data by using substring() method of String class
 *  - this data is named as product which is hold on a class-level variable
 *  - each data (except for availability and specList) can be gathered using product variable
 *
 * @author mdpinar
 */
public class Target extends AbstractWebsite {

    private JSONObject preLoad;
    private JSONObject product;
    private JSONObject priceData;

    public Target(Link link) {
        super(link);
    }

    private void setPreLoadData() {
        final String indicator = "__PRELOADED_STATE__= ";
        final String html = doc.html();

        int start = html.indexOf(indicator) + indicator.length();
        int end   = html.indexOf("</script>", start);

        if (start > indicator.length() && end > start) {
            preLoad = new JSONObject(html.substring(start, end));
            if (preLoad.has("product")) {
                JSONObject prod = preLoad.getJSONObject("product");
                if (prod.has("productDetails")) {
                    JSONObject details = prod.getJSONObject("productDetails");
                    if (details.has("item")) {
                        product = details.getJSONObject("item");
                    }
                }
            }
        }
    }

    private String getApiKey() {
        if (preLoad != null && preLoad.has("config")) {
            JSONObject config = preLoad.getJSONObject("config");
            if (config.has("firefly")) {
                return config.getJSONObject("firefly").getString("apiKey");
            }
        }
        return null;
    }

    /**
     * Returns payload as key value maps
     *
     * @return Map - payload required for having the data
     */
    private Map<String, String> getPayload() {
        Map<String, String> payload = new HashMap<>();
        payload.put("pricing_store_id", "1531");
        payload.put("key", getApiKey());
        return payload;
    }

    @Override
    protected JSONObject getJsonData() {
        setPreLoadData();
        return super.getJsonData();
    }

    private void setPriceData() {
        final Map<String, String> payload = getPayload();
        if (payload != null) {
            String body = HttpClient.get("https://redsky.target.com/web/pdp_location/v1/tcin/" + getSku(), payload, true);
            if (body != null && ! body.trim().isEmpty()) {
                JSONObject priceEL = new JSONObject(body.trim());
                if (! priceEL.isEmpty()) {
                    priceData = priceEL.getJSONObject("price");
                }
            }
        }
    }

    @Override
    public boolean isAvailable() {
        if (product != null) {
            JSONObject available = null;
            if (product.has("available_to_promise_network")) {
                available = product.getJSONObject("available_to_promise_network");
            } else if (product.has("children")) {
                JSONObject children = product.getJSONObject("children");
                if (! children.isEmpty()) {
                    Iterator<String> keys = children.keys();
                    if (keys.hasNext()) {
                        JSONObject firstChild = children.getJSONObject(keys.next());
                        available = firstChild.getJSONObject("available_to_promise_network");
                    }
                }
            }
            if (available != null) {
                return available.has("availability_status") && "IN_STOCK".equals(available.getString("availability_status"));
            }
        }
        return false;
    }

    @Override
    public String getSku() {
        if (product != null && product.has("productId")) {
            return product.getString("productId");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (product != null && product.has("title")) {
            return product.getString("title");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (product != null && product.has("price")) {
            JSONObject priceEL = product.getJSONObject("price");
            if (priceEL.has("price")) {
                String price = priceEL.getString("price");
                if (! price.isEmpty()) return new BigDecimal(price);
            }
        }

        setPriceData();
        if (priceData != null) {
            if (priceData.has("current_retail")) {
                return priceData.getBigDecimal("current_retail");
            }
            if (priceData.has("current_retail_max")) {
                return priceData.getBigDecimal("current_retail_max");
            }
            if (priceData.has("current_retail_min")) {
                return priceData.getBigDecimal("current_retail_min");
            }
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        if (product != null && product.has("productVendorName")) {
            String seller = product.getString("productVendorName");
            if (seller.isEmpty()) return getBrand();
        }
        return "Target";
    }

    @Override
    public String getShipment() {
        return "Free order pickup";
    }

    @Override
    public String getBrand() {
        if (product != null && product.has("manufacturerBrand")) {
            return product.getString("manufacturerBrand");
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        if (product != null && product.has("itemDetails")) {
            JSONObject details = product.getJSONObject("itemDetails");
            if (details.has("bulletDescription")) {
                JSONArray bullets = details.getJSONArray("bulletDescription");
                if (! bullets.isEmpty()) {
                    specList = new ArrayList<>();
                    for (int i = 0; i < bullets.length(); i++) {
                        String spec = bullets.getString(i);
                        String[] specChunks = spec.split("</B>");
                        String key = "";
                        String value;
                        if (specChunks.length > 1) {
                            key = specChunks[0];
                            value = specChunks[1];
                        } else {
                            value = specChunks[0];
                        }
                        specList.add(new LinkSpec(key.replaceAll(":|<B>", ""), value));
                    }
                }
            }
        }
        return specList;
    }
}
