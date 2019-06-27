package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Laredoute France
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Laredoute extends AbstractWebsite {

    private JSONObject offers;

    public Laredoute(Link link) {
        super(link);
    }

    @Override
    protected JSONObject getJsonData() {
        Element dataEL = doc.selectFirst("script[type='application/ld+json']");
        if (dataEL != null) {
            JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().trim());
            if (data.has("offers")) {
                offers = data.getJSONObject("offers");

                try {
                    JSONArray jarray = offers.getJSONArray("offers");
                    offers = jarray.getJSONObject(0);
                } catch (Exception e) {
                    //
                }
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
        Element sku = doc.getElementById("vendorsList");
        if (sku != null) {
            return sku.attr("data-prodid");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (json != null && json.has("name")) {
            return json.getString("name").trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        final String html = doc.html();
        final String indicator = "\"SalePriceAfterWithCharges\":";

        int start = html.indexOf(indicator) + indicator.length();
        int end = html.indexOf(",", start);

        if (start > indicator.length() && end > start) {
            return new BigDecimal(cleanPrice(html.substring(start, end)));
        }

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
        return "Laredoute";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("li.delivery-info-item.delivery-info.delivery-info-content");
        if (shipment != null) {
            return shipment.text().trim();
            /*
            String text = shipment.attr("data-text");
            String fee = text.replaceAll("<b>", "").replaceAll("</b>", "").replaceAll("\\[", "").replaceAll("]", "");
            if (fee.contains("DELIVERYPRICE")) {

                final String html = doc.html();
                final String indicator = "\"FormattedDeliveryFee\":";

                int start = html.indexOf(indicator) + indicator.length();
                int end = html.indexOf("\",", start);

                if (start > indicator.length() && end > start) {
                    String expense = html.substring(start, end);
                    return fee.replaceAll("DELIVERYPRICE", expense).replaceAll("\"", "").trim();
                }
            }
            return fee;

             */
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
        Element specs = doc.getElementsByTag("dscpdp").first();
        if (specs != null) {
            String[] specChunks;
            if (specs.text().indexOf("•") > 0)
                specChunks = specs.text().split("•");
            else
                specChunks = specs.text().split("\\.");

            if (specChunks.length > 0) {
                specList = new ArrayList<>(specChunks.length);
                for (String spec : specChunks) {
                    specList.add(new LinkSpec("", spec.trim()));
                }
            }
        }
        return specList;
    }
}
