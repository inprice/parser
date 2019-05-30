package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for n11 Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class N11 extends AbstractWebsite {

    public N11(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element amount = doc.selectFirst("input[class='stockCount']");
        if (amount != null) {
            try {
                int realAmount = new Integer(amount.val().trim());
                return (realAmount > 0);
            } catch (Exception e) {
                //
            }
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("input[class='productId']");
        if (sku != null) {
            return sku.val().trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1.proName");
        if (name == null) name = doc.selectFirst("h1.pro-title_main");

        if (name != null) {
            return name.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst(".newPrice ins");
        if (price == null) price = doc.selectFirst("ins.price-now");

        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content").trim()));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        String value = null;

        Element seller = doc.selectFirst("div.sallerTop h3 a");
        if (seller != null) {
            value = seller.attr("title").trim();
        } else {
            seller = doc.selectFirst(".shop-name");
            if (seller != null) {
                value = seller.text().trim();
            }
        }
        return value;
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst(".shipment-detail-container .cargoType");
        if (shipment == null) shipment = doc.selectFirst(".delivery-info_shipment span");

        if (shipment != null) {
            return shipment.text().replaceAll(":", "").trim();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        String[] titleChunks = getName().split("\\s");
        if (titleChunks.length > 0) return titleChunks[0].trim();
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getKeyValueSpecList(doc.select("div.feaItem"), ".label", ".data");
    }
}
