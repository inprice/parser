package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

public class NewLook extends AbstractWebsite {

    public NewLook(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        dsamfdasdflşsadfşl
        Element inStock = doc.selectFirst("meta[itemprop='availability']");
        if (inStock != null) {
            return inStock.attr("content").trim().equals("inStock");
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        Element code = doc.selectFirst("meta[itemprop='sku']");
        if (code != null) {
            return code.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element title = doc.selectFirst("li.active.list__item span[property='name']");
        if (title != null) {
            return title.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("meta[itemprop='price']");
        if (price != null) {
            return new BigDecimal(price.attr("content").trim());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "NewLook";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("span.product-delivery-link a");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("section[itemprop='brand'] meta[itemprop='name']");
        if (brand != null) {
            return brand.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("div.product-details--description.cms p"));
    }
}
