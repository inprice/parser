package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;


/**
 * Parser for NotebooksBilliger Deutschland
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class NotebooksBilliger extends AbstractWebsite {

    public NotebooksBilliger(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element availability = doc.selectFirst("div.availability_widget span.list_names");
        if (availability != null) {
            return availability.text().contains("Abholbereit");
        }
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("div#product_page_detail");
        if (sku != null) {
            return sku.attr("data-products-number").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.getElementById("product_detail_price");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content").trim()));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Notebooks Billiger";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.sameday img");
        if (shipment != null) {
            return shipment.attr("alt").trim();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("div.product_headline div.image_container img");
        if (brand != null) {
            String alt = brand.attr("alt").trim();
            if (! alt.isEmpty()) {
                return alt.substring(0, alt.lastIndexOf(" "));
            }
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("div#section_info li span"));
    }
}
