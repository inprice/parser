package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for MediaWorld Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class MediaWorld extends AbstractWebsite {

    private JSONObject offers;

    public MediaWorld(Link link) {
        super(link);
    }

    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("div.main-content script[type='application/ld+json']");
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
            if (data.has("offers")) {
                offers = data.getJSONObject("offers");
            }
            return data;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        Element available = doc.selectFirst("div.product-detail-main-container");
        if (available != null) {
            return (available.attr("data-gtm-avail").contains("vailable"));
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("div[data-product-sku]");
        if (sku != null) {
            return sku.attr("data-product-sku");
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content");
        }
        if (json != null && json.has("name")) {
            return json.getString("name");
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        if (offers != null && offers.has("price")) {
            return offers.getBigDecimal("price");
        }

        Element price = doc.selectFirst("span[itemprop='price']");
        if (price != null) {
            return new BigDecimal(cleanDigits(price.attr("content")));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        if (offers != null && offers.has("seller")) {
            JSONObject seller = offers.getJSONObject("seller");
            return seller.getString("name");
        }
        return "Media World";
    }

    @Override
    public String getShipment() {
        return "Ritiro in negozio";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brand")) {
            JSONObject brand = json.getJSONObject("brand");
            return brand.getString("name");
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Element specs = doc.selectFirst("h2[itemprop='description']");
        if (specs != null) {
            specList = new ArrayList<>();
            String[] specChunks = specs.html().split("<br>");
            for (String spec: specChunks) {
                specList.add(new LinkSpec("", spec));
            }
        }
        return specList;
    }
}
