package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Fnac France
 *
 * Contains standard data, all is extracted from html body and via json data in getJsonData()
 *
 * @author mdpinar
 */
public class Fnac extends AbstractWebsite {

    protected JSONObject offers;

    public Fnac(Link link) {
        super(link);
    }

    @Override
    public JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("script[type='application/ld+json']");
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().trim());
            if (data.has("offers")) {
                offers = data.getJSONObject("offers");
            }
            return data;
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (offers != null && offers.has("availability")) {
            String availability = offers.getString("availability");
            return availability.contains("InStock") || availability.contains("PreOrder");
        }
        return false;
    }

    @Override
    public String getSku() {
        if (json != null) {
            if (json.has("sku")) return json.getString("sku");
            if (json.has("prid")) return ""+json.getInt("prid");
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
        if (offers != null && offers.has("price")) {
            return offers.getBigDecimal("price");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        if (offers != null && offers.has("seller")) {
            JSONObject seller = offers.getJSONObject("seller");
            if (seller.has("name")) {
                return seller.getString("name");
            }
        }
        return "FNAC.COM";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("p.f-buyBox-shipping");
        if (shipment == null) shipment = doc.selectFirst("div.f-productSpecialsOffers-offerParagraphWrapper");

        if (shipment != null) {
            return shipment.text().trim();
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

        Elements specs = doc.select("table.f-productDetails-table tr");
        if (specs != null) {
            specList = new ArrayList<>();
            for (Element spec: specs) {
                Elements pairs = spec.select("td.f-productDetails-cell");
                if (pairs.size() == 1) {
                    specList.add(new LinkSpec("", pairs.get(0).text()));
                } else if (pairs.size() > 1) {
                    specList.add(new LinkSpec(pairs.get(0).text(), pairs.get(1).text()));
                }
            }
        }

        return specList;
    }
}
