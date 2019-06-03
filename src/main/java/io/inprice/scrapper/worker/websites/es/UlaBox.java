package io.inprice.scrapper.worker.websites.es;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for UlaBox Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class UlaBox extends AbstractWebsite {

    public UlaBox(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element product = doc.selectFirst("div.product-shop");
        if (product != null) {
            try {
                int quantity = new Integer(product.attr("data-product-qty"));
                return quantity > 0;
            } catch (Exception e) {
                //
            }
        }
        return false;
    }

    @Override
    public String getSku() {
        Element product = doc.selectFirst("div.product-shop");
        if (product != null) {
            return product.attr("data-product-id");
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element product = doc.selectFirst("div.product-shop");
        if (product != null) {
            return product.attr("data-product-name");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element product = doc.selectFirst("div.product-shop");
        if (product != null) {
            return new BigDecimal(cleanPrice(product.attr("data-price")));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "UlaBox";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.value-description");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("span.milli a.js-pjax");
        if (brand != null) {
            return brand.text().trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return null;
    }
}