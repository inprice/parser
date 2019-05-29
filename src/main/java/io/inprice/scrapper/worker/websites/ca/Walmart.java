package io.inprice.scrapper.worker.websites.ca;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.AbstractSPAsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Walmart extends AbstractSPAsite {

    private String sku;
    private JSONObject preData;

    private String getRequestData() {
        Element preDataEL = doc.selectFirst("div.js-content script[type='application/ld+json']");
        if (preDataEL != null) {
            preData = new JSONObject(preDataEL.dataNodes().get(0).getWholeData().trim());
            if (preData.has("sku")) {
                sku = preData.getString("sku");
                String[] urlChunks = url.split("/");
                if (urlChunks.length > 0) {
                    return String.format("{'availabilityStoreId':'3124','fsa':'P7B','lang':'en','products':[{'productId':'%s','skuIds':['%s']}]}",
                            urlChunks[urlChunks.length - 1], sku).replaceAll("'", "\"");
                }
            }
        }
        return null;
    }

    @Override
    public JSONObject getJsonData() {
        final String requestData = getRequestData();
        if (requestData != null && ! requestData.isEmpty()) {
            String body = HttpClient.post("https://www.walmart.ca/api/product-page/price-offer", requestData);
            if (! body.trim().isEmpty()) {
                JSONObject product = new JSONObject(body.trim());
                if (product.has("offers")) {
                    JSONObject offers = product.getJSONObject("offers");
                    if (offers.has(sku)) return offers.getJSONObject(sku); //find all the detail by sku
                }
            }
        }
        return null;
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("gmAvailability")) {
            return "Available".equals(json.getString("gmAvailability"));
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        return sku;
    }

    @Override
    public String getName() {
        if (preData != null && preData.has("name")) {
            return preData.getString("name");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (json !=  null && json.has("currentPrice")) {
            return json.getBigDecimal("currentPrice");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        if (json != null && json.has("sellerInfo")) {
            JSONObject sellerInfo = json.getJSONObject("sellerInfo");
            if (sellerInfo.has("en")) return sellerInfo.getString("en");
        }
        return "NA";
    }

    @Override
    public String getShipment() {
        if (getSeller() != null) {
            return "Sold & shipped by " + getSeller();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        if (preData != null && preData.has("brand")) {
            JSONObject brand = preData.getJSONObject("brand");
            if (brand.has("name")) {
                return brand.getString("name");
            }
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        final String body = doc.body().html();
        final String indicator = "featuresSpecifications";

        int start = body.indexOf(indicator) + indicator.length() + 3;
        int end = body.indexOf("\",\"type\"");

        List<LinkSpec> specList = null;

        if (start > indicator.length() && end > start) {
            String features = body.substring(start, end);
            String[] specs = features.split("•");
            if (specs.length > 0) {
                specList = new ArrayList<>();
                for (String spec : specs) {
                    if (! spec.trim().isEmpty()) {
                        final String clean = spec.replaceAll(".u2028|.u003C", "").replaceAll("br>", "");
                        specList.add(new LinkSpec("", clean));
                    }
                }
            }
        }
        return specList;
    }
}
