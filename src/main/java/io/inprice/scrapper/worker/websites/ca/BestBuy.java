package io.inprice.scrapper.worker.websites.ca;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BestBuy extends AbstractWebsite {

    private JSONObject product;

    @Override
    public JSONObject getJsonData() {
        int start = doc.data().indexOf("\"product\":{") + 10;
        int end   = doc.data().indexOf(",\"productSellers\":{");

        if (start > 10 && end > start) {
            //System.out.println(doc.data().substring(start, end));
            JSONObject data = new JSONObject(doc.data().substring(start, end));
            if (data.has("product")) product = data.getJSONObject("product");
            return data;
        }

        return null;
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("isAvailabilityError")) {
            return ! json.getBoolean("isAvailabilityError");
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        if (product != null && product.has("sku")) {
            return product.getString("sku");
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
        if (product != null && product.has("priceWithEhf")) {
            return product.getBigDecimal("priceWithEhf");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        if (product != null && ! product.isNull("seller")) {
            JSONObject seller = product.getJSONObject("seller");
            if (seller != null) {
                return seller.getString("name");
            }
        }
        return "Best Buy";
    }

    @Override
    public String getShipment() {
        return "Sold and shipped by " + getSeller();
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
        List<LinkSpec> specList = null;

        Elements specs = doc.select("div#MoreInformation li");
        if (specs == null || specs.isEmpty()) specs = doc.select("div#MoreInformation p");

        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                specList.add(new LinkSpec("", spec.text().trim()));
            }
        }
        return specList;
    }
}
