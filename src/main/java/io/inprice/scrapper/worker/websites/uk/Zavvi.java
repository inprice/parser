package io.inprice.scrapper.worker.websites.uk;

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
 * Parser for Zavvi UK
 *
 * Some parts of data place in html body, some in json data which is also in html,
 * and a significant part of them is in json which is in a script tag
 *
 * @author mdpinar
 */
public class Zavvi extends AbstractWebsite {

    /*
     * the main data provider derived from json placed in html
     */
    private JSONObject product;

    public Zavvi(Link link) {
        super(link);
    }

    /**
     * Returns some info of the product as json
     *
     * @return json - partially has product data
     */
    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("script[type='application/ld+json']");
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().trim());

            if (data.has("offers")) {
                JSONArray offersArray = data.getJSONArray("offers");
                if (! offersArray.isEmpty()) {
                    if (offersArray.getJSONObject(0).has("sku")) {
                        product = offersArray.getJSONObject(0);
                    }
                }
            }

            return data;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        Element stock = doc.selectFirst("p.productStockInformation_prefix");
        if (stock != null) {
            return stock.text().contains("In stock");
        }
        return false;
    }

    @Override
    public String getSku() {
        if (product != null && product.has("offers")) {
            return product.getString("sku");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (json != null && json.has("name")) {
            return json.getString("name").trim();
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        if (product != null && product.has("price")) {
            return product.getBigDecimal("price");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Zavvi";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.productDeliveryAndReturns_message");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brand")) {
            return json.getJSONObject("brand").getString("name");
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div.productDescription_contentWrapper");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                Element key = spec.selectFirst("div.productDescription_contentPropertyName span");
                Element value = spec.selectFirst("div.productDescription_contentPropertyValue");

                String strKey = null;
                String strValue= null;

                if (key != null) {
                    strKey = key.text().replaceAll(":", "").trim();
                }
                if (value != null) strValue = value.text().trim();

                specList.add(new LinkSpec(strKey, strValue));
            }
        }
        return specList;
    }
}
