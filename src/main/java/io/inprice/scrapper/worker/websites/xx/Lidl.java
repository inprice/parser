package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Lidl Global
 *
 * Contains standard data, all is extracted from html body and via json data in getJsonData()
 *
 * @author mdpinar
 */
public class Lidl extends AbstractWebsite {

    public Lidl(Link link) {
        super(link);
    }

    @Override
    public JSONObject getJsonData() {
        final String indicator = "var dynamic_tm_data = ";
        final String html = doc.html();

        int start = html.indexOf(indicator) + indicator.length();
        int end   = html.indexOf("};", start) + 1;

        if (start > indicator.length() && end > start) {
            return new JSONObject(html.substring(start, end));
        }

        return null;
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("product_instock")) {
            return json.getInt("product_instock") > 0;
        }
        return true;
    }

    @Override
    public String getSku() {
        if (json != null && json.has("productid")) {
            return json.getString("productid").trim();
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        if (json != null && json.has("productname")) {
            return json.getString("productname").trim();
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && json.has("amount")) {
            return json.getBigDecimal("amount");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Lidl";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.delivery span");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return "In-store pickup";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("productbrand")) {
            return json.getString("productbrand").trim();
        }

        Element brand = doc.selectFirst("div.brand div.brand__claim");
        if (brand != null && ! brand.text().isEmpty()) {
            return brand.text().trim();
        }

        String[] nameChunks = getName().split("\\s");
        if (nameChunks.length > 1) {
            return nameChunks[0];
        }

        return "Lidl";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        Elements specs = doc.select("div.product-detail-hero li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String value = spec.text().trim();
                specList.add(new LinkSpec("", value));
            }

            return specList;
        }

        specs = doc.select("div.attributebox__keyfacts li");
        if (specs == null || specs.size() == 0) specs = doc.select("div#detail-tab-0 li");

        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String strSpec = spec.text().trim();
                String key = "";
                String value = strSpec.trim();

                if (strSpec.contains(":")) {
                    String[] specChunks = strSpec.split(":");
                    if (specChunks.length > 1) {
                        key = specChunks[0].trim();
                        value = specChunks[1].trim();
                    }
                }
                specList.add(new LinkSpec(key, value));
            }
        }

        if (specs == null || specs.size() == 0) return getValueOnlySpecList(doc.select("div#detailtabProductDescriptionTab li"));

        return specList;
    }
}
