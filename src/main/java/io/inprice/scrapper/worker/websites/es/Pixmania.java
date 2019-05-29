package io.inprice.scrapper.worker.websites.es;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.AbstractSPAsite;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pixmania extends AbstractSPAsite {

    private JSONObject bestOffer;

    private String findProductId() {
        final String[] urlChunks = url.split("\\?");
        if (urlChunks.length > 0) {
            final String[] partChunks = urlChunks[0].split("-");
            if (partChunks.length > 0) {
                return partChunks[partChunks.length-1];
            }
        }
        return null;
    }

    private String findTokenData() {
        String body = HttpClient.get("https://www.pixmania.es/api/ecrm/session");
        if (! body.trim().isEmpty()) {
            JSONObject tokenData = new JSONObject(body.trim());
            if (tokenData.has("session")) {
                return tokenData.getJSONObject("session").getString("token");
            }
        }
        return null;
    }

    @Override
    public JSONObject getJsonData() {
        final String productId = findProductId();
        if (productId == null || productId.isEmpty()) return null;

        final String token = findTokenData();
        if (token == null || token.trim().isEmpty()) return null;

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Language", "es-ES");

        String body = HttpClient.get(String.format("https://www.pixmania.es/api/pcm/products/%s", productId), headers);
        if (body != null && ! body.trim().isEmpty()) {
            JSONObject data = new JSONObject(body.trim());
            if (data.has("product")) {
                JSONObject product = data.getJSONObject("product");
                if (product.has("best_offer")) {
                    bestOffer = product.getJSONObject("best_offer");
                }
                return product;
            }
        }
        return null;
    }

    @Override
    public boolean isAvailable() {
        if (bestOffer != null && bestOffer.has("stock")) {
            return bestOffer.getInt("stock") > 0;
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        if (bestOffer != null && bestOffer.has("sku")) {
            return bestOffer.getString("sku");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (bestOffer != null && bestOffer.has("name")) {
            return bestOffer.getString("name");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (bestOffer != null && bestOffer.has("price_with_vat")) {
            return bestOffer.getBigDecimal("price_with_vat");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        if (bestOffer != null && bestOffer.has("merchant")) {
            JSONObject merchant = bestOffer.getJSONObject("merchant");
            if (merchant.has("name")) {
                return merchant.getString("name");
            }
        }
        return "NA";
    }

    @Override
    public String getShipment() {
        if (bestOffer != null && bestOffer.has("free_shipping")) {
            return "Free shipping: " + bestOffer.getBoolean("free_shipping");
        }
        return "NA";
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

        if (json != null && json.has("description")) {
            final String description = json.getString("description");
            final String[] descChunks = description.split("\\\\n");
            if (descChunks.length > 0) {
                specList = new ArrayList<>();
                for (String desc: descChunks) {
                    specList.add(new LinkSpec("", desc));
                }
            }
        }
        return specList;
    }

}
