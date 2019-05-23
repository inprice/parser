package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NotebooksBilliger extends AbstractWebsite {

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
        return null;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content").trim();
        }
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        String strPrice = null;

        Element price = doc.getElementById("product_detail_price");
        if (price != null) {
            strPrice = price.attr("content").trim();
        }

        if (strPrice == null)
            return BigDecimal.ZERO;
        else
            return new BigDecimal(cleanPrice(strPrice));
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
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div#section_info li span");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String value = spec.text().trim();
                specList.add(new LinkSpec("", value));
            }
        }
        return specList;
    }
}
