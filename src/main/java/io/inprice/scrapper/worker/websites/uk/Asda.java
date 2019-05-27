package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Asda extends AbstractWebsite {

    private String brand = "NA";
    private List<LinkSpec> specList;

    private JSONObject product;

    @Override
    public JSONObject getJsonData() {
        final String html = doc.html();
        final String indicator = "window.productJSON";

        final int start = html.indexOf(indicator) + indicator.length() + 3;
        final int end = html.indexOf("};", start) + 1;

        if (start > 0 && end > start) {
            json = new JSONObject(html.substring(start, end));
        }

        if (json != null) {
            if (json.has("product")) {
                product = json.getJSONObject("product");

                if (product.has("attributes")) {
                    JSONArray attributes = product.getJSONArray("attributes");
                    if (! attributes.isEmpty()) {
                        specList = new ArrayList<>();
                        for (int i = 0; i < attributes.length(); i++) {
                            JSONObject attr = attributes.getJSONObject(i);
                            String key = null;
                            String value = null;
                            if (attr.has("key")) key = attr.getString("key");
                            if (attr.has("value")) value = attr.getString("value");
                            if ("Brand".equals(key)) brand = value;
                            specList.add(new LinkSpec(key, value));
                        }
                    }
                }
            }
            return json;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (product != null && product.has("available")) {
            return product.getBoolean("available");
        }
        return super.isAvailable();
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
        if (product != null) {
            if (product.has("price")) {
                JSONObject price = product.getJSONObject("price");
                if (price.has("sale")) {
                    return price.getJSONObject("sale").getBigDecimal("value");
                }
            }

            if (product.has("variants")) {
                JSONArray variants = product.getJSONArray("variants");
                if (variants.length() > 0) {
                    JSONObject firstVariant = variants.getJSONObject(0);
                    if (firstVariant.has("price")) {
                        JSONObject price = variants.getJSONObject(0).getJSONObject("price");
                        if (price.has("list")) {
                            JSONObject list = price.getJSONObject("list");
                            if (list.has("decimalPrice")) {
                                return list.getBigDecimal("decimalPrice");
                            }
                        }
                    }
                }
            }
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Asda";
    }

    @Override
    public String getShipment() {
        return "NA";
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return specList;
    }

}
