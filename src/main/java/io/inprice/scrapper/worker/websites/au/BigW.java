package io.inprice.scrapper.worker.websites.au;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for BigW Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class BigW extends AbstractWebsite {

    private String brand = "NA";
    private List<LinkSpec> specList;

    public BigW(Link link) {
        super(link);
    }

    @Override
    protected JSONObject getJsonData() {
        specList = getKeyValueSpecList(doc.select("div.tab-Specification li"), "div.meta", "div.subMeta");
        for (LinkSpec spec: specList) {
            if (spec.getKey().contains("Brand")) {
                brand = spec.getValue();
                break;
            }
        }

        final String indicator = "'products': [";

        int start = doc.html().indexOf(indicator) + indicator.length();
        int end   = doc.html().indexOf("}]", start) + 1;

        if (start > indicator.length() && end > start) {
            return new JSONObject(doc.html().substring(start, end));
        }

        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        Element increaeBtn = doc.getElementById("increase_quantity_JS");
        return (increaeBtn != null);
    }

    @Override
    public String getSku() {
        Element code = doc.selectFirst("div[data-productcode]");
        if (code != null) {
            return code.attr("data-productcode");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (json != null && json.has("name")) {
            return json.getString("name");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && json.has("price")) {
            return json.getBigDecimal("price");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Big W";
    }

    @Override
    public String getShipment() {
        return "In-store pickup";
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
