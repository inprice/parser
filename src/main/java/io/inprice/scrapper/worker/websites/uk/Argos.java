package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Argos extends AbstractWebsite {

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("[itemProp='sku']");
        if (sku != null) {
            return sku.attr("content").trim();
        }
        return null;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("span.product-title");
        if (name != null) {
            return name.text().trim();
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst(".product-price-primary");
        if (price != null) {
            return new BigDecimal(price.attr("content").trim());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Argos";
    }

    @Override
    public String getShipment() {
        return "Argos";
    }

    @Override
    public String getBrand() {
        Element seller = doc.selectFirst(".product-brand a");
        if (seller != null) {
            return seller.text().trim();
        }
        return null;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select(".product-description-content-text li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                specList.add(new LinkSpec("", spec.text().trim()));
            }
        }
        return specList;
    }
}
