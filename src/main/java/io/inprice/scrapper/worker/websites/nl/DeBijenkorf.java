package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO: "Data.product =" kismindaki json veri alinarak islem yapilsa daha dogru olur
 *
 *
 * Parser for DeBijenkorf the Netherlands
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class DeBijenkorf extends AbstractWebsite {

    public DeBijenkorf(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element inStock = doc.selectFirst("link[itemprop='availability']");
        if (inStock != null) {
            return inStock.attr("href").contains("InStock");
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("link[rel='canonical']");
        if (sku != null) {
            String href = sku.attr("href");
            if (! href.isEmpty()) {
                String[] hrefChunks = href.split("-");
                if (hrefChunks[hrefChunks.length-1].matches("\\d+")) {
                    return hrefChunks[hrefChunks.length-1];
                }
            }
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1[itemprop='name']");
        if (name != null) {
            return name.text();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("span[itemProp='price']");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.text()));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element seller = doc.select("ul.dbk-breadcrumb-group li.dbk-breadcrumb span").last();
        if (seller != null) {
            return seller.text();
        }
        return "DeBijenkorf";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("a[href='#dbk-ocp-delivery-info']");
        if (shipment != null) {
            return shipment.text();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("h1[itemProp='name'] a");
        if (brand != null) {
            return brand.text();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        //TODO: json veriden alinacak
        return null;
    }
}
