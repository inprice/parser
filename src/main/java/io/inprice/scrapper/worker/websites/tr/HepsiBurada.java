package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
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
        return isTrue("\"isInStock\":");
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("#addToCartForm input[name='sku']");
        if (sku != null) {
            return sku.val();
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("span[itemprop='name']");
        if (name != null) {
            return name.text();
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("span[itemprop='price']");
        if (price != null) {
            return new BigDecimal(cleanDigits(price.attr("content")));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst("span.seller a");
        if (seller != null) {
            return seller.text();
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getShipment() {
        Element shipping = doc.selectFirst("label.campaign-text span");
        if (shipping != null) {
            return shipping.text();
        }

        boolean freeShipping = isTrue("\"freeShipping\":");
        return "Ücretsiz Kargo: " + (freeShipping ? "Evet" : "Hayır");
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("span[itemprop='brand']");
        if (brand != null) {
            return brand.attr("content");
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getKeyValueSpecList(doc.select(".data-list.tech-spec tr"), "th", "td");
    }

    private boolean isTrue(String indicator) {
        final String result = findAPart(doc.html(), indicator, ",");
        return "true".equalsIgnoreCase(result);
    }

}
