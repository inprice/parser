package io.inprice.scrapper.worker.websites.uk;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Asda UK
 *
 * Contains json data placed in html. So, all data is extracted from json
 *
 * @author mdpinar
 */
public class Asda extends AbstractWebsite {

    private JSONObject product;

    public Asda(Link link) {
        super(link);
    }

    /**
     * Returns json object which holds all the essential data
     *
     * @return json - product data
     */
    @Override
    public JSONObject getJsonData() {
        String[] urlChunks = getUrl().split("/");

        if (urlChunks.length > 1) {
            String productId = urlChunks[urlChunks.length-1];
            if (productId.matches("\\d+")) {
                HttpResponse<String> response = HttpClient.get("https://groceries.asda.com/api/items/view?itemid=" + productId);
                if (response.getStatus() == 200 && ! response.getBody().isEmpty()) {
                    JSONObject prod = new JSONObject(response.getBody());
                    if (prod.has("items")) {
                        JSONArray items = prod.getJSONArray("items");
                        if (items.length() > 0) {
                            product = items.getJSONObject(0);
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean isAvailable() {
        if (product != null && product.has("availability")) {
            return "A".equals(product.getString("availability"));
        }
        return false;
    }

    @Override
    public String getSku() {
        if (product != null && product.has("id")) {
            return product.getString("id");
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
        if (product != null && product.has("price")) {
            return new BigDecimal(cleanPrice(product.getString("price")));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Asda";
    }

    @Override
    public String getShipment() {
        return "In-store pickup.";
    }

    @Override
    public String getBrand() {
        if (product != null && product.has("brandName")) {
            return product.getString("brandName");
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return null;
    }

}
