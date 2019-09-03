package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
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
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("div#product_page_detail");
        if (sku != null) {
            return sku.attr("data-products-number");
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content");
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.getElementById("product_detail_price");
        if (price != null) {
            return new BigDecimal(cleanDigits(price.attr("content")));
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
            return shipment.attr("alt");
        }
        return "Abholung im Geschäft";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("div.product_headline div.image_container img");
        if (brand != null) {
            String alt = brand.attr("alt");
            if (! alt.isEmpty()) {
                return alt.substring(0, alt.lastIndexOf(" "));
            }
        }

        final String brandName = findAPart(doc.html(), "\"productBrand\":\"", "\"");
        if (brandName != null) {
            return brandName;
        }

        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = getValueOnlySpecList(doc.select("div#section_info li span"));
        if (specList == null) specList = getKeyValueSpecList(doc.select("table.properties_table tr"), "td.produktDetails_eigenschaft2", "td.produktDetails_eigenschaft3");

        return specList;
    }
}
