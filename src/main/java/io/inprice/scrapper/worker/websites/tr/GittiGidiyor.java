package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for GittiGidiyor Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class GittiGidiyor extends AbstractWebsite {

    public GittiGidiyor(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element amount = doc.getElementById("VariantProductRemaingCount");
        if (amount != null) {
            try {
                int realAmount = new Integer(amount.text().trim());
                return (realAmount > 0);
            } catch (Exception e) {
                //
            }
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        Element sku = doc.getElementById("productId");
        if (sku != null) {
            return sku.val().trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.getElementById("productTitle");
        if (name == null) name = doc.selectFirst("span.title");

        if (name != null) {
            return name.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("[data-price]");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("data-price").trim()));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst(".member-name a strong");
        if (seller != null) {
            return seller.text().trim();
        }
        return "NA";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst(".CargoInfos .color-black");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst(".mr10.gt-product-brand-0 a");
        if (brand != null) {
            return brand.text().trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        //TODO: the operation needed to be executed for value part --> String value = spec.select("strong").text().replaceAll(":", "").trim();
        return getKeyValueSpecList(doc.select("#specs-container ul li"), "span", "strong");
    }

}
