package io.inprice.scrapper.worker.websites.ca;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.AbstractSpasite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * SPA based parser for Walmart Canada
 * Please note that: Since website is a kind of SPA page, its url is never directly used!
 *
 * The parsing steps:
 *
 *  - we need a payload to get essential data
 *  - in order to do that we need two info:
 *      a) product-id
 *      b) sku
 *
 * in getPayload(), that payload is generated by using product-id and sku
 *
 *  Product-id
 *  - is derived from url by splitting it up by forward slash /
 *  - the last part of url is the product-id
 *  SKU
 *  - the html body of link's url contains sku data as json
 *
 *  - in getJsonData(), the data is pulled with the payload whose steps are explained above
 *  - all data is built by using html body, preData set in getPayload(), and json object set in getJsonData()
 *
 * @author mdpinar
 */
public class Walmart extends AbstractSpasite {

    //used in pre-request
    private static final String STATIC_DATA = "{'availabilityStoreId':'3124','fsa':'P7B','lang':'en','products':[{'productId':'%s','skuIds':['%s']}]}";

    //used for getting the data
    private static final String STATIC_URL  = "https://www.walmart.ca/api/product-page/price-offer";

    /*
     * the main data derived from json placed in html
     */
    private JSONObject preData;

    private String sku;

    public Walmart(Link link) {
        super(link);
    }

    /**
     * In order to generate the payload to request the data;
     * the method uses html body for extracting sku, and link's url for product id
     *
     * @return String - a link for the data pulling from the server
     */
    private String getPayload() {
        Element preDataEL = doc.selectFirst("div.js-content script[type='application/ld+json']");
        if (preDataEL != null) {
            preData = new JSONObject(preDataEL.dataNodes().get(0).getWholeData().trim());
            if (preData.has("sku")) {
                sku = preData.getString("sku");
                String[] urlChunks = getMainUrl().split("/");
                if (urlChunks.length > 0) {
                    return String.format(STATIC_DATA, urlChunks[urlChunks.length - 1], sku).replaceAll("'", "\"");
                }
            }
        }
        return null;
    }

    /**
     * Request the essential data with a constant url with payload
     *
     * @return JSONObject - json
     */
    @Override
    public JSONObject getJsonData() {
        final String payload = getPayload();
        if (payload != null && ! payload.isEmpty()) {
            String body = HttpClient.post(STATIC_URL, payload);
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
