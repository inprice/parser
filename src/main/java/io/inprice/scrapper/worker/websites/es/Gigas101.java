package io.inprice.scrapper.worker.websites.es;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for 101Gigas Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Gigas101 extends AbstractWebsite {

    public Gigas101(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element available = doc.selectFirst("meta[property='product:availability']");
        if (available != null) {
            return available.attr("content").trim().contains("instock");
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("meta[property='product:retailer_part_no']");
        if (sku != null) {
            return sku.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1[itemprop='name']");
        if (name != null) {
            return name.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("meta[property='product:sale_price:amount']");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content")));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "101Gigas";
    }

    @Override
    public String getShipment() {
        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("meta[property='product:brand']");
        if (brand != null) {
            return brand.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        Elements specs = doc.select("div#desc_prop tr");

        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec: specs) {
                Element key = spec.selectFirst("td");
                Element value = key.nextElementSibling();
                specList.add(new LinkSpec(key.text().trim(), value.text().trim()));
            }
        }

        return specList;
    }
}