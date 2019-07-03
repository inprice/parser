package io.inprice.scrapper.worker.websites.us;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
     * Returns payload as key value query string
     *
     * @return String - as query parameter payload
     */
    private String getPayload() {
        StringBuilder sb = new StringBuilder();
        sb.append("pricing_store_id=1531");
        sb.append("&");
        sb.append("key=");
        sb.append(getApiKey());
        return sb.toString();
    }

    @Override
    protected JSONObject getJsonData() {
        setPreLoadData();
        return super.getJsonData();
    }

    private void setPriceData() {
        HttpResponse<String> response = HttpClient.get("https://redsky.target.com/web/pdp_location/v1/tcin/" + getSku() + "?" + getPayload());
        if (response != null && response.getStatus() < 400) {
            JSONObject priceEL = new JSONObject(response.getBody());
            if (! priceEL.isEmpty()) {
                priceData = priceEL.getJSONObject("price");
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
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        if (product != null && product.has("title")) {
            return product.getString("title");
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        if (product != null && product.has("price")) {
            JSONObject priceEL = product.getJSONObject("price");
            if (priceEL.has("price")) {
                String price = priceEL.getString("price");
                if (! price.trim().isEmpty()) return new BigDecimal(price.trim());
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
            if (! seller.isEmpty()) return seller;
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
        return Constants.NOT_AVAILABLE;
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
