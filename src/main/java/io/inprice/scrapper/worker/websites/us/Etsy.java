package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Etsy US
 *
 * Contains standard data. Nothing special, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Etsy extends AbstractWebsite {

    public Etsy(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element quantity = doc.selectFirst("input[name='quantity']");
        if (quantity != null) {
            try {
                int qty = new Integer(quantity.attr("value"));
                return qty > 0;
            } catch (Exception e) {
                //
            }
        }

        Elements availabilities = doc.select("select#inventory-variation-select-quantity option");
        return  (availabilities != null && availabilities.size() > 0);
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("input[name='listing_id']");
        if (sku != null) {
            return sku.attr("value");
        }

        sku = doc.selectFirst("h1[data-listing-id]");
        if (sku != null) {
            return sku.attr("data-listing-id").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("meta[property='etsymarketplace:price_value']");
        if (price == null) price = doc.selectFirst("meta[property='product:price:amount']");

        if (price != null) {
            return new BigDecimal(price.attr("content"));
        }

        price = doc.selectFirst("span.override-listing-price");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.text().trim()));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element brand = doc.selectFirst("a[aria-label='Contact the shop']");
        if (brand != null) {
            return brand.attr("data-to_username");
        }
        return "NA";
    }

    @Override
    public String getShipment() {
        StringBuilder sb = new StringBuilder();
        Element shipment = doc.selectFirst("div.js-estimated-delivery div");
        if (shipment != null) {
            sb.append(shipment.text().trim());
            sb.append(". ");
        }

        shipment = doc.selectFirst("div.js-ships-from");
        if (shipment != null) {
            sb.append(shipment.text().trim());
            sb.append(". ");
        }

        shipment = doc.selectFirst("div.shipping-cost");
        if (shipment != null) {
            sb.append(shipment.text().trim());
            sb.append(". ");
        }

        if (sb.length() == 0) sb.append("NA");

        return sb.toString().trim();
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("a[aria-label='Contact the shop']");
        if (brand != null) {
            return brand.attr("data-to_user_display_name");
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("div.listing-page-overview-component.bg-white div p"));
    }

}
