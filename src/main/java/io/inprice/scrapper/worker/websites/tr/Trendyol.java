package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Trendyol Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Trendyol extends AbstractWebsite {

    public Trendyol(Link link) {
        super(link);
    }

    @Override
    public String getSku() {
        String[] nameChunks = getName().split("\\s");
        if (nameChunks.length > 0) {
            return nameChunks[nameChunks.length-1].trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[name='twitter:title']");
        if (name != null) {
            return name.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("meta[name='twitter:data1']");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content").trim()));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst("meta[name='twitter:description']");
        if (seller != null) {
            String[] sellerChunks = seller.attr("content").split(":");
            if (sellerChunks.length > 0) {
                return sellerChunks[sellerChunks.length-1].trim();
            }
        }
        return "Trendyol";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.stamp.crg div");
        if (shipment != null) {
            return "Kargo Bedava";
        }

        shipment = doc.selectFirst("span.pr-in-dt-spn");
        if (shipment != null) {
            return shipment.text().trim() + " tarafından gönderilecektir.";
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        return getSeller();
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("div.pr-in-dt-cn ul span li"));
    }

}
