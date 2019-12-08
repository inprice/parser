package io.inprice.scrapper.worker.websites.ca;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for BestBuy Canada
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
public class BestBuy extends AbstractWebsite {

    /*
     * the main data provider derived from json placed in html
     */
    private JSONObject product;

    public BestBuy(Link link) {
        super(link);
    }

    /**
     * The data we looking for is in html body.
     * So, we get it by using String manipulations
     */
    @Override
    public JSONObject getJsonData() {
        final String prodData = findAPart(doc.html(), "\"product\":", ",\"productSellers\":{");

        if (prodData != null) {
            JSONObject data = new JSONObject(prodData);
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
        return false;
    }

    @Override
    public String getSku() {
        if (product != null && product.has("sku")) {
            return product.getString("sku");
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        if (product != null && product.has("name")) {
            return product.getString("name");
        }
        return Consts.Words.NOT_AVAILABLE;
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
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        Elements specs = doc.select("div#MoreInformation li");
        if (specs == null || specs.isEmpty()) specs = doc.select("div#MoreInformation p");
        return getValueOnlySpecList(specs);
    }
}
