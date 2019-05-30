package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for HepsiBurada Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class HepsiBurada extends AbstractWebsite {

    public HepsiBurada(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element available = doc.selectFirst("div.product-detail-box[style*='display: none']");
        return (available != null);
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("#addToCartForm input[name='sku']");
        if (sku != null) {
            return sku.val().trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.getElementById("product-name");
        if (name != null) {
            return name.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.getElementById("offering-price");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content").trim()));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst("input[name='merchantId']");
        if (seller != null) {
            return seller.val().trim();
        }
        return "NA";
    }

    @Override
    public String getShipment() {
        //TODO: static text is not suitable, must be reasonable
        return "50 TL ve üzeri Kargo Bedava";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst(".brand-name a");
        if (brand != null) {
            return brand.text().trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getKeyValueSpecList(doc.select(".data-list.tech-spec tr"), "th", "td");
    }
}
