package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for vidaXL Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class VidaXL extends AbstractWebsite {

    public VidaXL(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element inStock = doc.selectFirst("div#not-available div.false");
        return  (inStock != null);
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
        Element title = doc.selectFirst("h1[itemprop='name']");
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
        Element seller = doc.selectFirst("meta[itemprop='seller']");
        if (seller != null) {
            return seller.attr("content");
        }
        return "VidaXL";
    }

    @Override
    public String getShipment() {
        StringBuilder sb = new StringBuilder();
        Element shipment = doc.selectFirst("div.delivery-name");
        if (shipment != null) {
            sb.append(shipment.text());
            sb.append(". ");
        }

        shipment = doc.selectFirst("div.delivery-ship shipping-from");
        if (shipment != null) {
            sb.append(shipment.text());
            sb.append(". ");
        }

        shipment = doc.selectFirst("div.delivery-seller");
        if (shipment != null) {
            sb.append(shipment.text());
            sb.append(". ");
        }

        if (sb.length() == 0) sb.append("NA");

        return sb.toString().trim();
    }

    @Override
    public String getBrand() {
        final String indicator = "\"brand\":\"";

        int start = doc.html().indexOf(indicator) + indicator.length();
        int end   = doc.html().indexOf(",\"", start);

        if (start > indicator.length() && end > start) {
            return doc.html().substring(start, end);
        }

        return "VidaXL";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("ul.specs li"));
    }
}
