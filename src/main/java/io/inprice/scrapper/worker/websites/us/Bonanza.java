package io.inprice.scrapper.worker.websites.us;

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
 * Parser for Bonanza USA
 *
 * The data is in two parts:
 *  - in html body
 *  - price is handled via a rest call
 *
 *
 * @author mdpinar
 */
public class Bonanza extends AbstractWebsite {

    /*
     * The following data can only be gathered over spec list
     */
    private String sku = "NA";
    private String brand = "NA";
    private boolean availability;
    private List<LinkSpec> specList;

    public Bonanza(Link link) {
        super(link);
    }

    /**
     * This method is used as a initial data loader using product's spec list.
     * Class level variables are set over the spec list here.
     *
     * @return nothing!
     */
    @Override
    protected JSONObject getJsonData() {
        Elements specs = doc.select("table.extended_info_table tr.extended_info_row");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String key = spec.selectFirst("td.extended_info_label").text().replaceAll(":", "");
                String value = spec.selectFirst("p.extended_info_value_content").text();
                specList.add(new LinkSpec(key, value));

                if (key.equals("Item number")) sku = value;
                if (key.equals("Brand")) brand = value;

                if (key.equals("Quantity Available")) {
                    try {
                        String val = cleanPrice(value);
                        availability = (new Integer(val)) > 0;
                    } catch (Exception e) {
                        //
                    }
                }
            }
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        Element available = doc.selectFirst("meta[property='og:availability']");
        if (available != null) {
            return "instock".equals(available.attr("content"));
        }

        return availability;
    }

    @Override
    public String getSku() {
        Element skuEL = doc.selectFirst("meta[property='product:retailer_item_id']");
        if (skuEL != null) {
            return skuEL.attr("content").trim();
        }
        return sku;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content").trim();
        }

        name = doc.selectFirst("span[itemprop='name']");
        if (name != null) {
            return name.text();
        }

        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("meta[property='product:price:amount']");
        if (price != null) {
            return new BigDecimal(price.attr("content").trim());
        }

            price = doc.selectFirst("div.item_price");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.text()));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst("meta[property='wanelo:store:name']");
        if (seller != null) {
            return seller.attr("content").trim();
        }

        seller = doc.selectFirst("div.booth_link a");
        if (seller != null) {
            return seller.text();
        }

        return "Bonanza";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.free_shipping");
        if (shipment != null) {
            return "Free shipping";
        }

        return "NA";
    }

    @Override
    public String getBrand() {
        Element brandEL = doc.selectFirst("meta[property='product:brand']");
        if (brandEL != null) {
            return brandEL.attr("content").trim();
        }
        return brand;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return specList;
    }
}
