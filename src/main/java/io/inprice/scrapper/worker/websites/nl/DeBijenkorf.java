package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for DeBijenkorf the Netherlands
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class DeBijenkorf extends AbstractWebsite {

    private JSONObject variant;

    public DeBijenkorf(Link link) {
        super(link);
    }

    @Override
    protected JSONObject getJsonData() {
        final String indicator = "Data.product =";

        int start = doc.html().indexOf(indicator) + indicator.length();
        int end   = doc.html().indexOf("};", start) + 1;

        if (start > indicator.length() && end > start) {
            JSONObject data = new JSONObject(doc.html().substring(start, end));
            if (data.has("product")) {
                JSONObject product = data.getJSONObject("product");
                if (product.has("currentVariantProduct")) {
                    variant = product.getJSONObject("currentVariantProduct");
                }
                return product;
            }
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (variant != null && variant.has("availability")) {
            JSONObject availability = variant.getJSONObject("availability");
            return availability.has("available") && availability.getBoolean("available");
        }
        return false;
    }

    @Override
    public String getSku() {
        if (json != null && json.has("code")) {
            return json.getString("code");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (json != null && json.has("displayName")) {
            return json.getString("displayName");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (variant != null && variant.has("sellingPrice")) {
            JSONObject sellingPrice = variant.getJSONObject("sellingPrice");
            return sellingPrice.getBigDecimal("value");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element seller = doc.select("ul.dbk-breadcrumb-group li.dbk-breadcrumb span").last();
        if (seller != null) {
            return seller.text();
        }
        return "DeBijenkorf";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("a[href='#dbk-ocp-delivery-info']");
        if (shipment != null) {
            return shipment.text();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brand")) {
            JSONObject brand = json.getJSONObject("brand");
            if (brand.has("name")) {
                return brand.getString("name");
            }
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        if (json != null && json.has("groupedAttributes")) {
            JSONArray groupedAttributes = json.getJSONArray("groupedAttributes");
            if (! groupedAttributes.isEmpty()) {
                specList = new ArrayList<>();
                for (int i = 0; i <  groupedAttributes.length(); i++) {
                    JSONObject gattr = groupedAttributes.getJSONObject(i);
                    if (! gattr.isEmpty() && gattr.has("attributes")) {
                        JSONArray attributes = gattr.getJSONArray("attributes");
                        for (int j = 0; j <  attributes.length(); j++) {
                            JSONObject attr = attributes.getJSONObject(j);
                            if (attr.has("label") && attr.has("value")) {
                              specList.add(new LinkSpec(attr.getString("label"), attr.getString("value")));
                            }
                        }
                    }
                }
            }
        }

        return specList;
    }
}
